package com.core.api.services

import com.core.api.dto.AuthResponse
import com.core.api.dto.LoginRequest
import com.core.api.dto.RegisterRequest
import com.core.api.models.Tenant
import com.core.api.models.Tenants
import com.core.api.models.User
import com.core.api.models.Users
import com.core.api.security.JwtConfig
import org.jetbrains.exposed.sql.transactions.transaction
import org.mindrot.jbcrypt.BCrypt

class AuthService {
    fun register(request: RegisterRequest): AuthResponse {
        return transaction {
            // Check if user already exists
            val existingUser = User.find { Users.email eq request.email }.singleOrNull()
            if (existingUser != null) {
                throw IllegalArgumentException("User with this email already exists")
            }

            // For simplicity, we create a new tenant for every new registration,
            // or we could check if a tenant exists by name. Let's create a new one.
            val tenant = Tenant.new {
                name = request.tenantName
            }

            val hashedPassword = BCrypt.hashpw(request.password, BCrypt.gensalt())

            val user = User.new {
                this.tenant = tenant
                this.email = request.email
                this.passwordHash = hashedPassword
                this.role = request.role
            }

            val token = JwtConfig.generateToken(user.id.value, user.role, tenant.id.value)

            AuthResponse(
                token = token,
                userId = user.id.value,
                role = user.role,
                tenantId = tenant.id.value
            )
        }
    }

    fun login(request: LoginRequest): AuthResponse {
        return transaction {
            val user = User.find { Users.email eq request.email }.singleOrNull()
                ?: throw IllegalArgumentException("Invalid email or password")

            if (!BCrypt.checkpw(request.password, user.passwordHash)) {
                throw IllegalArgumentException("Invalid email or password")
            }

            val token = JwtConfig.generateToken(user.id.value, user.role, user.tenant.id.value)

            AuthResponse(
                token = token,
                userId = user.id.value,
                role = user.role,
                tenantId = user.tenant.id.value
            )
        }
    }
}
