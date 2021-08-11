package io.sparkled.persistence

import io.sparkled.model.entity.v2.SparkledEntity
import io.sparkled.model.util.IdUtils
import io.sparkled.persistence.query.common.DeleteQuery
import io.sparkled.persistence.query.common.GetByIdQuery
import io.sparkled.persistence.query.common.*

interface DbService {
    fun init()
    fun <T> query(query: DbQuery<T>): T
}

inline fun <reified T : Any> DbService.getAll(
    orderBy: String? = null,
    desc: Boolean = false
): List<T> {
    return query(GetAllQuery(T::class, orderBy, desc))
}

inline fun <reified T : Any> DbService.getById(id: Int?): T? {
    return query(GetByIdQuery(T::class, id ?: IdUtils.NO_ID))
}

inline fun <reified T : SparkledEntity> DbService.insert(entity: T): String {
    return query(InsertQuery(entity))
}

inline fun <reified T : SparkledEntity> DbService.update(entity: T, vararg fieldsToUpdate: String) {
    return query(UpdateQuery(entity, *fieldsToUpdate))
}

inline fun <reified T : SparkledEntity> DbService.delete(entity: T) {
    return query(DeleteQuery(entity))
}
