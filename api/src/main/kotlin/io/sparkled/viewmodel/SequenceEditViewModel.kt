package io.sparkled.viewmodel

import io.sparkled.model.UniqueId
import io.sparkled.model.annotation.GenerateClientType
import io.sparkled.model.enumeration.SequenceStatus

@GenerateClientType
data class SequenceEditViewModel(
    val songId: UniqueId,
    val stageId: UniqueId,
    val name: String,
    val framesPerSecond: Int,
    val frameCount: Int = 0,
    val status: SequenceStatus = SequenceStatus.NEW,
    val channels: List<SequenceChannelViewModel> = emptyList(),
) : ViewModel
