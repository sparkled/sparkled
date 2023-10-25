package io.sparkled.persistence.repository

import io.micronaut.data.annotation.Query
import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.repository.CrudRepository
import io.sparkled.model.PlaylistSequenceModel
import io.sparkled.model.UniqueId

@JdbcRepository(dialect = Dialect.ANSI)
abstract class PlaylistSequenceRepository : CrudRepository<PlaylistSequenceModel, String> {
    @Query(
        """
        SELECT ps.*
        FROM PLAYLIST_SEQUENCE ps
        JOIN PLAYLIST p ON ps.playlist_id = p.id
        WHERE ps.playlist_id = :playlistId
        ORDER BY ps.display_order
    """
    )
    abstract fun getPlaylistSequencesByPlaylistId(playlistId: UniqueId): List<PlaylistSequenceModel>
}
