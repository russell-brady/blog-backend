package com.example.russellbrady.blog.models

import com.example.russellbrady.blog.dto.PostDto
import com.example.russellbrady.blog.dto.PostForm
import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
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

    val updatedOn: LocalDateTime?,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    val author: User
) {

    constructor(postForm: PostForm, author: User, createdOn: LocalDateTime) : this(
        -1,
        postForm.title!!,
        postForm.content!!,
        createdOn,
        null,
        author
    )

    fun toPostDto() = PostDto(
        id, title, content, createdOn, updatedOn, author.id
    )

    fun update(postForm: PostForm, updatedOn: LocalDateTime): Post {
        return Post(id, postForm.title!!, postForm.content!!, createdOn, updatedOn, author)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Post) return false

        if (id != other.id) return false
        if (title != other.title) return false
        if (content != other.content) return false
        if (createdOn != other.createdOn) return false
        if (updatedOn != other.updatedOn) return false
        if (author.id != other.author.id) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + title.hashCode()
        result = 31 * result + content.hashCode()
        result = 31 * result + createdOn.hashCode()
        result = 31 * result + (updatedOn?.hashCode() ?: 0)
        result = 31 * result + author.id.hashCode()
        return result
    }

    override fun toString(): String {
        return "Post(id=$id, title='$title', content='$content', createdOn=$createdOn, updatedOn=$updatedOn, authorId=${author.id})"
    }
}
