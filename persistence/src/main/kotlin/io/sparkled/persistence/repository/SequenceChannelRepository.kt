package io.sparkled.persistence.repository

import io.micronaut.data.annotation.Query
import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.repository.CrudRepository
import io.sparkled.model.SequenceChannelModel
import io.sparkled.model.UniqueId

@JdbcRepository(dialect = Dialect.SQL_SERVER)
abstract class SequenceChannelRepository : CrudRepository<SequenceChannelModel, String> {

    @Query("""
        SELECT *
        FROM SEQUENCE_CHANNEL
        WHERE sequence_id = :id
        ORDER BY display_order
    """)
    abstract fun findAllBySequenceId(id: UniqueId): List<SequenceChannelModel>
}
