package org.example.user_service.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class RegisterRequest(
    @field:NotBlank(message = "phone is required")
    @field:Size(min = 10, max = 20)
    val phone: String,

    @field:NotBlank(message = "password is required")
    @field:Size(min = 8, message = "password must be at least 8 characters")
    val password: String,

    val name: String? = null
)