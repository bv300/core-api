package com.core.api

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import org.jetbrains.exposed.sql.transactions.transaction

import com.core.api.routes.authRoutes
import com.core.api.services.AuthService

fun Application.configureRouting() {
    val authService = AuthService()
    
    routing {
        authRoutes(authService)

        get("/") {
            call.respond(
                HttpStatusCode.OK,
                mapOf("status" to "ok")
            )
        }

        get("/api/health") {
            call.respond(
                HttpStatusCode.OK,
                mapOf(
                    "status" to "ok",
                    "service" to "core-api"
                )
            )
        }

        get("/api/db-health") {
            val result = transaction {
                exec("SELECT 1") { resultSet ->
                    resultSet.next()
                    resultSet.getInt(1)
                }
            }

            call.respond(
                HttpStatusCode.OK,
                mapOf(
                    "status" to "ok",
                    "database" to "connected",
                    "result" to result
                )
            )
        }

        authenticate("auth-jwt") {
            get("/api/protected") {
                val principal = call.principal<JWTPrincipal>()
                val userId = principal?.payload?.getClaim("userId")?.asInt()
                val role = principal?.payload?.getClaim("role")?.asString()
                call.respond(HttpStatusCode.OK, mapOf("message" to "Hello user $userId with role $role!"))
            }
        }
    }
}