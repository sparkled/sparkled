package io.sparkled.persistence.query.stage

import com.fasterxml.jackson.databind.ObjectMapper
import io.sparkled.persistence.DbQuery
import org.jdbi.v3.core.Jdbi
import org.slf4j.LoggerFactory

class DeleteRenderedStagePropsQuery(
    private val renderedStagePropIds: Collection<Int>
) : DbQuery<Unit> {

    override fun execute(jdbi: Jdbi, objectMapper: ObjectMapper) {
        jdbi.perform { handle ->
            val rowsDeleted = handle
                .createUpdate(query)
                .bindList("ids", renderedStagePropIds)
                .execute()

            logger.info("Deleted $rowsDeleted rendered stage prop(s).")
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(DeleteRenderedStagePropsQuery::class.java)
        private const val query = "DELETE FROM rendered_stage_prop WHERE id IN (<ids>)"
    }
}
