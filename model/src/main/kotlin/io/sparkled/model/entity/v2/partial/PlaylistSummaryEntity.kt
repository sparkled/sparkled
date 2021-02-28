package io.sparkled.model.entity.v2.partial

data class PlaylistSummaryEntity(
    val id: Int,
    val name: String,
    val sequenceCount: Int,
    val durationSeconds: Int
)
