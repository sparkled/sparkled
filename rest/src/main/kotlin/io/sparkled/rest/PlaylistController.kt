package io.sparkled.rest

import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Delete
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.Put
import io.micronaut.spring.tx.annotation.Transactional
import io.sparkled.persistence.playlist.PlaylistPersistenceService
import io.sparkled.rest.response.IdResponse
import io.sparkled.viewmodel.playlist.PlaylistViewModel
import io.sparkled.viewmodel.playlist.PlaylistViewModelConverter
import io.sparkled.viewmodel.playlist.search.PlaylistSearchViewModelConverter
import io.sparkled.viewmodel.playlist.sequence.PlaylistSequenceViewModelConverter

@Controller("/api/playlists")
open class PlaylistController(
    private val playlistPersistenceService: PlaylistPersistenceService,
    private val playlistSearchViewModelConverter: PlaylistSearchViewModelConverter,
    private val playlistViewModelConverter: PlaylistViewModelConverter,
    private val playlistSequenceViewModelConverter: PlaylistSequenceViewModelConverter
) {

    @Get("/")
    @Transactional(readOnly = true)
    open fun getAllPlaylists(): HttpResponse<Any> {
        val playlists = playlistPersistenceService.getAllPlaylists()
        return HttpResponse.ok(playlistSearchViewModelConverter.toViewModels(playlists))
    }

    @Get("/{id}")
    @Transactional(readOnly = true)
    open fun getPlaylist(id: Int): HttpResponse<Any> {
        val playlist = playlistPersistenceService.getPlaylistById(id)

        if (playlist != null) {
            val viewModel = playlistViewModelConverter.toViewModel(playlist)

            val playlistSequences = playlistPersistenceService
                .getPlaylistSequencesByPlaylistId(id)
                .asSequence()
                .map(playlistSequenceViewModelConverter::toViewModel)
                .toList()
            viewModel.setSequences(playlistSequences)

            return HttpResponse.ok(viewModel)
        }

        return HttpResponse.notFound("Playlist not found.")
    }

    @Post("/")
    @Transactional
    open fun createPlaylist(playlistViewModel: PlaylistViewModel): HttpResponse<Any> {
        val playlist = playlistViewModelConverter.toModel(playlistViewModel)
        val savedPlaylist = playlistPersistenceService.createPlaylist(playlist)
        return HttpResponse.ok(IdResponse(savedPlaylist.getId()!!))
    }

    @Put("/{id}")
    @Transactional
    open fun updatePlaylist(id: Int, playlistViewModel: PlaylistViewModel): HttpResponse<Any> {
        playlistViewModel.setId(id) // Prevent client-side ID tampering.

        val playlist = playlistViewModelConverter.toModel(playlistViewModel)
        val playlistSequences = playlistViewModel.getSequences()
            .asSequence()
            .map(playlistSequenceViewModelConverter::toModel)
            .map { it.setPlaylistId(id) }
            .toList()

        playlistPersistenceService.savePlaylist(playlist, playlistSequences)
        return HttpResponse.ok()
    }

    @Delete("/{id}")
    @Transactional
    open fun deletePlaylist(id: Int): HttpResponse<Any> {
        playlistPersistenceService.deletePlaylist(id)
        return HttpResponse.ok()
    }
}
