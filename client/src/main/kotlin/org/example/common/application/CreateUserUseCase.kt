package org.example.common.application

import jakarta.validation.ValidationException
import org.example.common.domain.model.User
import org.example.common.domain.repository.UserRepository
import org.example.common.model.Role
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class CreateUserUseCase(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) {
    fun execute(phone: String, password: String): User {
        if (userRepository.isUserExists(phone)) {
            throw ValidationException("Phone already registered")
        }

        val passwordHash = passwordEncoder.encode(password)

        val user = User(
            phone = phone,
            passwordHash = passwordHash,
            roles = setOf(Role.CLIENT)
        )

        return userRepository.save(user)
    }
}