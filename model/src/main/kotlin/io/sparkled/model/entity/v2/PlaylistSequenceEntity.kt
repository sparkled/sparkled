package io.sparkled.model.entity.v2

import io.sparkled.model.annotation.Entity
import io.sparkled.model.util.IdUtils
import java.util.UUID

@Entity(name = "playlist_sequence", idField = "uuid")
data class PlaylistSequenceEntity(
    val uuid: UUID = IdUtils.newUuid(),
    val playlistId: Int,
    val sequenceId: Int,
    val displayOrder: Int,
) : SparkledEntity
