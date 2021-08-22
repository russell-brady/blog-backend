package com.example.russellbrady.blog.repositories

import com.example.russellbrady.blog.dto.PostForm
import com.example.russellbrady.blog.models.Post
import com.example.russellbrady.blog.models.User
import java.time.LocalDateTime
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class PostRepositoryTests : BaseRepositoryTest() {

    @Autowired
    lateinit var postRepository: PostRepository

    @Autowired
    lateinit var userRepository: UserRepository

    @Test
    fun `findById - Should return single record for a given Id`() {
        val now = LocalDateTime.now()
        val user = userRepository.save(User("", "", ""))
        val post = postRepository.save(Post(PostForm("", ""), user, now))

        assertThat(postRepository.findById(post.id).get()).isEqualTo(post)
        assertThat(postRepository.findById(100)).isEmpty
    }
}
