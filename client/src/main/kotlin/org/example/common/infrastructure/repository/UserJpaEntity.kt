package org.example.common.infrastructure.repository

import org.example.common.domain.model.User
import org.example.common.model.UserJpaEntity

fun User.toJpa(): UserJpaEntity {
    return UserJpaEntity(
        id = id,
        phone = phone,
        passwordHash = passwordHash,
        roles = roles,
        active = active,
    )
}