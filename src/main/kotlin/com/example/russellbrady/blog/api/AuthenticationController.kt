package com.example.russellbrady.blog.api

import com.example.russellbrady.blog.dto.LoginForm
import com.example.russellbrady.blog.dto.RegistrationForm
import com.example.russellbrady.blog.services.AuthenticationService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class AuthenticationController(
    val authenticationService: AuthenticationService
) {

    @PostMapping("/register")
    fun register(@RequestBody registrationForm: RegistrationForm): ResponseEntity<Unit> {
        return authenticationService.register(registrationForm).let { ResponseEntity.ok().build() }
    }

    @PostMapping("/login")
    fun login(@RequestBody loginForm: LoginForm): ResponseEntity<Unit> {
        return authenticationService.login(loginForm).let { ResponseEntity.ok().build() }
    }
}
