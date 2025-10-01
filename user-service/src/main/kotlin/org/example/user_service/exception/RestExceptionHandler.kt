package org.example.user_service.exception

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class RestExceptionHandler {

    @ExceptionHandler(ApiException::class)
    fun handleApi(ex: ApiException): ResponseEntity<Map<String, Any>> {
        val body = mapOf(
            "error" to ex.javaClass.simpleName,
            "message" to (ex.message ?: "")
        )
        return ResponseEntity.status(ex.status).body(body)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidation(ex: MethodArgumentNotValidException): ResponseEntity<Map<String, Any>> {
        val msg = ex.bindingResult.fieldErrors.joinToString("; ") { "${it.field}: ${it.defaultMessage}" }
        return ResponseEntity.badRequest().body(mapOf("error" to "ValidationError", "message" to msg))
    }

    @ExceptionHandler(Exception::class)
    fun handleOther(ex: Exception): ResponseEntity<Map<String, Any>> {
        ex.printStackTrace()
        return ResponseEntity.status(500)
            .body(mapOf("error" to "InternalError", "message" to (ex.message ?: "Unknown")))
    }
}