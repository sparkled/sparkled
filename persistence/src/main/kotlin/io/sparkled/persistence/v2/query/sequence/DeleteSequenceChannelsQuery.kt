package io.sparkled.persistence.v2.query.sequence

import com.fasterxml.jackson.databind.ObjectMapper
import io.sparkled.persistence.DbQuery
import org.jdbi.v3.core.Jdbi
import org.slf4j.LoggerFactory
import java.util.*

class DeleteSequenceChannelsQuery(
    private val sequenceChannelUuids: Collection<UUID>
) : DbQuery<Unit> {

    override fun execute(jdbi: Jdbi, objectMapper: ObjectMapper) {
        if (sequenceChannelUuids.isEmpty()) {
            logger.info("No sequence channels to delete.")
        } else {
            jdbi.perform { handle ->
                val rowsDeleted = handle
                    .createUpdate(query)
                    .bindList("uuids", sequenceChannelUuids)
                    .execute()

                logger.info("Deleted $rowsDeleted sequence channel(s).")
            }
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(DeleteSequenceChannelsQuery::class.java)
        private const val query = "DELETE FROM sequence_channel WHERE uuid IN (<uuids>)"
    }
}
