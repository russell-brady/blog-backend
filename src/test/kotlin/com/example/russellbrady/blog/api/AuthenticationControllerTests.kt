package com.example.russellbrady.blog.api

import com.example.russellbrady.blog.dto.LoginForm
import com.example.russellbrady.blog.dto.RegistrationForm
import com.example.russellbrady.blog.dto.UserDto
import com.example.russellbrady.blog.models.User
import com.example.russellbrady.blog.utils.toJson
import com.example.russellbrady.blog.utils.toObject
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class AuthenticationControllerTests : BaseControllerTest() {

    val baseUrl = "/api/auth"

    @Autowired
    lateinit var passwordEncoder: PasswordEncoder

    @Test
    fun `register - Should successfully register user - When valid user form data`() {
        val registrationForm = RegistrationForm("username", "email@email.com", "password")

        perform(post("$baseUrl/register")
            .content(registrationForm.toJson())
            .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk)

        assertThat(userRepository.findByUsername(registrationForm.username!!)).isNotNull
    }

    @Test
    fun `register - Should not register user - When invalid user form data`() {
        val registrationForm = RegistrationForm("", null, "password")

        perform(post("$baseUrl/register")
            .content(registrationForm.toJson())
            .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isUnprocessableEntity)
    }

    @Test
    fun `login - Should successfully login user - When valid user credentials`() {
        val password = "password"
        val user = userRepository.save(User("username", "email@email.com", passwordEncoder.encode(password)))
        val loginForm = LoginForm(user.username, password)

        perform(post("$baseUrl/login")
            .content(loginForm.toJson())
            .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk)
    }

    @Test
    fun `login - Should not login user - When invalid user credentials`() {
        val password = "password"
        val user = userRepository.save(User("username", "email@email.com", passwordEncoder.encode(password)))
        val invalidLoginForm = LoginForm(user.username, "otherpassword")

        perform(post("$baseUrl/login")
            .content(invalidLoginForm.toJson())
            .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isUnauthorized)
    }

    @Test
    fun `getMe - Should return unauthorized - When user is not logged in`() {
        perform(get("$baseUrl/me")).andExpect(status().isUnauthorized)
    }

    @Test
    fun `getMe - Should return user details - When user is logged in`() {
        val user = createUser("username")

        val mvcResponse = perform(get("$baseUrl/me").with(user(user.username)))
            .andExpect(status().isOk)
            .andReturn()

        val response: UserDto = mvcResponse.response.contentAsString.toObject()

        assertThat(response.emailAddress).isEqualTo(user.emailAddress)
        assertThat(response.username).isEqualTo(user.username)
    }
}
