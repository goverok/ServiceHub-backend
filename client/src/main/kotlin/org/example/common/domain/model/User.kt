package org.example.common.domain.model

import org.example.common.model.Role
import org.example.common.model.UserJpaEntity

data class User(
    val id: Long? = null,
    val phone: String,
    val passwordHash: String,
    val roles: Set<Role> = setOf(Role.CLIENT),
    val active: Boolean = true
)

fun UserJpaEntity.toDomain(): User {
    return User(
        id = id,
        phone = phone,
        passwordHash = passwordHash,
        roles = roles,
        active = active,
    )
}