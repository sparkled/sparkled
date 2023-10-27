package io.sparkled.model

import io.micronaut.data.annotation.MappedEntity
import io.sparkled.model.enumeration.SequenceStatus
import jakarta.persistence.Id
import java.time.Instant

@MappedEntity("SEQUENCE")
data class SequenceModel(
    @Id
    override var id: UniqueId = uniqueId(),
    override var createdAt: Instant = Instant.now(),
    override var updatedAt: Instant = Instant.now(),

    var stageId: UniqueId,
    var songId: UniqueId,

    var status: SequenceStatus,
    var name: String,
    var framesPerSecond: Int,
) : Model
