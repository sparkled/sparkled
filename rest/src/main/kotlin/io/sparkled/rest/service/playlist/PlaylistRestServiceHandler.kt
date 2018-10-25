package io.sparkled.rest.service.playlist

import com.google.inject.persist.Transactional
import io.sparkled.model.entity.Playlist
import io.sparkled.model.entity.PlaylistSequence
import io.sparkled.persistence.playlist.PlaylistPersistenceService
import io.sparkled.rest.response.IdResponse
import io.sparkled.rest.service.RestServiceHandler
import io.sparkled.viewmodel.playlist.PlaylistViewModel
import io.sparkled.viewmodel.playlist.PlaylistViewModelConverter
import io.sparkled.viewmodel.playlist.search.PlaylistSearchViewModel
import io.sparkled.viewmodel.playlist.search.PlaylistSearchViewModelConverter
import io.sparkled.viewmodel.playlist.sequence.PlaylistSequenceViewModel
import io.sparkled.viewmodel.playlist.sequence.PlaylistSequenceViewModelConverter

import javax.inject.Inject
import javax.ws.rs.core.Response
import java.util.Optional

import java.util.stream.Collectors.toList

class PlaylistRestServiceHandler @Inject
constructor(private val playlistPersistenceService: PlaylistPersistenceService,
            private val playlistViewModelConverter: PlaylistViewModelConverter,
            private val playlistSearchViewModelConverter: PlaylistSearchViewModelConverter,
            private val playlistSequenceViewModelConverter: PlaylistSequenceViewModelConverter) : RestServiceHandler() {

    @Transactional
    internal fun createPlaylist(playlistViewModel: PlaylistViewModel): Response {
        var playlist = playlistViewModelConverter.toModel(playlistViewModel)
        playlist = playlistPersistenceService.createPlaylist(playlist)
        return respondOk(IdResponse(playlist.getId()))
    }

    internal val allPlaylists: Response
        get() {
            val playlists = playlistPersistenceService.getAllPlaylists()
            val results = playlistSearchViewModelConverter.toViewModels(playlists)

            return respondOk(results)
        }

    internal fun getPlaylist(playlistId: Int): Response {
        val playlistOptional = playlistPersistenceService.getPlaylistById(playlistId)

        if (playlistOptional.isPresent()) {
            val playlist = playlistOptional.get()
            val viewModel = playlistViewModelConverter.toViewModel(playlist)

            val playlistSequences = playlistPersistenceService
                    .getPlaylistSequencesByPlaylistId(playlistId)
                    .stream()
                    .map(???({ playlistSequenceViewModelConverter.toViewModel() }))
            .collect(toList())
            viewModel.setSequences(playlistSequences)

            return respondOk(viewModel)
        }

        return respond(Response.Status.NOT_FOUND, "Playlist not found.")
    }

    @Transactional
    internal fun updatePlaylist(id: Int, playlistViewModel: PlaylistViewModel): Response {
        playlistViewModel.setId(id) // Prevent client-side ID tampering.

        val playlist = playlistViewModelConverter.toModel(playlistViewModel)
        val playlistSequences = playlistViewModel.getSequences()
                .stream()
                .map(???({ playlistSequenceViewModelConverter.toModel() }))
        .map { ps -> ps.setPlaylistId(id) }
                .collect(toList())

        playlistPersistenceService.savePlaylist(playlist, playlistSequences)
        return respondOk()
    }

    @Transactional
    internal fun deletePlaylist(id: Int): Response {
        playlistPersistenceService.deletePlaylist(id)
        return respondOk()
    }
}