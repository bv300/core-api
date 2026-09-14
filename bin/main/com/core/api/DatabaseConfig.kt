package com.core.api

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.github.cdimascio.dotenv.dotenv
import org.jetbrains.exposed.sql.Database

object DatabaseConfig {
    fun connect() {
        val env = dotenv()
        val config = HikariConfig().apply {
            jdbcUrl = "jdbc:postgresql://${env["DB_HOST"]}:${env["DB_PORT"]}/${env["DB_NAME"]}"
            driverClassName = "org.postgresql.Driver"
            username = env["DB_USER"]
            password = env["DB_PASSWORD"]
            maximumPoolSize = 10
            isAutoCommit = false
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"
            validate()
        }
        val dataSource = HikariDataSource(config)
        Database.connect(dataSource)
    }
}