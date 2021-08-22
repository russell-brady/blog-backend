package com.example.russellbrady.blog.services

import com.example.russellbrady.blog.dto.LoginForm
import com.example.russellbrady.blog.dto.RegistrationForm
import com.example.russellbrady.blog.models.User
import com.example.russellbrady.blog.repositories.UserRepository
import javax.persistence.EntityExistsException
import javax.persistence.EntityNotFoundException
import javax.validation.Valid
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.validation.annotation.Validated

@Validated
interface AuthenticationService {
    fun register(@Valid registrationForm: RegistrationForm)
    fun login(@Valid loginForm: LoginForm)
    fun getUserLoggedIn(): User
}

@Service
class AuthenticationServiceImpl(
    val passwordEncoder: PasswordEncoder,
    val authenticationManager: AuthenticationManager,
    val userRepository: UserRepository
) : AuthenticationService {

    override fun register(registrationForm: RegistrationForm) {
        if (userRepository.findByUsername(registrationForm.username!!) != null)
            throw EntityExistsException("User with username ${registrationForm.username} already exists")
        val password = passwordEncoder.encode(registrationForm.password)
        userRepository.save(User(registrationForm.username, registrationForm.emailAddress!!, password))
    }

    override fun login(loginForm: LoginForm) {
        val authentication = authenticationManager.authenticate(UsernamePasswordAuthenticationToken(loginForm.username, loginForm.password))
        SecurityContextHolder.getContext().authentication = authentication
    }

    override fun getUserLoggedIn(): User {
        val authentication = SecurityContextHolder.getContext().authentication.principal as UserDetails
        return userRepository.findByUsername(authentication.username)
            ?: throw EntityNotFoundException("User with username ${authentication.username} does not exist")
    }
}
