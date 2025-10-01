package org.example.user_service.service

import jakarta.transaction.Transactional
import org.example.user_service.dto.RegisterRequest
import org.example.user_service.dto.UserDto
import org.example.user_service.entity.UserEntity
import org.example.user_service.entity.UserRole
import org.example.user_service.exception.BadRequestException
import org.example.user_service.exception.ConflictException
import org.example.user_service.repository.UserRepository
import org.example.user_service.utils.PhoneUtils
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.*


@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
) {
    /**
     * Register a new user. Validates phone (Russia only) and unique constraint.
     */
    @Transactional
    fun register(req: RegisterRequest): UserDto {
        val normalized = PhoneUtils.normalizeRussianPhone(req.phone)
            ?: throw BadRequestException("phone must be a valid Russian number (e.g. +7XXXXXXXXXX or 8XXXXXXXXXX)")

        // enforce unique (case-insensitive)
        if (userRepository.existsByPhone(normalized)) {
            throw ConflictException("phone already registered")
        }

        val passwordHash = passwordEncoder.encode(req.password)

        val entity = UserEntity(
            id = UUID.randomUUID(),
            phone = normalized,
            passwordHash = passwordHash,
            name = req.name,
            role = UserRole.CLIENT,
            createdAt = Instant.now()
        )

        val saved = userRepository.save(entity)
        return UserDto(
            id = saved.id,
            phone = saved.phone,
            name = saved.name,
            role = saved.role,
            createdAt = saved.createdAt
        )
    }
}