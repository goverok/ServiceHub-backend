package org.example.user_service.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.example.user_service.entity.UserRole
import org.springframework.stereotype.Component
import java.security.Key
import java.time.Instant
import java.util.*

@Component
class JwtProvider {
    private val key: Key = Keys.secretKeyFor(SignatureAlgorithm.HS256)

    private val accessTokenValidity: Long = 15 * 60 // 15 минут
    private val refreshTokenValidity: Long = 15L * 24 * 60 * 60 // 15 дней

    fun generateAccessToken(userId: UUID, role: UserRole): String {
        val now = Instant.now()
        val expiry = now.plusSeconds(accessTokenValidity)
        return Jwts.builder()
            .setSubject(userId.toString())
            .claim("role", role.name)
            .setIssuedAt(Date.from(now))
            .setExpiration(Date.from(expiry))
            .signWith(key)
            .compact()
    }

    fun generateRefreshToken(): Pair<String, Instant> {
        val now = Instant.now()
        val expiry = now.plusSeconds(refreshTokenValidity)
        val token = UUID.randomUUID().toString() + UUID.randomUUID().toString()
        return token to expiry
    }

    fun parseUserId(token: String): UUID = UUID
        .fromString(Jwts.parser().setSigningKey(key).build().parseClaimsJws(token).body.subject)

    fun parseClaims(token: String): Claims =
        Jwts.parser()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .body
}