package com.example.russellbrady.blog.services

import com.example.russellbrady.blog.dto.PostForm
import com.example.russellbrady.blog.models.Post
import com.example.russellbrady.blog.models.User
import com.example.russellbrady.blog.repositories.PostRepository
import com.example.russellbrady.blog.repositories.UserRepository
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import java.time.LocalDateTime
import java.util.Optional
import javax.persistence.EntityNotFoundException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class PostServiceTests {

    val authenticationService = mock<AuthenticationService>()
    val timeService = mock<TimeService>()
    val postRepository = mock<PostRepository>()
    val userRepository = mock<UserRepository>()

    val postService = PostServiceImpl(authenticationService, timeService, postRepository, userRepository)

    @Test
    fun `getPost - Should throw exception - When no post exists`() {
        val id = 1
        whenever(postRepository.findById(1)).thenReturn(Optional.empty())

        val exception = assertThrows<EntityNotFoundException> {
            postService.getPost(id)
        }

        assertThat(exception.message).isEqualTo("Post with ID $id not found")
    }

    @Test
    fun `getPost - Should return post - When post exists`() {
        val now = LocalDateTime.now()
        val user = User("", "", "")
        val post = Post(1, "", "", now, now, user)
        whenever(postRepository.findById(1)).thenReturn(Optional.of(post))

        val result = postService.getPost(post.id)

        assertThat(result.id).isEqualTo(post.id)
    }

    @Test
    fun `createPost - Should create post - For logged in user`() {
        val user = User("", "", "")
        val now = LocalDateTime.now()
        val postForm = PostForm("title", "content")

        whenever(authenticationService.getUserLoggedIn()).thenReturn(user)
        whenever(timeService.nowDateTime()).thenReturn(now)

        postService.createPost(postForm)

        verify(postRepository).save(Post(postForm, user, now))
    }

    @Test
    fun `updatePost - Should throw exception - When no post exists`() {
        val id = 1
        val postForm = PostForm("title", "content")
        whenever(postRepository.findById(id)).thenReturn(Optional.empty())

        val exception = assertThrows<EntityNotFoundException> {
            postService.updatePost(id, postForm)
        }

        assertThat(exception.message).isEqualTo("Post with ID $id not found")
    }

    @Test
    fun `updatePost - Should throw exception - When logged in user doesn't own the post`() {
        val postOwner = User(1, "", "", "", setOf())
        val otherOwner = User(2, "", "", "", setOf())
        val now = LocalDateTime.now()
        val post = Post(1, "", "", now, now, postOwner)
        val postForm = PostForm("title", "content")

        whenever(postRepository.findById(postOwner.id)).thenReturn(Optional.of(post))
        whenever(authenticationService.getUserLoggedIn()).thenReturn(otherOwner)

        val exception = assertThrows<Exception> {
            postService.updatePost(post.id, postForm)
        }

        assertThat(exception.message).isEqualTo("Logged in user doesn't own this post and is therefore not allowed to update it.")
    }

    @Test
    fun `updatePost - Should update post - When logged in user owns the post`() {
        val postOwner = User(1, "", "", "", setOf())
        val now = LocalDateTime.now()
        val post = Post(1, "", "", now, now, postOwner)
        val postForm = PostForm("title", "content")

        whenever(postRepository.findById(postOwner.id)).thenReturn(Optional.of(post))
        whenever(authenticationService.getUserLoggedIn()).thenReturn(postOwner)
        whenever(timeService.nowDateTime()).thenReturn(now)

        postService.updatePost(post.id, postForm)

        verify(postRepository).save(post.update(postForm, now))
    }

    @Test
    fun `deletePost - Should throw exception - When no post exists`() {
        val id = 1
        whenever(postRepository.findById(id)).thenReturn(Optional.empty())

        val exception = assertThrows<EntityNotFoundException> {
            postService.deletePost(id)
        }

        assertThat(exception.message).isEqualTo("Post with ID $id not found")
    }

    @Test
    fun `deletePost - Should throw exception - When logged in user doesn't own the post`() {
        val postOwner = User(1, "", "", "", setOf())
        val otherOwner = User(2, "", "", "", setOf())
        val now = LocalDateTime.now()
        val post = Post(1, "", "", now, now, postOwner)

        whenever(postRepository.findById(postOwner.id)).thenReturn(Optional.of(post))
        whenever(authenticationService.getUserLoggedIn()).thenReturn(otherOwner)

        val exception = assertThrows<Exception> {
            postService.deletePost(post.id)
        }

        assertThat(exception.message).isEqualTo("Logged in user doesn't own this post and is therefore not allowed to delete it.")
    }

    @Test
    fun `deletePost - Should delete post - When logged in user owns the post`() {
        val postOwner = User(1, "", "", "", setOf())
        val now = LocalDateTime.now()
        val post = Post(1, "", "", now, now, postOwner)

        whenever(postRepository.findById(postOwner.id)).thenReturn(Optional.of(post))
        whenever(authenticationService.getUserLoggedIn()).thenReturn(postOwner)
        whenever(timeService.nowDateTime()).thenReturn(now)

        postService.deletePost(post.id)

        verify(postRepository).delete(post)
    }
}
