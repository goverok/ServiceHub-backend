package org.example.user_service.controller

import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/test")
class TestRbacController {

    @GetMapping("/owner")
    @PreAuthorize("hasRole('OWNER')")
    fun ownerOnly(): Map<String, String> =
        mapOf("msg" to "Hello OWNER!")

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    fun adminOnly(): Map<String, String> =
        mapOf("msg" to "Hello ADMIN!")

    @GetMapping("/client")
    @PreAuthorize("hasRole('CLIENT')")
    fun clientOnly(): Map<String, String> =
        mapOf("msg" to "Hello CLIENT!")
}