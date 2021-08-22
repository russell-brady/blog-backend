package com.example.russellbrady.blog.api

import com.example.russellbrady.blog.dto.ApiSubErrorDto
import com.example.russellbrady.blog.dto.RestApiErrorDto
import com.example.russellbrady.blog.services.TimeService
import javax.persistence.EntityNotFoundException
import javax.validation.ConstraintViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class RestControllerExceptionAdvice(
    val timeService: TimeService
) {

    @ExceptionHandler(EntityNotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleEntityNotFoundException(exception: EntityNotFoundException): ResponseEntity<RestApiErrorDto> {
        return ResponseEntity(
            RestApiErrorDto(HttpStatus.NOT_FOUND, exception.localizedMessage, timeService.nowDateTime()),
            HttpStatus.NOT_FOUND
        )
    }

    @ExceptionHandler(IllegalStateException::class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    fun handleIllegalStateException(illegalStateException: IllegalStateException): ResponseEntity<RestApiErrorDto> {
        return ResponseEntity(
            RestApiErrorDto(HttpStatus.UNPROCESSABLE_ENTITY, illegalStateException.localizedMessage, timeService.nowDateTime()),
            HttpStatus.UNPROCESSABLE_ENTITY
        )
    }

    @ExceptionHandler(ConstraintViolationException::class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    fun handleConstraintViolationException(ex: ConstraintViolationException): ResponseEntity<RestApiErrorDto> {
        val statusCode = HttpStatus.UNPROCESSABLE_ENTITY
        val restApiError = RestApiErrorDto(
            statusCode,
            "Constraint Violations",
            timeService.nowDateTime(),
            ex.constraintViolations.map {
                ApiSubErrorDto(it.rootBeanClass.name, it.propertyPath.toString(), it.invalidValue, it.message)
            }
        )
        return ResponseEntity(restApiError, statusCode)
    }
}
