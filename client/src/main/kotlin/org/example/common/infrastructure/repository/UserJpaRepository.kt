package org.example.common.infrastructure.repository

import org.example.common.model.UserJpaEntity
import org.springframework.data.jpa.repository.JpaRepository

interface UserJpaRepository : JpaRepository<UserJpaEntity, Long> {
    fun findByPhone(phone: String): UserJpaEntity?
}