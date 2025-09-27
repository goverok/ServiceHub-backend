package org.example.business_service.infrastructure.repository

import org.springframework.data.jpa.repository.JpaRepository

interface BusinessJpaRepository : JpaRepository<BusinessJpaEntity, Long>