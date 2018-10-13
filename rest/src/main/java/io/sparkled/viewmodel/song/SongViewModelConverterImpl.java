package io.sparkled.viewmodel.song;

import io.sparkled.model.entity.Song;
import io.sparkled.persistence.song.SongPersistenceService;
import io.sparkled.viewmodel.exception.ViewModelConversionException;

import javax.inject.Inject;

public class SongViewModelConverterImpl extends SongViewModelConverter {

    private SongPersistenceService songPersistenceService;

    @Inject
    public SongViewModelConverterImpl(SongPersistenceService songPersistenceService) {
        this.songPersistenceService = songPersistenceService;
    }

    @Override
    public SongViewModel toViewModel(Song model) {
        return new SongViewModel()
                .setId(model.getId())
                .setName(model.getName())
                .setArtist(model.getArtist())
                .setAlbum(model.getAlbum())
                .setDurationMs(model.getDurationMs());
    }

    @Override
    public Song toModel(SongViewModel viewModel) {
        final Integer songId = viewModel.getId();
        Song model = getSong(songId);

        return model
                .setId(viewModel.getId())
                .setName(viewModel.getName())
                .setArtist(viewModel.getArtist())
                .setAlbum(viewModel.getAlbum())
                .setDurationMs(viewModel.getDurationMs());
    }

    private Song getSong(Integer songId) {
        if (songId == null) {
            return new Song();
        }

        return songPersistenceService.getSongById(songId)
                .orElseThrow(() -> new ViewModelConversionException("Song with ID of '" + songId + "' not found."));
    }
}
