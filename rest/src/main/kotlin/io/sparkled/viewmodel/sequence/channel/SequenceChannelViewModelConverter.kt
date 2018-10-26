package io.sparkled.viewmodel.sequence.channel

import io.sparkled.model.entity.SequenceChannel
import io.sparkled.viewmodel.ModelConverter
import io.sparkled.viewmodel.ViewModelConverter

abstract class SequenceChannelViewModelConverter : ModelConverter<SequenceChannel, SequenceChannelViewModel>, ViewModelConverter<SequenceChannelViewModel, SequenceChannel>
