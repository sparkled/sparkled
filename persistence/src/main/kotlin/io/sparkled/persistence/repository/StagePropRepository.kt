package io.sparkled.persistence.repository

import io.micronaut.data.annotation.Query
import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.repository.CrudRepository
import io.sparkled.model.StagePropModel
import io.sparkled.model.UniqueId

@JdbcRepository(dialect = Dialect.SQL_SERVER)
abstract class StagePropRepository : CrudRepository<StagePropModel, String> {

    @Query("SELECT * FROM STAGE_PROP WHERE stage_id = :id ORDER BY display_order")
    abstract fun findAllByStageId(id: UniqueId): List<StagePropModel>
}
