package io.sparkled.e2e.util.query

import com.fasterxml.jackson.databind.ObjectMapper
import io.sparkled.persistence.DbQuery
import org.jdbi.v3.core.Jdbi

class ResetDatabaseE2eQuery : DbQuery<Unit> {
    override fun execute(jdbi: Jdbi, objectMapper: ObjectMapper) {
        jdbi.perform { handle ->
            handle.createUpdate(query).execute()
        }
    }

    private companion object {
        @Suppress("SqlWithoutWhere")
        val query = """
            DELETE FROM scheduled_job;
            DELETE FROM setting;
            DELETE FROM playlist_sequence;
            DELETE FROM sequence_channel;
            DELETE FROM sequence;
            DELETE FROM stage_prop;
            DELETE FROM stage;
            DELETE FROM song_audio;
            DELETE FROM song;
            DELETE FROM playlist;
        """.trimIndent()
    }
}
