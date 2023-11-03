package io.sparkled.model

import io.micronaut.data.annotation.MappedEntity
import io.sparkled.model.enumeration.ScheduledActionType
import io.sparkled.model.util.IdUtils.uniqueId
import jakarta.persistence.Id
import java.time.Instant

@MappedEntity("SCHEDULED_ACTION")
data class ScheduledActionModel(
    @Id
    override var id: UniqueId = uniqueId(),
    override var createdAt: Instant = Instant.now(),
    override var updatedAt: Instant = Instant.now(),

    var playlistId: UniqueId?,

    var type: ScheduledActionType,
    var cronExpression: String,
    var value: String?,
) : Model
