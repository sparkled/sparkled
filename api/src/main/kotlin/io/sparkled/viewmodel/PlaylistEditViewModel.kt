package io.sparkled.viewmodel

import io.sparkled.model.UniqueId
import io.sparkled.model.annotation.GenerateClientType

@GenerateClientType
data class PlaylistEditViewModel(
    val name: String,
    val insertions: List<PlaylistSequenceInsertionViewModel> = emptyList(),
    val deletions: List<UniqueId> = emptyList(),
) : ViewModel
