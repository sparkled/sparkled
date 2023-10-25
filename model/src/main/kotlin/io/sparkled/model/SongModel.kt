package io.sparkled.model

import io.micronaut.data.annotation.MappedEntity
import jakarta.persistence.Id
import java.time.Instant

@MappedEntity("SONG")
data class SongModel(
    @Id
    override var id: String,
    override var createdAt: Instant = Instant.now(),
    override var updatedAt: Instant = Instant.now(),

    var stageId: String,
    var songId: String,

    var name: String,
    var artist: String? = null,
    var durationMs: Int
) : Model
