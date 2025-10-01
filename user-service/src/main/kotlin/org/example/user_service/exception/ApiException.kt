package org.example.user_service.exception

import org.springframework.http.HttpStatus


open class ApiException(
    val status: HttpStatus,
    message: String
) : RuntimeException(message)

class ConflictException(message: String): ApiException(HttpStatus.CONFLICT, message)
class BadRequestException(message: String): ApiException(HttpStatus.BAD_REQUEST, message)