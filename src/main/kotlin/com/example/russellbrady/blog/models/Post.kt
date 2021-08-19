package com.example.russellbrady.blog.models

import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "posts")
class Post(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int,

    val title: String,

    val content: String,

    val createdOn: LocalDateTime,

    val updatedOn: LocalDateTime,

    val authorId: Int
)
