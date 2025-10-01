package org.example.user_service.config

import io.jsonwebtoken.Claims
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.example.user_service.entity.UserRole
import org.example.user_service.security.JwtProvider
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.util.UUID

@Component
class JwtAuthFilter(
    private val jwtProvider: JwtProvider,
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authHeader = request.getHeader("Authorization")

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            val token = authHeader.substring(7)
            try {
                val claims: Claims = jwtProvider.parseClaims(token)
                val userId = UUID.fromString(claims.subject)
                val role = UserRole.valueOf(claims["role"] as String)

                println("******************* role = $role")

                val authorities = RoleMapper.map(role)

                println("******************* authentication = $authorities")

                val authentication = UsernamePasswordAuthenticationToken(
                    userId, // principal = userId
                    null,   // credentials
                    authorities
                )

                println("******************* authentication = $authentication")
                authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
                SecurityContextHolder.getContext().authentication = authentication
            } catch (ex: Exception) {
                println("*******************")
                ex.printStackTrace()
                // токен невалиден или истёк → оставляем SecurityContext пустым
            }
        }

        filterChain.doFilter(request, response)
    }
}