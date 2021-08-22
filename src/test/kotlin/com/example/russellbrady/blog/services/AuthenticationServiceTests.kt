package com.example.russellbrady.blog.services

import com.example.russellbrady.blog.dto.RegistrationForm
import com.example.russellbrady.blog.models.User
import com.example.russellbrady.blog.repositories.UserRepository
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import javax.persistence.EntityExistsException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.crypto.password.PasswordEncoder

class AuthenticationServiceTests {

    val passwordEncoder = mock<PasswordEncoder>()
    val authenticationManager = mock<AuthenticationManager>()
    val userRepository = mock<UserRepository>()

    val authenticationService = AuthenticationServiceImpl(passwordEncoder, authenticationManager, userRepository)

    @Test
    fun `register - Should throw exception - When user for given username already exists`() {
        val registrationForm = RegistrationForm("test", "testEmail", "password")
        whenever(userRepository.findByUsername(registrationForm.username!!)).thenReturn(User("", "", ""))

        val exception = assertThrows<EntityExistsException> {
            authenticationService.register(registrationForm)
        }

        assertThat(exception.message).isEqualTo("User with username ${registrationForm.username} already exists")
    }

    @Test
    fun `register - Should save user with encoded password - When username does not already exist`() {
        val registrationForm = RegistrationForm("test", "testEmail", "password")
        val encodedPassword = "encodedPassword"
        whenever(userRepository.findByUsername(registrationForm.username!!)).thenReturn(null)
        whenever(passwordEncoder.encode(registrationForm.password)).thenReturn(encodedPassword)

        authenticationService.register(registrationForm)

        verify(userRepository).save(User(registrationForm.username!!, registrationForm.emailAddress!!, encodedPassword))
    }
}
