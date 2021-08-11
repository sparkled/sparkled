package io.sparkled.persistence.query.sequence

import com.fasterxml.jackson.databind.ObjectMapper
import io.sparkled.model.entity.v2.SongEntity
import io.sparkled.persistence.DbQuery
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.mapTo

class GetSongBySequenceIdQuery(
    private val sequenceId: Int
) : DbQuery<SongEntity?> {

    override fun execute(jdbi: Jdbi, objectMapper: ObjectMapper): SongEntity? {
        return jdbi.perform { handle ->
            handle.createQuery(query)
                .bind("sequenceId", sequenceId)
                .mapTo<SongEntity>()
                .findFirst().orElseGet { null }
        }
    }

    companion object {
        private val query = """
            SELECT s.*
            FROM song s
            JOIN sequence seq on seq.song_id = s.id
            WHERE seq.id = :sequenceId
        """.trimIndent()
    }
}
