package io.sparkled.viewmodel

import io.sparkled.model.annotation.GenerateClientType

@GenerateClientType
data class PlaylistEditViewModel(
    val name: String,
    val sequences: List<PlaylistSequenceViewModel> = emptyList()
) : ViewModel
