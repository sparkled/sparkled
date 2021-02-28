package io.sparkled.persistence.v2.query.stage

import com.fasterxml.jackson.databind.ObjectMapper
import io.sparkled.model.entity.v2.StagePropEntity
import io.sparkled.persistence.DbQuery
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.mapTo

class GetStagePropsByStageIdQuery(
    private val stageId: Int
) : DbQuery<List<StagePropEntity>> {

    override fun execute(jdbi: Jdbi, objectMapper: ObjectMapper): List<StagePropEntity> {
        return jdbi.perform { handle ->
            handle.createQuery(query)
                .bind("stageId", stageId)
                .mapTo<StagePropEntity>()
                .list()
        }
    }

    companion object {
        private const val query = "SELECT * FROM stage_prop WHERE stage_id = :stageId ORDER BY display_order ASC"
    }
}

