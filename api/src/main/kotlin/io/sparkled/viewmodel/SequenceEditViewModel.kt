package io.sparkled.viewmodel

import io.sparkled.model.enumeration.SequenceStatus

data class SequenceEditViewModel(
    val name: String,
    val status: SequenceStatus = SequenceStatus.NEW,
    val channels: List<SequenceChannelViewModel> = emptyList(),
) : ViewModel