package io.sparkled.viewmodel

data class PlaylistEditViewModel(
    val name: String,
    val sequences: List<PlaylistSequenceViewModel> = emptyList()
) : ViewModel
