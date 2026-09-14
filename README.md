# Core API

Backend API for a Laptop EMI & Device Management Platform.

## Project Overview

Core API is the central backend system connecting:

- Super Admin Dashboard
- Shop Admin Dashboard
- Laptop / Device Management
- Windows Agent
- Customer Management
- EMI Management
- Payment Management
- License Management
- Subscription Management

The backend is designed as a multi-tenant REST API.

---

# Business Model

```text
                    CORE API
                       │
        ┌──────────────┼──────────────┐
        │              │              │
        ▼              ▼              ▼
   Super Admin     Shop Admin    Windows Agent
        │              │              │
        │              ▼              ▼
        │          Customers       Laptops
        │              │              │
        │              ▼              │
        │             EMI             │
        │              │              │
        │              ▼              │
        │           Payments          │
        │                             │
        └─────────────┬───────────────┘
                      ▼
                 PostgreSQL
```

---

## Technology Stack

- Kotlin
- Ktor
- Gradle
- PostgreSQL 18
- Exposed ORM
- Flyway
- JWT Authentication
- Valkey
- REST API

---

# Project Setup

## 1. Requirements

Make sure the following are installed:

- JDK 21+
- PostgreSQL 18
- pgAdmin 4
- VS Code
- Git

The project uses the Kotlin JVM toolchain:

```kotlin
kotlin {
    jvmToolchain(21)
}
```

## 2. Database Configuration & Migrations (Step 1)

The project connects to PostgreSQL using HikariCP for connection pooling and Exposed as the ORM. Flyway is used to manage database migrations automatically on application startup.

### Setting up the Database

1. Create a `.env` file in the root directory based on the `.env.example` (or use the one already provided):
   ```env
   DB_HOST=localhost
   DB_PORT=5432
   DB_NAME=core_api
   DB_USER=postgres
   DB_PASSWORD=your_password
   ```

2. Make sure your local PostgreSQL server is running and the database `core_api` exists.

3. The initial schema migration is located at `src/main/resources/db/migration/V1__create_initial_schema.sql`, which creates the base `tenants` and `users` tables.

---

## 3. Authentication (Step 2)

The API uses JWT (JSON Web Tokens) for authenticating users. 

### Configuration
JWT secrets and issuer details can be defined in your `.env` file:
```env
JWT_SECRET=your_super_secret_key
JWT_ISSUER=http://localhost:8080
JWT_AUDIENCE=http://localhost:8080/api
```

- **Token Generation**: Handled by the `JwtConfig.generateToken` utility. Tokens encode the `userId`, `role`, and `tenantId`.
- **Protected Routes**: Wrap any routes that require authentication in an `authenticate("auth-jwt") { ... }` block in your routing configuration.
