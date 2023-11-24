package io.sparkled.persistence.repository

import io.micronaut.data.annotation.Query
import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.repository.CrudRepository
import io.sparkled.model.SongModel
import io.sparkled.model.UniqueId

@JdbcRepository(dialect = Dialect.ANSI)
abstract class SongRepository : CrudRepository<SongModel, String> {

    @Query(
        """
        SELECT s.*
            FROM SONG s
            JOIN SEQUENCE sq on s.id = sq.stage_id
            WHERE sq.id = :sequenceId
    """
    )
    abstract fun findBySequenceId(sequenceId: UniqueId): SongModel?
}
