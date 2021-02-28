package io.sparkled.persistence.v2.query.sequence

import com.fasterxml.jackson.databind.ObjectMapper
import io.sparkled.model.entity.v2.SequenceChannelEntity
import io.sparkled.persistence.DbQuery
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.mapTo

class GetSequenceChannelsBySequenceIdQuery(
    private val sequenceId: Int
) : DbQuery<List<SequenceChannelEntity>> {

    override fun execute(jdbi: Jdbi, objectMapper: ObjectMapper): List<SequenceChannelEntity> {
        return jdbi.perform { handle ->
            handle.createQuery(query)
                .bind("sequenceId", sequenceId)
                .mapTo<SequenceChannelEntity>()
                .toList()
        }
    }

    companion object {
        private val query = """
            SELECT *
            FROM sequence_channel
            WHERE sequence_id = :sequenceId
            ORDER BY display_order
        """.trimIndent()
    }
}
