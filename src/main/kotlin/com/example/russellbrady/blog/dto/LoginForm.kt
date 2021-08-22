package com.example.russellbrady.blog.dto

import javax.validation.constraints.NotBlank

data class LoginForm(
    @field:NotBlank
    val username: String?,

    @field:NotBlank
    val password: String?
)
