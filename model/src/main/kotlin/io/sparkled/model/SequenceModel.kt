package io.sparkled.model

import io.micronaut.data.annotation.MappedEntity
import io.micronaut.data.annotation.MappedProperty
import io.sparkled.model.converter.InstantConverter
import io.sparkled.model.enumeration.SequenceStatus
import io.sparkled.model.util.IdUtils.uniqueId
import jakarta.persistence.Id
import java.time.Instant

@MappedEntity("SEQUENCE")
data class SequenceModel(
    @Id
    override var id: UniqueId = uniqueId(),

    @MappedProperty(converter = InstantConverter::class)
    override var createdAt: Instant = Instant.now(),

    @MappedProperty(converter = InstantConverter::class)
    override var updatedAt: Instant = Instant.now(),

    var stageId: UniqueId,
    var songId: UniqueId,

    var status: SequenceStatus,
    var name: String,
    var framesPerSecond: Int,
) : Model
