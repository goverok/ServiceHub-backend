package org.example.business_service.presentation

import org.example.business_service.application.CreateBusinessUseCase
import org.example.business_service.domain.model.Owner
import org.example.business_service.presentation.dto.request.RegisterOwnerRequest
import org.example.common.dto.BaseResponse
import org.example.common.dto.Status
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/business")
class BusinessController(
    private val createBusinessUseCase: CreateBusinessUseCase,
) {
    @PostMapping("/register-owner")
    @ResponseStatus(HttpStatus.CREATED)
    fun registerOwner(@RequestBody request: RegisterOwnerRequest): BaseResponse<Owner> {
        val owner = createBusinessUseCase.execute(
            request = request,
        )
        return BaseResponse(
            status = Status.SUCCESS,
            message = "Owner registered successfully",
            data = owner,
        )
    }
}