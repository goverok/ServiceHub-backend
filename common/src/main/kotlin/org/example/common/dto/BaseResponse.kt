package org.example.common.dto

data class BaseResponse<T>(
    val status: Status,
    val message: String? = null,
    val data: T? = null,
    val errors: List<String>? = null
)

enum class Status {
    SUCCESS,
    ERROR
}