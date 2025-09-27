package org.example.business_service.infrastructure.repository

import org.example.business_service.domain.model.Business
import org.example.business_service.domain.model.toDomain
import org.example.business_service.domain.repository.BusinessRepository
import org.springframework.stereotype.Repository

@Repository
class BusinessRepositoryAdapter(
    private val jpa: BusinessJpaRepository
) : BusinessRepository {
    override fun save(user: Business): Business = jpa.save(user.toJpa()).toDomain()

    override fun findById(id: Long): Business? = jpa.findById(id).orElse(null)?.toDomain()
}