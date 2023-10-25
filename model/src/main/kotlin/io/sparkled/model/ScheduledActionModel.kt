package io.sparkled.model

import io.micronaut.data.annotation.MappedEntity
import io.sparkled.model.enumeration.ScheduledActionType
import jakarta.persistence.Id
import java.time.Instant

@MappedEntity("SCHEDULED_ACTION")
data class ScheduledActionModel(
    @Id
    override var id: String,
    override var createdAt: Instant = Instant.now(),
    override var updatedAt: Instant = Instant.now(),

    var playlistId: String?,

    var type: ScheduledActionType,
    var cronExpression: String,
    var value: String?,
) : Model
