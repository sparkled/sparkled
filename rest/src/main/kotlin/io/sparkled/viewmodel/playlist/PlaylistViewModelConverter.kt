package io.sparkled.viewmodel.playlist;

import io.sparkled.model.entity.Playlist;
import io.sparkled.viewmodel.ModelConverter;
import io.sparkled.viewmodel.ViewModelConverter;

public abstract class PlaylistViewModelConverter implements ModelConverter<Playlist, PlaylistViewModel>,
        ViewModelConverter<PlaylistViewModel, Playlist> {
}
