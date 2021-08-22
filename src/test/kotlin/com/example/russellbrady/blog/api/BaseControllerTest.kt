package com.example.russellbrady.blog.api

import com.example.russellbrady.blog.models.User
import com.example.russellbrady.blog.repositories.UserRepository
import java.time.Instant
import javax.transaction.Transactional
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder

@ExtendWith(SpringExtension::class)
@SpringBootTest
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
class BaseControllerTest {

    @Autowired
    lateinit var mvc: MockMvc

    @Autowired
    lateinit var userRepository: UserRepository

    fun perform(request: MockHttpServletRequestBuilder): ResultActions {
        return mvc.perform(request)
    }

    fun createUser(username: String, emailAddress: String = "email@email.com", password: String = "password", passwordTimestamp: Instant = Instant.now()): User {
        val encodedPassword = BCryptPasswordEncoder().encode(password)
        return userRepository.save(User(username, emailAddress, encodedPassword))
    }
}
