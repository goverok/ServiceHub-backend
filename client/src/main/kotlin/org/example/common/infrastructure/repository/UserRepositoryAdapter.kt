package org.example.common.infrastructure.repository

import org.example.common.domain.model.User
import org.example.common.domain.model.toDomain
import org.example.common.domain.repository.UserRepository
import org.springframework.stereotype.Repository

@Repository
class UserRepositoryAdapter(
    private val jpa: UserJpaRepository
) : UserRepository {
    override fun save(user: User): User = jpa.save(user.toJpa()).toDomain()

    override fun findById(id: Long): User? = jpa.findById(id).orElse(null)?.toDomain()

    override fun findByPhone(phone: String): User? = jpa.findByPhone(phone)?.toDomain()

    override fun findAll(): List<User> = jpa.findAll().map { it.toDomain() }

    override fun deleteById(id: Long) = jpa.deleteById(id)

    override fun delete(user: User) = jpa.delete(user.toJpa())

    override fun isUserExists(phone: String): Boolean = jpa.findByPhone(phone) != null
}