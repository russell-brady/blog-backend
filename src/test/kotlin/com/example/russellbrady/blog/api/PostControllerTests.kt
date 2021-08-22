package com.example.russellbrady.blog.api

import com.example.russellbrady.blog.dto.PostDto
import com.example.russellbrady.blog.dto.PostForm
import com.example.russellbrady.blog.models.Post
import com.example.russellbrady.blog.models.User
import com.example.russellbrady.blog.repositories.PostRepository
import com.example.russellbrady.blog.utils.toJson
import com.example.russellbrady.blog.utils.toObject
import com.example.russellbrady.blog.utils.toObjectList
import java.time.LocalDateTime
import javax.persistence.EntityManager
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user
import org.springframework.test.annotation.Rollback
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class PostControllerTests : BaseControllerTest() {

    fun baseUrl(username: String) = "/api/users/$username/posts"

    @Autowired
    lateinit var passwordEncoder: PasswordEncoder

    @Autowired
    lateinit var postRepository: PostRepository

    @Autowired
    lateinit var entityManager: EntityManager

    lateinit var user: User
    lateinit var post: Post
    val now = LocalDateTime.now()

    @BeforeEach
    @Rollback(value = false)
    fun beforeAll() {
        user = createUser("user")
        post = postRepository.save(Post(PostForm("title", "content"), user, now))
    }

    @Test
    @WithMockUser
    fun `getUserPosts - Should return posts for a given username`() {
        val user = createUser("username")
        val post1 = postRepository.save(Post(PostForm("title", "content"), user, now))
        val post2 = postRepository.save(Post(PostForm("other title", "other content"), user, now))

        entityManager.clear()

        val mvcResponse = perform(get(baseUrl(user.username))).andExpect(status().isOk).andReturn()

        val posts: List<PostDto> = mvcResponse.response.contentAsString.toObjectList()

        assertThat(posts).hasSize(2).contains(post1.toPostDto()).contains(post2.toPostDto())
    }

    @Test
    @WithMockUser
    fun `getPost - Should return post for a given id`() {
        val user = userRepository.save(User("username", "email@email.com", passwordEncoder.encode("password")))
        val post = postRepository.save(Post(PostForm("title", "content"), user, now))

        val mvcResponse = perform(get("${baseUrl(user.username)}/${post.id}")).andExpect(status().isOk).andReturn()

        val posts: PostDto = mvcResponse.response.contentAsString.toObject()

        assertThat(posts).isEqualTo(post.toPostDto())
    }

    @Test
    fun `createPost - Should create a post with the given form fields`() {
        val postForm = PostForm("title", "content")

        perform(
            post(baseUrl(user.username))
                .with(user(user.username))
                .content(postForm.toJson())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
    }

    @Test
    fun `createPost - Should not create post - When invalid from fields`() {
        val postForm = PostForm("", null)

        perform(
            post(baseUrl(user.username))
                .with(user(user.username))
                .content(postForm.toJson())
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isUnprocessableEntity)
    }

    @Test
    fun `updatePost - Should return unauthorized - when logged in user doesn't own post for given id`() {
        val userWithNoPosts = createUser("other user")
        val patchForm = PostForm("new title", "new content")

        perform(
            patch("${baseUrl(user.username)}/${post.id}")
                .with(user(userWithNoPosts.username))
                .content(patchForm.toJson())
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isUnprocessableEntity)
    }

    @Test
    fun `updatePost - Should update post for a given id with new fields from Form`() {
        val patchForm = PostForm("new title", "new content")

        perform(
            patch("${baseUrl(user.username)}/${post.id}")
                .with(user(user.username))
                .content(patchForm.toJson())
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk)

        val updatedPost = postRepository.findById(post.id).get()
        assertThat(updatedPost.title).isEqualTo(patchForm.title)
        assertThat(updatedPost.content).isEqualTo(patchForm.content)
    }

    @Test
    fun `deletePost - Should return unauthorized - when logged in user doesn't own the post for a given id`() {
        val userWithNoPosts = createUser("other user")
        perform(delete("${baseUrl(user.username)}/${post.id}").with(user(userWithNoPosts.username)))
            .andExpect(status().isUnprocessableEntity)
    }

    @Test
    fun `deletePost - Should delete post for a given id`() {
        perform(delete("${baseUrl(user.username)}/${post.id}").with(user(user.username))).andExpect(status().isOk)
        assertThat(postRepository.findById(post.id)).isEmpty
    }
}
