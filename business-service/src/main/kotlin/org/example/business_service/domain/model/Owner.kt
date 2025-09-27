package org.example.business_service.domain.model

import org.example.common.domain.model.User

data class Owner(
    val user: User,
    val business: Business,
)