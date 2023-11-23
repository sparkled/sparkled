package io.sparkled.viewmodel

data class SongEditViewModel(
    val name: String,
    val artist: String?,
    val durationMs: Int,
) : ViewModel
