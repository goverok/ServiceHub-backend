package org.example.user_service.repository

import org.example.user_service.entity.RefreshTokenEntity
import org.example.user_service.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface RefreshTokenRepository : JpaRepository<RefreshTokenEntity, UUID> {
    fun findByToken(token: String): RefreshTokenEntity?
    fun deleteAllByUser(user: UserEntity)
}