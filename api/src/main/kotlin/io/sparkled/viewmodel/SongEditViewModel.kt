package io.sparkled.viewmodel

import io.sparkled.model.annotation.GenerateClientType

@GenerateClientType
data class SongEditViewModel(
    val name: String,
    val artist: String?,
    val durationMs: Int,
) : ViewModel
