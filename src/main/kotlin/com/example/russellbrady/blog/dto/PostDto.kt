package com.example.russellbrady.blog.dto

import java.time.LocalDateTime

data class PostDto(
    val id: Int,
    val title: String,
    val content: String,
    val createdOn: LocalDateTime,
    val updatedOn: LocalDateTime?,
    val authorId: Int
)
