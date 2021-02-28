package io.sparkled.persistence.v2.query.song

import com.fasterxml.jackson.databind.ObjectMapper
import io.sparkled.persistence.DbQuery
import org.jdbi.v3.core.Jdbi
import org.slf4j.LoggerFactory

class DeleteSongsQuery(
    private val songIds: Collection<Int>
) : DbQuery<Unit> {

    override fun execute(jdbi: Jdbi, objectMapper: ObjectMapper) {
        if (songIds.isEmpty()) {
            logger.info("No songs to delete.")
        } else {
            jdbi.perform { handle ->
                val rowsDeleted = handle
                    .createUpdate(query)
                    .bindList("songIds", songIds)
                    .execute()

                logger.info("Deleted $rowsDeleted song(s).")
            }
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(DeleteSongsQuery::class.java)
        private val query = """
            DELETE FROM song WHERE id IN (<songIds>);
        """.trimIndent()
    }
}
