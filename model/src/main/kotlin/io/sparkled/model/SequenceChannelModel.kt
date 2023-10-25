package io.sparkled.model

import io.micronaut.data.annotation.MappedEntity
import jakarta.persistence.Id
import java.time.Instant

@MappedEntity("SEQUENCE_CHANNEL")
data class SequenceChannelModel(
    @Id
    override var id: String,
    override var createdAt: Instant = Instant.now(),
    override var updatedAt: Instant = Instant.now(),

    var sequenceId: String,
    var stagePropId: String,

    var name: String,
    var displayOrder: Int,
    var channelJson: String,
) : Model
