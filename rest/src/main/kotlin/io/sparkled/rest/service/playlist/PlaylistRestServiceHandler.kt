package io.sparkled.rest.service.playlist

import io.sparkled.persistence.playlist.PlaylistPersistenceService
import io.sparkled.persistence.transaction.Transaction
import io.sparkled.rest.response.IdResponse
import io.sparkled.rest.service.RestServiceHandler
import io.sparkled.viewmodel.playlist.PlaylistViewModel
import io.sparkled.viewmodel.playlist.PlaylistViewModelConverter
import io.sparkled.viewmodel.playlist.search.PlaylistSearchViewModelConverter
import io.sparkled.viewmodel.playlist.sequence.PlaylistSequenceViewModelConverter
import javax.inject.Inject
import javax.inject.Provider
import javax.persistence.EntityManager
import javax.ws.rs.core.Response

open class PlaylistRestServiceHandler @Inject
constructor(private val entityManagerProvider: Provider<EntityManager>,
            private val playlistPersistenceService: PlaylistPersistenceService,
            private val playlistViewModelConverter: PlaylistViewModelConverter,
            private val playlistSearchViewModelConverter: PlaylistSearchViewModelConverter,
            private val playlistSequenceViewModelConverter: PlaylistSequenceViewModelConverter) : RestServiceHandler() {

    internal fun createPlaylist(playlistViewModel: PlaylistViewModel): Response {
        return Transaction(entityManagerProvider).of {
            var playlist = playlistViewModelConverter.toModel(playlistViewModel)
            playlist = playlistPersistenceService.createPlaylist(playlist)
            return@of respondOk(IdResponse(playlist.getId()!!))
        }
    }

    internal fun getAllPlaylists(): Response {
        val playlists = playlistPersistenceService.getAllPlaylists()
        val results = playlistSearchViewModelConverter.toViewModels(playlists)
        return respondOk(results)
    }

    internal fun getPlaylist(playlistId: Int): Response {
        val playlistOptional = playlistPersistenceService.getPlaylistById(playlistId)

        if (playlistOptional.isPresent) {
            val playlist = playlistOptional.get()
            val viewModel = playlistViewModelConverter.toViewModel(playlist)

            val playlistSequences = playlistPersistenceService
                    .getPlaylistSequencesByPlaylistId(playlistId)
                    .asSequence()
                    .map(playlistSequenceViewModelConverter::toViewModel)
                    .toList()

            viewModel.setSequences(playlistSequences)
            return respondOk(viewModel)
        }

        return respond(Response.Status.NOT_FOUND, "Playlist not found.")
    }

    internal fun updatePlaylist(id: Int, playlistViewModel: PlaylistViewModel): Response {
        return Transaction(entityManagerProvider).of {
            playlistViewModel.setId(id) // Prevent client-side ID tampering.

            val playlist = playlistViewModelConverter.toModel(playlistViewModel)
            val playlistSequences = playlistViewModel.getSequences()
                    .asSequence()
                    .map(playlistSequenceViewModelConverter::toModel)
                    .map { it.setPlaylistId(id) }
                    .toList()

            playlistPersistenceService.savePlaylist(playlist, playlistSequences)
            return@of respondOk()
        }
    }

    internal fun deletePlaylist(id: Int): Response {
        return Transaction(entityManagerProvider).of {
            playlistPersistenceService.deletePlaylist(id)
            return@of respondOk()
        }
    }
}