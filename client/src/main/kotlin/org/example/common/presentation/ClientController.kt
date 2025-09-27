package org.example.common.presentation

import org.example.common.application.CreateUserUseCase
import org.example.common.domain.model.User
import org.example.common.dto.BaseResponse
import org.example.common.dto.Status
import org.example.common.presentation.dto.request.CreateClientRequest
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/client")
internal class ClientController(
    private val createUserUseCase: CreateUserUseCase,
) {
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    fun registerClient(@RequestBody request: CreateClientRequest): BaseResponse<User> {
        val user = createUserUseCase.execute(
            phone = request.phone,
            password = request.password
        )
        return BaseResponse(
            status = Status.SUCCESS,
            message = "Client registered successfully",
            data = user,
        )
    }
}