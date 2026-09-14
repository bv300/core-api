package com.core.api

import io.ktor.server.application.*
import io.ktor.server.netty.EngineMain
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*

import com.core.api.security.JwtConfig
import com.auth0.jwt.JWT
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*

fun main(args: Array<String>) {
    EngineMain.main(args)
}

fun Application.module() {
    install(ContentNegotiation) {
        json()
    }

    install(Authentication) {
        jwt("auth-jwt") {
            realm = JwtConfig.myRealm
            verifier(
                JWT
                    .require(JwtConfig.getAlgorithm())
                    .withAudience(JwtConfig.audience)
                    .withIssuer(JwtConfig.getIssuer())
                    .build()
            )
            validate { credential ->
                if (credential.payload.getClaim("userId").asInt() != null) {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }
            challenge { defaultScheme, realm ->
                call.respond(HttpStatusCode.Unauthorized, "Token is not valid or has expired")
            }
        }
    }

    FlywayConfig.migrate()

    DatabaseConfig.connect()

    configureRouting()
}