package io.sparkled.viewmodel.song

import io.sparkled.model.entity.Song
import io.sparkled.persistence.song.SongPersistenceService
import io.sparkled.viewmodel.exception.ViewModelConversionException

import javax.inject.Inject

class SongViewModelConverterImpl
@Inject constructor(private val songPersistenceService: SongPersistenceService) : SongViewModelConverter() {

    override fun toViewModel(model: Song): SongViewModel {
        return SongViewModel()
            .setId(model.getId())
            .setName(model.getName())
            .setArtist(model.getArtist())
            .setAlbum(model.getAlbum())
            .setDurationMs(model.getDurationMs())
    }

    override fun toModel(viewModel: SongViewModel): Song {
        val songId = viewModel.getId()
        val model = getSong(songId)

        return model
            .setId(viewModel.getId())
            .setName(viewModel.getName())
            .setArtist(viewModel.getArtist())
            .setAlbum(viewModel.getAlbum())
            .setDurationMs(viewModel.getDurationMs())
    }

    private fun getSong(songId: Int?): Song {
        if (songId == null) {
            return Song()
        }

        return songPersistenceService.getSongById(songId)
            ?: throw ViewModelConversionException("Song with ID of '$songId' not found.")
    }
}
