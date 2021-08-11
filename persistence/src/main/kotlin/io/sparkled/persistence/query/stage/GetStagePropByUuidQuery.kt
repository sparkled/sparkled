package io.sparkled.persistence.query.stage

import com.fasterxml.jackson.databind.ObjectMapper
import io.sparkled.model.entity.v2.StagePropEntity
import io.sparkled.persistence.DbQuery
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.mapTo
import java.util.UUID

class GetStagePropByUuidQuery(
    private val stageId: Int,
    private val uuid: UUID
) : DbQuery<StagePropEntity?> {

    override fun execute(jdbi: Jdbi, objectMapper: ObjectMapper): StagePropEntity? {
        return jdbi.perform { handle ->
            handle.createQuery(query)
                .bind("stageId", stageId)
                .bind("uuid", uuid)
                .mapTo<StagePropEntity>()
                .findFirst().orElseGet { null }
        }
    }

    companion object {
        private const val query = "SELECT * FROM stage_prop WHERE stage_id = :stageId AND uuid = :uuid"
    }
}
