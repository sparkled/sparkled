package io.sparkled.persistence.query.sequence

import com.fasterxml.jackson.databind.ObjectMapper
import io.sparkled.model.entity.v2.SequenceChannelEntity
import io.sparkled.persistence.DbQuery
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.mapTo
import java.util.UUID

class GetSequenceChannelByUuidQuery(
    private val sequenceId: Int,
    private val uuid: UUID
) : DbQuery<SequenceChannelEntity?> {

    override fun execute(jdbi: Jdbi, objectMapper: ObjectMapper): SequenceChannelEntity? {
        return jdbi.perform { handle ->
            handle.createQuery(query)
                .bind("sequenceId", sequenceId)
                .bind("uuid", uuid)
                .mapTo<SequenceChannelEntity>()
                .findFirst().orElseGet { null }
        }
    }

    companion object {
        private const val query = "SELECT * FROM sequence_channel WHERE sequence_id = :sequenceId AND uuid = :uuid"
    }
}
