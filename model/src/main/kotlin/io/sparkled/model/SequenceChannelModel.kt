package io.sparkled.model

import io.micronaut.data.annotation.MappedEntity
import io.micronaut.data.annotation.MappedProperty
import io.sparkled.model.converter.InstantConverter
import io.sparkled.model.embedded.ChannelData
import io.sparkled.model.util.IdUtils.uniqueId
import jakarta.persistence.Id
import java.time.Instant

@MappedEntity("SEQUENCE_CHANNEL")
data class SequenceChannelModel(
    @Id
    override var id: UniqueId = uniqueId(),

    @MappedProperty(converter = InstantConverter::class)
    override var createdAt: Instant = Instant.now(),

    @MappedProperty(converter = InstantConverter::class)
    override var updatedAt: Instant = Instant.now(),

    var sequenceId: UniqueId,
    var stagePropId: UniqueId,

    var name: String,
    var displayOrder: Int,
    var channelData: ChannelData = ChannelData.empty,
) : Model
