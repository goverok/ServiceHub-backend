package org.example.user_service.service

import jakarta.transaction.Transactional
import org.example.user_service.dto.LoginRequest
import org.example.user_service.dto.LogoutRequest
import org.example.user_service.dto.RefreshRequest
import org.example.user_service.dto.TokenResponse
import org.example.user_service.entity.RefreshTokenEntity
import org.example.user_service.exception.ApiException
import org.example.user_service.repository.RefreshTokenRepository
import org.example.user_service.repository.UserRepository
import org.example.user_service.security.JwtProvider
import org.example.user_service.utils.PhoneUtils
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.UUID

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val refreshTokenRepository: RefreshTokenRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtProvider: JwtProvider,
) {

    @Transactional
    fun login(req: LoginRequest): TokenResponse {
        val normalized = PhoneUtils.normalizeRussianPhone(req.phone)
            ?: throw ApiException(HttpStatus.BAD_REQUEST, "Invalid phone format")

        val user = userRepository.findByPhone(normalized)
            ?: throw ApiException(HttpStatus.UNAUTHORIZED, "User not found")

        if (!passwordEncoder.matches(req.password, user.passwordHash)) {
            throw ApiException(HttpStatus.UNAUTHORIZED, "Invalid credentials")
        }

        val accessToken = jwtProvider.generateAccessToken(user.id, user.role)
        val (refreshToken, expiresAt) = jwtProvider.generateRefreshToken()

        val entity = RefreshTokenEntity(
            user = user,
            token = refreshToken,
            expiresAt = expiresAt
        )
        refreshTokenRepository.save(entity)

        return TokenResponse(
            accessToken = accessToken,
            refreshToken = refreshToken,
            expiresIn = 15 * 60 // 15 min in seconds
        )
    }

    @Transactional
    fun refresh(req: RefreshRequest): TokenResponse {
        val tokenEntity = refreshTokenRepository.findByToken(req.refreshToken)
            ?: throw ApiException(HttpStatus.UNAUTHORIZED, "Invalid refresh token")

        if (tokenEntity.revoked || tokenEntity.expiresAt.isBefore(Instant.now())) {
            throw ApiException(HttpStatus.UNAUTHORIZED, "Refresh token expired or revoked")
        }

        val user = tokenEntity.user
        val accessToken = jwtProvider.generateAccessToken(user.id, user.role)
        val (newRefreshToken, expiresAt) = jwtProvider.generateRefreshToken()

        // revoke old token, issue new
        val updated = tokenEntity.copy(revoked = true)
        refreshTokenRepository.save(updated)

        val newEntity = RefreshTokenEntity(
            user = user,
            token = newRefreshToken,
            expiresAt = expiresAt
        )
        refreshTokenRepository.save(newEntity)

        return TokenResponse(
            accessToken = accessToken,
            refreshToken = newRefreshToken,
            expiresIn = 15 * 60
        )
    }

    @Transactional
    fun logout(req: LogoutRequest) {
        val tokenEntity = refreshTokenRepository.findByToken(req.refreshToken)
            ?: throw ApiException(HttpStatus.UNAUTHORIZED, "Invalid refresh token")

        val updated = tokenEntity.copy(revoked = true)
        refreshTokenRepository.save(updated)
    }

    @Transactional
    fun logoutAll(userId: UUID) {
        val user = userRepository.findById(userId)
            .orElseThrow { ApiException(HttpStatus.NOT_FOUND, "User not found") }

        val tokens = refreshTokenRepository.findAll().filter { it.user.id == user.id && !it.revoked }
        tokens.forEach { token ->
            refreshTokenRepository.save(token.copy(revoked = true))
        }
    }
}