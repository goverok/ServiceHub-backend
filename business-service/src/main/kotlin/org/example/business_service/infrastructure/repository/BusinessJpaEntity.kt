package org.example.business_service.infrastructure.repository

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import org.example.business_service.domain.model.Business

@Entity
@Table(
    name = "businesses",
    uniqueConstraints = [UniqueConstraint(columnNames = ["name", "owner_id"])]
)
class BusinessJpaEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    val name: String,

    @Column(nullable = false)
    val address: String,

    @Column(name = "owner_id", nullable = false)
    val ownerId: Long,

    @Column(nullable = false)
    val active: Boolean = true

)

fun Business.toJpa(): BusinessJpaEntity {
    return BusinessJpaEntity(
        name = name,
        address = address,
        ownerId = ownerId,
        active = active,
    )
}
