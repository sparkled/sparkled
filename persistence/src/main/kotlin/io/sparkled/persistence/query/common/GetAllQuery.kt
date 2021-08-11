package io.sparkled.persistence.query.common

import com.fasterxml.jackson.databind.ObjectMapper
import io.sparkled.model.annotation.Entity
import io.sparkled.persistence.DbQuery
import org.jdbi.v3.core.Jdbi
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation

class GetAllQuery<T : Any>(
    private val entity: KClass<T>,
    private val orderBy: String? = null,
    private val desc: Boolean = false
) : DbQuery<List<T>> {

    override fun execute(jdbi: Jdbi, objectMapper: ObjectMapper): List<T> {
        return jdbi.perform { handle ->
            val tableName = entity.findAnnotation<Entity>()!!.name

            val query = if (orderBy == null) "SELECT * FROM $tableName" else {
                val sort = if (desc) "DESC" else "ASC"
                "SELECT * FROM $tableName ORDER BY $orderBy $sort"
            }

            handle.createQuery(query)
                .mapTo(entity.java)
                .list()
        }
    }
}
