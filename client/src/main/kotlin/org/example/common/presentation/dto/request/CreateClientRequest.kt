package org.example.common.presentation.dto.request

internal data class CreateClientRequest(
    val phone: String,
    val password: String
)