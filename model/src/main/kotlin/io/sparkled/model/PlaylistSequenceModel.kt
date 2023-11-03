package io.sparkled.model

import io.micronaut.data.annotation.MappedEntity
import io.sparkled.model.util.IdUtils.uniqueId
import jakarta.persistence.Id
import java.time.Instant
import java.util.UUID

@MappedEntity("PLAYLIST_SEQUENCE")
data class PlaylistSequenceModel(
    @Id
    override var id: UniqueId = uniqueId(),
    override var createdAt: Instant = Instant.now(),
    override var updatedAt: Instant = Instant.now(),

    var playlistId: UniqueId,
    var sequenceId: UniqueId,

    var displayOrder: Int,
) : Model
