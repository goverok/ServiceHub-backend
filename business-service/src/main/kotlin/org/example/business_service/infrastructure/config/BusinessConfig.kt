package org.example.business_service.infrastructure.config

import org.example.business_service.application.CreateBusinessUseCase
import org.example.business_service.infrastructure.repository.BusinessRepositoryAdapter
import org.example.common.domain.repository.UserRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
class BusinessConfig(
    private val userRepository: UserRepository,
    private val businessRepository: BusinessRepositoryAdapter,
    private val passwordEncoder: PasswordEncoder
) {

    @Bean
    fun createBusinessUseCase() = CreateBusinessUseCase(
        userRepository = userRepository,
        businessRepository = businessRepository,
        passwordEncoder = passwordEncoder,
    )

}