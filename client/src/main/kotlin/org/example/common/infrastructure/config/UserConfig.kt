package org.example.common.infrastructure.config

import org.example.common.application.CreateUserUseCase
import org.example.common.infrastructure.repository.UserRepositoryAdapter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
class UserConfig(
    private val repository: UserRepositoryAdapter,
    private val passwordEncoder: PasswordEncoder,
) {
    @Bean
    fun createUserUseCase() = CreateUserUseCase(
        userRepository = repository,
        passwordEncoder = passwordEncoder,
    )
}