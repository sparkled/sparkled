package io.sparkled.viewmodel.playlist

import io.sparkled.model.entity.Playlist
import io.sparkled.persistence.playlist.PlaylistPersistenceService
import io.sparkled.viewmodel.exception.ViewModelConversionException

import javax.inject.Inject

class PlaylistViewModelConverterImpl @Inject
constructor(private val playlistPersistenceService: PlaylistPersistenceService) : PlaylistViewModelConverter() {

    @Override
    fun toViewModel(model: Playlist): PlaylistViewModel {
        return PlaylistViewModel()
                .setId(model.getId())
                .setName(model.getName())
    }

    @Override
    fun toModel(viewModel: PlaylistViewModel): Playlist {
        val playlistId = viewModel.getId()
        val model = getPlaylist(playlistId)

        return model.setName(viewModel.getName())
    }

    private fun getPlaylist(playlistId: Integer?): Playlist {
        if (playlistId == null) {
            return Playlist()
        }

        return playlistPersistenceService.getPlaylistById(playlistId)
                .orElseThrow({ ViewModelConversionException("Playlist with ID of '$playlistId' not found.") })
    }
}
