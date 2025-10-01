package org.example.user_service.dto

import org.example.user_service.entity.UserRole
import java.time.Instant
import java.util.*

data class UserDto(
    val id: UUID,
    val phone: String,
    val name: String?,
    val role: UserRole,
    val createdAt: Instant
)