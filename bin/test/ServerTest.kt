package com.core.api

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals

class ServerTest {

    @Test
    fun testHealthEndpoint() = testApplication {
        application {
            module()
        }

        val response = client.get("/api/health")

        println("STATUS: ${response.status}")
        println("BODY: ${response.bodyAsText()}")

        assertEquals(HttpStatusCode.OK, response.status)
    }
}