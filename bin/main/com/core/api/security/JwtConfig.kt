package com.core.api.security

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.github.cdimascio.dotenv.dotenv
import java.util.Date

object JwtConfig {
    private val env = dotenv()
    private val secret = env["JWT_SECRET"] ?: "default-secret"
    private val issuer = env["JWT_ISSUER"] ?: "http://localhost:8080"
    val audience = env["JWT_AUDIENCE"] ?: "http://localhost:8080/api"
    const val myRealm = "Core API Realm"

    fun getAlgorithm(): Algorithm = Algorithm.HMAC256(secret)
    fun getIssuer(): String = issuer

    /**
     * Generate a token for a given user ID and Role.
     */
    fun generateToken(userId: Int, role: String, tenantId: Int): String {
        return JWT.create()
            .withAudience(audience)
            .withIssuer(issuer)
            .withClaim("userId", userId)
            .withClaim("role", role)
            .withClaim("tenantId", tenantId)
            .withExpiresAt(Date(System.currentTimeMillis() + 60000000)) // ~16 hours
            .sign(getAlgorithm())
    }
}
