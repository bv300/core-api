package com.core.api

import io.github.cdimascio.dotenv.dotenv
import org.flywaydb.core.Flyway

object FlywayConfig {

    fun migrate() {

        val env = dotenv()

        Flyway.configure()
            .dataSource(
                "jdbc:postgresql://${env["DB_HOST"]}:${env["DB_PORT"]}/${env["DB_NAME"]}",
                env["DB_USER"],
                env["DB_PASSWORD"]
            )
            .locations("classpath:db/migration")
            .load()
            .migrate()
    }
}