package io.sparkled.persistence.v2.query.stage

import com.fasterxml.jackson.databind.ObjectMapper
import io.sparkled.persistence.DbQuery
import org.jdbi.v3.core.Jdbi
import org.slf4j.LoggerFactory
import java.util.*

class DeleteStagePropsQuery(
    private val stagePropUuids: Collection<UUID>
) : DbQuery<Unit> {

    override fun execute(jdbi: Jdbi, objectMapper: ObjectMapper) {
        if (stagePropUuids.isEmpty()) {
            logger.info("No stage props to delete.")
        } else {
            jdbi.perform { handle ->
                val rowsDeleted = handle
                    .createUpdate(query)
                    .bindList("stagePropUuids", stagePropUuids)
                    .execute()

                logger.info("Deleted $rowsDeleted stage prop(s).")
            }
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(DeleteStagePropsQuery::class.java)
        private val query = """
            DELETE FROM stage_prop WHERE uuid IN (<stagePropUuids>);
        """.trimIndent()
    }
}
