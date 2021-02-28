package io.sparkled.persistence.v2.query.common

import com.fasterxml.jackson.databind.ObjectMapper
import io.sparkled.model.annotation.Entity
import io.sparkled.model.entity.v2.SparkledEntity
import io.sparkled.persistence.DbQuery
import io.sparkled.persistence.v2.util.toSnakeCase
import org.jdbi.v3.core.Jdbi
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties

class DeleteQuery<T : SparkledEntity>(
    private val entity: T
) : DbQuery<Unit> {

    override fun execute(jdbi: Jdbi, objectMapper: ObjectMapper) {
        val entityAnnotation = entity::class.findAnnotation<Entity>()!!
        val idColumn = toSnakeCase(entityAnnotation.idField)
        val idProperty = entity::class.memberProperties.find { it.name == entityAnnotation.idField }!!

        val query = "DELETE FROM ${entityAnnotation.name} WHERE $idColumn = :$idColumn"

        jdbi.perform { handle ->
            handle.createUpdate(query).bind(idColumn, idProperty.getter.call(entity)).execute()
        }
    }
}
