package io.sparkled.model

import io.micronaut.data.annotation.MappedEntity
import io.micronaut.data.annotation.MappedProperty
import io.sparkled.model.converter.InstantConverter
import io.sparkled.model.util.IdUtils.uniqueId
import jakarta.persistence.Id
import java.time.Instant

@MappedEntity("SONG")
data class SongModel(
    @Id
    override var id: UniqueId = uniqueId(),

    @MappedProperty(converter = InstantConverter::class)
    override var createdAt: Instant = Instant.now(),

    @MappedProperty(converter = InstantConverter::class)
    override var updatedAt: Instant = Instant.now(),

    var name: String,
    var artist: String? = null,
    var durationMs: Int,
) : Model
