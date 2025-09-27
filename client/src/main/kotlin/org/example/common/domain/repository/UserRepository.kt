package org.example.common.domain.repository

import org.example.common.domain.model.User

interface UserRepository {
    fun save(user: User): User
    fun findById(id: Long): User?
    fun findByPhone(phone: String): User?
    fun findAll(): List<User>
    fun deleteById(id: Long)
    fun delete(user: User)
    fun isUserExists(phone: String): Boolean
}