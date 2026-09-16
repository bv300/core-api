package com.core.api.models

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.CurrentTimestamp
import org.jetbrains.exposed.sql.javatime.timestamp

object Tenants : IntIdTable("tenants") {
    val name = varchar("name", 255)
    val createdAt = timestamp("created_at").defaultExpression(CurrentTimestamp)
}

class Tenant(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Tenant>(Tenants)

    var name by Tenants.name
    var createdAt by Tenants.createdAt
}
