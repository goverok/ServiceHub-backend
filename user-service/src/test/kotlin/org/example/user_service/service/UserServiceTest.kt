package org.example.user_service.service

import org.example.user_service.dto.LoginRequest
import org.example.user_service.dto.LogoutRequest
import org.example.user_service.dto.RefreshRequest
import org.example.user_service.dto.RegisterRequest
import org.example.user_service.entity.RefreshTokenEntity
import org.example.user_service.entity.UserEntity
import org.example.user_service.entity.UserRole
import org.example.user_service.exception.ApiException
import org.example.user_service.exception.ConflictException
import org.example.user_service.repository.RefreshTokenRepository
import org.example.user_service.repository.UserRepository
import org.example.user_service.security.JwtProvider
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.never
import org.mockito.Mockito.times
import org.mockito.kotlin.any
import org.mockito.kotlin.check
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.security.crypto.password.PasswordEncoder
import java.time.Instant
import java.util.*

class UserServiceTest {

    private val userRepository: UserRepository = mock()

    private val refreshTokenRepository: RefreshTokenRepository = mock()

    private val passwordEncoder: PasswordEncoder = mock()

    private val jwtProvider: JwtProvider = JwtProvider()

    private val userService = UserService(userRepository, passwordEncoder)

    private val service = AuthService(
        userRepository = userRepository,
        refreshTokenRepository = refreshTokenRepository,
        passwordEncoder = passwordEncoder,
        jwtProvider,
    )

    private fun testUser(): UserEntity =
        UserEntity(
            id = UUID.randomUUID(),
            phone = "+79123456789",
            passwordHash = "hashed",
            name = "Ivan",
            role = UserRole.CLIENT
        )

    @Test
    fun `register success`() {
        val req = RegisterRequest(phone = "8 (912) 345-67-89", password = "password123", name = "Ivan")
        whenever(userRepository.existsByPhone("+79123456789")).thenReturn(false)
        whenever(passwordEncoder.encode("password123")).thenReturn("hashed")
        whenever(userRepository.save(any())).thenAnswer { invocation ->
            val arg = invocation.getArgument<UserEntity>(0)
            arg.copy(id = UUID.randomUUID())
        }

        val dto = userService.register(req)
        assertEquals("+79123456789", dto.phone)
        assertEquals("Ivan", dto.name)
        assertEquals(UserRole.CLIENT, dto.role)
        verify(userRepository).save(any())
    }

    @Test
    fun `register conflict when phone exists`() {
        val req = RegisterRequest(phone = "+7 912 345 6789", password = "password123")
        whenever(userRepository.existsByPhone("+79123456789")).thenReturn(true)

        val ex = org.junit.jupiter.api.assertThrows<ConflictException> {
            userService.register(req)
        }
        assertTrue(ex.message?.contains("phone already registered") ?: false)
        verify(userRepository, never()).save(any())
    }

    @Test
    fun `login success`() {
        val user = testUser()
        whenever(userRepository.findByPhone(user.phone)).thenReturn(user)
        whenever(passwordEncoder.matches("password123", user.passwordHash)).thenReturn(true)
        whenever(refreshTokenRepository.save(any())).thenAnswer { it.arguments[0] }

        val req = LoginRequest(user.phone, "password123")
        val resp = service.login(req)

        assertNotNull(resp.accessToken)
        assertNotNull(resp.refreshToken)
        assertEquals(900, resp.expiresIn)
    }

    @Test
    fun `login fails when wrong password`() {
        val user = testUser()
        whenever(userRepository.findByPhone(user.phone)).thenReturn(user)
        whenever(passwordEncoder.matches(any(), any())).thenReturn(false)

        val ex = assertThrows(ApiException::class.java) {
            service.login(LoginRequest(user.phone, "wrong"))
        }
        assertEquals(401, ex.status.value())
    }

    @Test
    fun `refresh success`() {
        val user = testUser()
        val oldToken = "old-token"
        val entity = RefreshTokenEntity(
            id = UUID.randomUUID(),
            user = user,
            token = oldToken,
            expiresAt = Instant.now().plusSeconds(3600),
            revoked = false
        )
        whenever(refreshTokenRepository.findByToken(oldToken)).thenReturn(entity)
        whenever(refreshTokenRepository.save(any())).thenAnswer { it.arguments[0] }

        val resp = service.refresh(RefreshRequest(oldToken))

        assertNotNull(resp.accessToken)
        assertNotEquals(oldToken, resp.refreshToken)
    }

    @Test
    fun `refresh fails when expired`() {
        val user = testUser()
        val oldToken = "expired"
        val entity = RefreshTokenEntity(
            id = UUID.randomUUID(),
            user = user,
            token = oldToken,
            expiresAt = Instant.now().minusSeconds(10),
            revoked = false
        )
        whenever(refreshTokenRepository.findByToken(oldToken)).thenReturn(entity)

        val ex = assertThrows(ApiException::class.java) {
            service.refresh(RefreshRequest(oldToken))
        }
        assertEquals(401, ex.status.value())
    }

    @Test
    fun `logout success`() {
        val user = testUser()
        val tokenEntity = RefreshTokenEntity(
            id = UUID.randomUUID(),
            user = user,
            token = "refresh123",
            expiresAt = Instant.now().plusSeconds(3600),
            revoked = false
        )
        whenever(refreshTokenRepository.findByToken("refresh123")).thenReturn(tokenEntity)
        whenever(refreshTokenRepository.save(any())).thenAnswer { it.arguments[0] }

        service.logout(LogoutRequest("refresh123"))
        verify(refreshTokenRepository).save(check { assertTrue(it.revoked) })
    }

    @Test
    fun `logoutAll revokes all tokens`() {
        val user = testUser()
        val tokens = listOf(
            RefreshTokenEntity(UUID.randomUUID(), user, "t1", Instant.now().plusSeconds(1000), false),
            RefreshTokenEntity(UUID.randomUUID(), user, "t2", Instant.now().plusSeconds(2000), false)
        )
        whenever(userRepository.findById(user.id)).thenReturn(Optional.of(user))
        whenever(refreshTokenRepository.findAll()).thenReturn(tokens)
        whenever(refreshTokenRepository.save(any())).thenAnswer { it.arguments[0] }

        service.logoutAll(user.id)
        verify(refreshTokenRepository, times(2)).save(any())
    }
}