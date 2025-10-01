package org.example.user_service.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.Table
import java.time.Instant
import java.util.*

@Entity
@Table(name = "users", indexes = [Index(name = "ux_users_phone", columnList = "lower(phone)")])
data class UserEntity(
    @Id
    val id: UUID = UUID.randomUUID(),

    @Column(nullable = false, unique = true, length = 20)
    var phone: String,

    @Column(name = "password_hash", nullable = false)
    var passwordHash: String,

    @Column
    var name: String? = null,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var role: UserRole = UserRole.CLIENT,

    @Column(name = "created_at", nullable = false)
    var createdAt: Instant = Instant.now(),

    @Column(name = "last_login_at")
    var lastLoginAt: Instant? = null
)

enum class UserRole {
    CLIENT, OWNER, ADMIN, STAFF
}