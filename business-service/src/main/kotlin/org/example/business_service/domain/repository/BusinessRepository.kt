package org.example.business_service.domain.repository

import org.example.business_service.domain.model.Business

interface BusinessRepository {

    fun save(user: Business): Business

    fun findById(id: Long): Business?
}