package io.sparkled.persistence.repository

import io.micronaut.data.annotation.Query
import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.repository.CrudRepository
import io.sparkled.model.SequenceModel
import io.sparkled.model.UniqueId

@JdbcRepository(dialect = Dialect.SQL_SERVER)
abstract class SequenceRepository : CrudRepository<SequenceModel, String> {

    @Query("""
        SELECT s.*
        FROM SEQUENCE s
        JOIN PLAYLIST_SEQUENCE ps ON ps.sequence_id = s.id
        WHERE ps.playlist_id = :playlistId
        ORDER BY ps.display_order
    """)
    abstract fun findAllByPlaylistId(playlistId: UniqueId): List<SequenceModel>

    @Query("""
        DELETE FROM SEQUENCE_CHANNEL WHERE sequence_id = :id
        DELETE FROM PLAYLIST_SEQUENCE WHERE sequence_id = :id
        DELETE FROM SEQUENCE WHERE id = :id
    """)
    abstract fun deleteSequenceAndDependentsById(id: UniqueId)
}
