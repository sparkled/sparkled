package io.sparkled.viewmodel.playlist.sequence;

import io.sparkled.model.entity.PlaylistSequence;
import io.sparkled.viewmodel.ModelConverter;
import io.sparkled.viewmodel.ViewModelConverter;

public abstract class PlaylistSequenceViewModelConverter implements ModelConverter<PlaylistSequence, PlaylistSequenceViewModel>,
        ViewModelConverter<PlaylistSequenceViewModel, PlaylistSequence> {
}
