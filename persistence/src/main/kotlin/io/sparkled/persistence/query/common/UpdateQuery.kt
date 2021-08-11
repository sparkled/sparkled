package io.sparkled.persistence.query.common

import com.fasterxml.jackson.databind.ObjectMapper
import io.sparkled.model.annotation.Entity
import io.sparkled.model.entity.v2.SparkledEntity
import io.sparkled.persistence.DbQuery
import io.sparkled.persistence.util.toSnakeCase
import org.jdbi.v3.core.Jdbi
import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties

class UpdateQuery<T : SparkledEntity>(
    private val entity: T,
    vararg val fieldsToUpdate: String
) : DbQuery<Unit> {

    override fun execute(jdbi: Jdbi, objectMapper: ObjectMapper) {
        val properties = entity::class.memberProperties
        val entityAnnotation = entity::class.findAnnotation<Entity>()!!
        val idField = entityAnnotation.idField

        val query = queryCache.computeIfAbsent(entity::class) {
            val tableName = entityAnnotation.name
            val fields = properties.map { it.name to it.getter.call(entity) }
            val whitelist = fieldsToUpdate.toSet()

            val fieldUpdates = fields
                .filter { it.first != idField }
                .filter { whitelist.isEmpty() || whitelist.contains(it.first) }
                .joinToString(",") {
                    "${toSnakeCase(it.first)} = :${it.first}"
                }

            """
                UPDATE $tableName
                SET $fieldUpdates
                WHERE ${toSnakeCase(idField)} = :$idField
            """.trimIndent()
        }

        jdbi.perform { handle ->
            handle.createUpdate(query).apply {
                properties.forEach {
                    bind(it.name, it.getter.call(entity))
                }
            }.execute()
        }
    }

    companion object {
        private val queryCache = ConcurrentHashMap<KClass<out Any>, String>()
    }
}
