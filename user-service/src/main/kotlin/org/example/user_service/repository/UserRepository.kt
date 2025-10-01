package org.example.user_service.repository

import org.example.user_service.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface UserRepository : JpaRepository<UserEntity, UUID> {
    fun findByPhone(phone: String): UserEntity?
    fun existsByPhone(phone: String): Boolean
}