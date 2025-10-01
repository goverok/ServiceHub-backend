package org.example.user_service.controller

import jakarta.validation.Valid
import org.example.user_service.dto.LoginRequest
import org.example.user_service.dto.LogoutRequest
import org.example.user_service.dto.RefreshRequest
import org.example.user_service.dto.RegisterRequest
import org.example.user_service.dto.TokenResponse
import org.example.user_service.dto.UserDto
import org.example.user_service.service.AuthService
import org.example.user_service.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestAttribute
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("/api/v1/auth")
class LoginController(
    private val userService: UserService,
    private val authService: AuthService
) {

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    fun register(@RequestBody @Valid request: RegisterRequest): UserDto {
        return userService.register(request)
    }

    @PostMapping("/login")
    fun login(@RequestBody @Valid request: LoginRequest): TokenResponse {
        return authService.login(request)
    }

    @PostMapping("/refresh")
    fun refresh(@RequestBody @Valid req: RefreshRequest): TokenResponse {
        return authService.refresh(req)
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun logout(@RequestBody @Valid req: LogoutRequest) {
        authService.logout(req)
    }

    @PostMapping("/logout/all")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun logoutAll(@RequestAttribute("userId") userId: UUID) {
        // userId нужно пробрасывать из фильтра, который валидирует AccessToken
        authService.logoutAll(userId)
    }
}