package io.sparkled.persistence.query.sequence

import com.fasterxml.jackson.databind.ObjectMapper
import io.sparkled.model.entity.v2.SequenceEntity
import io.sparkled.persistence.DbQuery
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.mapTo

class GetSequencesByPlaylistIdQuery(
    private val playlistId: Int
) : DbQuery<List<SequenceEntity>> {

    override fun execute(jdbi: Jdbi, objectMapper: ObjectMapper): List<SequenceEntity> {
        return jdbi.perform { handle ->
            handle.createQuery(query)
                .bind("playlistId", playlistId)
                .mapTo<SequenceEntity>()
                .toList()
        }
    }

    companion object {
        private val query = """
            SELECT *
            FROM sequence s
            JOIN playlist_sequence ps ON ps.sequence_id = s.id
            WHERE ps.playlist_id = :playlistId
            ORDER BY ps.display_order
        """.trimIndent()
    }
}
