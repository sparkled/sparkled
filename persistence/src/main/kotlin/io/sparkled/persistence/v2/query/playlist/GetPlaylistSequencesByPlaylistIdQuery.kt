package io.sparkled.persistence.v2.query.playlist

import com.fasterxml.jackson.databind.ObjectMapper
import io.sparkled.model.entity.v2.PlaylistSequenceEntity
import io.sparkled.persistence.DbQuery
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.mapTo

class GetPlaylistSequencesByPlaylistIdQuery(
    private val playlistId: Int
) : DbQuery<List<PlaylistSequenceEntity>> {

    override fun execute(jdbi: Jdbi, objectMapper: ObjectMapper): List<PlaylistSequenceEntity> {
        return jdbi.perform { handle ->
            handle.createQuery(query)
                .bind("playlistId", playlistId)
                .mapTo<PlaylistSequenceEntity>()
                .toList()
        }
    }

    companion object {
        private val query = """
            SELECT ps.*
            FROM playlist_sequence ps
            JOIN playlist p ON ps.playlist_id = p.id
            WHERE ps.playlist_id = :playlistId
            ORDER BY ps.display_order
        """.trimIndent()
    }
}
