package io.sparkled.viewmodel.song;

import io.sparkled.model.entity.Song;
import io.sparkled.viewmodel.ModelConverter;
import io.sparkled.viewmodel.ViewModelConverter;

public abstract class SongViewModelConverter implements ModelConverter<Song, SongViewModel>,
        ViewModelConverter<SongViewModel, Song> {
}
