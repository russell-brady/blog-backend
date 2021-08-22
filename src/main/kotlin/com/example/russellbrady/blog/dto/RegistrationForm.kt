package com.example.russellbrady.blog.dto

import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank

data class RegistrationForm(
    @field:NotBlank
    val username: String?,

    @field:NotBlank
    @field:Email
    val emailAddress: String?,

    @field:NotBlank
    val password: String?
)
