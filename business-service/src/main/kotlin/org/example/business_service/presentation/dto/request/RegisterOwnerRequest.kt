package org.example.business_service.presentation.dto.request

data class RegisterOwnerRequest(
    val phone: String,
    val password: String,
    val business: RegisterBusinessRequest,
)