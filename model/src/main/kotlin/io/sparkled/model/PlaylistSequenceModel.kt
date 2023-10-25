package io.sparkled.model

import io.micronaut.data.annotation.MappedEntity
import jakarta.persistence.Id
import java.time.Instant
import java.util.UUID

@MappedEntity("PLAYLIST_SEQUENCE")
data class PlaylistSequenceModel(
    @Id
    override var id: String,
    override var createdAt: Instant = Instant.now(),
    override var updatedAt: Instant = Instant.now(),

    var playlistId: String,
    var sequenceId: String,

    var displayOrder: Int,
) : Model
