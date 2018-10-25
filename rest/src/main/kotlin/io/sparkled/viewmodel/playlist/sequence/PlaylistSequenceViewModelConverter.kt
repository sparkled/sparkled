package io.sparkled.viewmodel.playlist.sequence

import io.sparkled.model.entity.PlaylistSequence
import io.sparkled.viewmodel.ModelConverter
import io.sparkled.viewmodel.ViewModelConverter

abstract class PlaylistSequenceViewModelConverter : ModelConverter<PlaylistSequence, PlaylistSequenceViewModel>, ViewModelConverter<PlaylistSequenceViewModel, PlaylistSequence>
