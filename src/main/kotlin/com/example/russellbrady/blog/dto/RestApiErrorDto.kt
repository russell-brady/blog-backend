package com.example.russellbrady.blog.dto

import java.time.LocalDateTime
import org.springframework.http.HttpStatus

data class RestApiErrorDto(
    val httpStatus: HttpStatus,
    val message: String,
    val timestamp: LocalDateTime,
    val apiSubErrors: List<ApiSubErrorDto> = listOf()
)

data class ApiSubErrorDto(
    val objectName: String?,
    val field: String? = "",
    val rejectedValue: Any? = null,
    val message: String?,
    val fieldErrorCodes: List<String> = emptyList()
)
