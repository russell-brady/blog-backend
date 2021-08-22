package com.example.russellbrady.blog.dto

import javax.validation.constraints.NotBlank

data class PostForm(

    @field:NotBlank
    val title: String?,

    @field:NotBlank
    val content: String?
)
