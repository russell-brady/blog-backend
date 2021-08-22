package com.example.russellbrady.blog.services

import com.example.russellbrady.blog.dto.PostDto
import com.example.russellbrady.blog.dto.PostForm
import com.example.russellbrady.blog.models.Post
import com.example.russellbrady.blog.repositories.PostRepository
import com.example.russellbrady.blog.repositories.UserRepository
import javax.persistence.EntityNotFoundException
import javax.validation.Valid
import org.springframework.stereotype.Service
import org.springframework.validation.annotation.Validated

@Validated
interface PostService {
    fun getPost(id: Int): PostDto
    fun createPost(@Valid postForm: PostForm)
    fun updatePost(id: Int, @Valid postForm: PostForm)
    fun deletePost(id: Int)
    fun getUserPosts(username: String): List<PostDto>
}

@Service
class PostServiceImpl(
    val authenticationService: AuthenticationService,
    val timeService: TimeService,
    val postRepository: PostRepository,
    val userRepository: UserRepository
) : PostService {

    override fun getPost(id: Int): PostDto {
        return postRepository.findById(id)
            .orElseThrow { EntityNotFoundException("Post with ID $id not found") }
            .toPostDto()
    }

    override fun createPost(postForm: PostForm) {
        val user = authenticationService.getUserLoggedIn()
        val createdOn = timeService.nowDateTime()
        postRepository.save(Post(postForm, user, createdOn))
    }

    override fun updatePost(id: Int, postForm: PostForm) {
        val post = postRepository.findById(id)
            .orElseThrow { EntityNotFoundException("Post with ID $id not found") }

        val loggedInUser = authenticationService.getUserLoggedIn()
        if (post.author.id != loggedInUser.id)
            throw IllegalStateException("Logged in user doesn't own this post and is therefore not allowed to update it.")

        val updatedOn = timeService.nowDateTime()

        postRepository.save(post.update(postForm, updatedOn))
    }

    override fun deletePost(id: Int) {
        val post = postRepository.findById(id)
            .orElseThrow { EntityNotFoundException("Post with ID $id not found") }

        val loggedInUser = authenticationService.getUserLoggedIn()
        if (post.author.id != loggedInUser.id)
            throw IllegalStateException("Logged in user doesn't own this post and is therefore not allowed to delete it.")

        postRepository.delete(post)
    }

    override fun getUserPosts(username: String): List<PostDto> {
        val user = userRepository.findByUsername(username)
            ?: throw EntityNotFoundException("User with username $username not found")

        return user.posts.map { it.toPostDto() }
    }
}
