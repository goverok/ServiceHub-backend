package org.example.business_service.domain.model

import org.example.business_service.infrastructure.repository.BusinessJpaEntity

data class Business(
    val name: String,
    val address: String,
    val ownerId: Long,
    val active: Boolean = true
)

fun BusinessJpaEntity.toDomain(): Business {
    return Business(
        name = name,
        address = address,
        ownerId = ownerId,
        active = active,
    )
}