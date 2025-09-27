package org.example.business_service.application

import jakarta.transaction.Transactional
import jakarta.validation.ValidationException
import org.example.business_service.domain.model.Business
import org.example.business_service.domain.model.Owner
import org.example.business_service.domain.repository.BusinessRepository
import org.example.business_service.presentation.dto.request.RegisterOwnerRequest
import org.example.common.domain.model.User
import org.example.common.domain.repository.UserRepository
import org.example.common.model.Role
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class CreateBusinessUseCase(
    private val userRepository: UserRepository,
    private val businessRepository: BusinessRepository,
    private val passwordEncoder: PasswordEncoder
) {
    @Transactional
    fun execute(request: RegisterOwnerRequest): Owner {
        if (userRepository.isUserExists(request.phone)) {
            throw ValidationException("Phone already registered")
        }

        val passwordHash = passwordEncoder.encode(request.password)

        val owner = User(
            phone = request.phone,
            passwordHash = passwordHash,
            roles = setOf(Role.OWNER, Role.CLIENT)
        )

        val savedOwner = userRepository.save(owner)

        val business = Business(
            name = request.business.name,
            address = request.business.address,
            ownerId = savedOwner.id!!
        )
        businessRepository.save(business)

        return Owner(
            user = owner,
            business = business,
        )
    }
}