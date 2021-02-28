package io.sparkled.persistence.v2.query.common

import com.fasterxml.jackson.databind.ObjectMapper
import io.sparkled.model.annotation.Entity
import io.sparkled.persistence.DbQuery
import io.sparkled.persistence.v2.util.toSnakeCase
import org.jdbi.v3.core.Jdbi
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation

class GetByIdQuery<T : Any>(
    private val entity: KClass<T>,
    private val id: Any
) : DbQuery<T?> {

    override fun execute(jdbi: Jdbi, objectMapper: ObjectMapper): T? {
        return jdbi.perform { handle ->

            val entityAnnotation = entity.findAnnotation<Entity>()!!
            handle.createQuery("""
                SELECT *
                FROM ${entityAnnotation.name}
                WHERE ${toSnakeCase(entityAnnotation.idField)} = :id
            """.trimIndent())
                .bind("id", id)
                .mapTo(entity.java)
                .findFirst().orElseGet { null }
        }
    }
}
