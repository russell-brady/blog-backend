package com.example.russellbrady.blog.repositories

import com.example.russellbrady.blog.models.User
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class UserRepositoryTests : BaseRepositoryTest() {

    @Autowired
    lateinit var userRepository: UserRepository

    @Test
    fun `findByEmailAddress - Should return user for given email address`() {
        val user = userRepository.save(User("user1", "user1@example.com", "password"))

        val actual = userRepository.findByEmailAddress(user.emailAddress)

        assertThat(actual).isNotNull
        assertThat(actual?.emailAddress).isEqualTo(user.emailAddress)
        assertThat(actual?.id).isEqualTo(user.id)

        assertThat(userRepository.findByEmailAddress("fakeemail@example.com")).isNull()
    }

    @Test
    fun `findByUsername - Should return user for given username`() {
        val user = userRepository.save(User("user1", "user1@example.com", "password"))

        val actual = userRepository.findByUsername(user.username)

        assertThat(actual).isNotNull
        assertThat(actual?.username).isEqualTo(user.username)
        assertThat(actual?.id).isEqualTo(user.id)

        assertThat(userRepository.findByEmailAddress("fakeemail@example.com")).isNull()
    }
}
