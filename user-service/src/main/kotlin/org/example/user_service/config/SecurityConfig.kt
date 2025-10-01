package org.example.user_service.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.DelegatingPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity(debug = true)
@EnableMethodSecurity
class SecurityConfig(
    private val jwtAuthFilter: JwtAuthFilter
) {

    /**
     * Provide a PasswordEncoder bean. Default: Argon2.
     * DelegatingPasswordEncoder allows upgrade in future while keeping old passwords valid.
     */
    @Bean
    fun passwordEncoder(): PasswordEncoder {
        val idForEncode = "argon2"
        val encoders: MutableMap<String, PasswordEncoder> = HashMap()
        encoders["argon2"] = Argon2PasswordEncoder(
            16,     // длина соли в байтах
            32,     // длина хэша в байтах
            1,     // количество потоков
            1 shl 12,   // 4096 KB памяти (2^12)
            3       // число итераций
        )
        encoders["bcrypt"] = BCryptPasswordEncoder()
        return DelegatingPasswordEncoder(idForEncode, encoders)
    }

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        return http
            .csrf { it.disable() }
            .authorizeHttpRequests { auth ->
                auth.requestMatchers(
                    "/api/v1/auth/register",
                    "/api/v1/auth/login",
                    "/api/v1/auth/refresh",
                ).permitAll()

                auth.anyRequest().authenticated()
            }
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter::class.java)
            .build()
    }
}