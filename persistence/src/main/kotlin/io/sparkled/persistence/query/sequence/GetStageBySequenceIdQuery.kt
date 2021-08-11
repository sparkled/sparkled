package io.sparkled.persistence.query.sequence

import com.fasterxml.jackson.databind.ObjectMapper
import io.sparkled.model.entity.v2.StageEntity
import io.sparkled.persistence.DbQuery
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.mapTo

class GetStageBySequenceIdQuery(
    private val sequenceId: Int
) : DbQuery<StageEntity?> {

    override fun execute(jdbi: Jdbi, objectMapper: ObjectMapper): StageEntity? {
        return jdbi.perform { handle ->
            handle.createQuery(query)
                .bind("sequenceId", sequenceId)
                .mapTo<StageEntity>()
                .findFirst().orElseGet { null }
        }
    }

    companion object {
        private val query = """
            SELECT st.*
            FROM stage st
            JOIN sequence s on st.id = s.stage_id
            WHERE s.id = :sequenceId
        """.trimIndent()
    }
}
