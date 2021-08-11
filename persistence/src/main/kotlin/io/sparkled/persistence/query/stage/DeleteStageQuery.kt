package io.sparkled.persistence.query.stage

import com.fasterxml.jackson.databind.ObjectMapper
import io.sparkled.persistence.DbQuery
import io.sparkled.persistence.query.sequence.DeleteSequencesQuery
import org.jdbi.v3.core.Jdbi
import org.slf4j.LoggerFactory
import java.util.UUID

class DeleteStageQuery(
    private val stageId: Int
) : DbQuery<Unit> {

    override fun execute(jdbi: Jdbi, objectMapper: ObjectMapper) {
        jdbi.perform { handle ->
            val sequenceIds = handle.createQuery(getSequenceIdsQuery)
                .bind("stageId", stageId)
                .map { rs, _ -> rs.getInt("id") }
                .list()
            DeleteSequencesQuery(sequenceIds).execute(jdbi, objectMapper)

            val stagePropUuids = handle.createQuery(getStagePropUuidsQuery)
                .bind("stageId", stageId)
                .map { rs, _ -> UUID.fromString(rs.getString("uuid")) }
                .list()
            DeleteStagePropsQuery(stagePropUuids).execute(jdbi, objectMapper)

            handle
                .createUpdate(deleteStageQuery)
                .bind("stageId", stageId)
                .execute()

            logger.info("Deleted stage.")
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(DeleteSequencesQuery::class.java)

        private const val getSequenceIdsQuery = "SELECT id FROM sequence WHERE stage_id = :stageId"
        private const val getStagePropUuidsQuery = "SELECT uuid FROM stage_prop WHERE stage_id = :stageId"
        private const val deleteStageQuery = "DELETE FROM stage WHERE id = :stageId"
    }
}
