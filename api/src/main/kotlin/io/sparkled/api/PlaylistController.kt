package io.sparkled.api

import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Delete
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.Put
import io.micronaut.scheduling.TaskExecutors
import io.micronaut.scheduling.annotation.ExecuteOn
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import io.sparkled.model.PlaylistModel
import io.sparkled.model.UniqueId
import io.sparkled.model.util.IdUtils.uniqueId
import io.sparkled.persistence.DbService
import io.sparkled.persistence.repository.findByIdOrNull
import io.sparkled.viewmodel.PlaylistSummaryViewModel
import io.sparkled.viewmodel.PlaylistViewModel
import jakarta.transaction.Transactional

@ExecuteOn(TaskExecutors.IO)
@Secured(SecurityRule.IS_ANONYMOUS)
@Controller("/api/playlists")
class PlaylistController(
    private val db: DbService,
) {

    @Get("/")
    @Transactional
    fun getAllPlaylists(): HttpResponse<Any> {
        val playlists = db.playlists.findAll().sortedBy { it.name }
        val playlistSequences = db.playlistSequences.findAll().groupBy { it.playlistId }
        val sequences = db.sequences.findAll().toList()
        val songs = db.songs.findAll().toList()

        val playlistSummaries = playlists.map {
            PlaylistSummaryViewModel.fromModel(
                it,
                playlistSequences[it.id] ?: emptyList(),
                sequences,
                songs,
            )
        }

        return HttpResponse.ok(playlistSummaries)
    }

    @Get("/{id}")
    @Transactional
    fun getPlaylist(id: UniqueId): HttpResponse<Any> {
        val playlist = getPlaylistById(id)

        return if (playlist != null) {
            HttpResponse.ok(playlist)
        } else HttpResponse.notFound("Playlist not found.")
    }

    @Post("/")
    @Transactional
    fun createPlaylist(
        @Body body: PlaylistViewModel,
    ): HttpResponse<Any> {
        val playlist = PlaylistModel(id = uniqueId(), name = body.name)
        db.playlists.save(playlist)

        val playlistSequences = body.sequences.map { it.toModel(playlistId = playlist.id) }
        db.playlistSequences.saveAll(playlistSequences)

        return HttpResponse.created(getPlaylistById(playlist.id))
    }

    private fun getPlaylistById(id: String): PlaylistViewModel? {
        val playlist = db.playlists.findByIdOrNull(id)
        val playlistSequences = db.playlistSequences.getPlaylistSequencesByPlaylistId(id)
        return playlist?.let { PlaylistViewModel.fromModel(it, playlistSequences) }
    }

    @Put("/{id}")
    @Transactional
    fun updatePlaylist(id: UniqueId, playlistViewModel: PlaylistViewModel): HttpResponse<Any> {
        val playlistAndSequences = playlistViewModel.copy(id = id).toModel()
        val playlist = playlistAndSequences.first.copy(id = id)

        // Update playlist.
        db.playlists.update(playlist)

        val existingSequences = db.playlistSequences.getPlaylistSequencesByPlaylistId(id).associateBy { it.id }
        val newSequences = playlistAndSequences.second.map { it.copy(playlistId = id) }.associateBy { it.id }

        // Delete playlist sequences that no longer exist.
        (existingSequences.keys - newSequences.keys).forEach { db.playlistSequences.delete(existingSequences.getValue(it)) }

        // Insert playlist sequences that didn't exist previously.
        (newSequences.keys - existingSequences.keys).forEach { db.playlistSequences.save(newSequences.getValue(it)) }

        // Update playlist sequences that exist
        (newSequences.keys.intersect(existingSequences.keys)).forEach {
            db.playlistSequences.update(
                newSequences.getValue(
                    it
                )
            )
        }

        return HttpResponse.ok()
    }

    @Delete("/{id}")
    @Transactional
    fun deletePlaylist(id: UniqueId): HttpResponse<Any> {
        db.playlists.deleteById(id)
        return HttpResponse.noContent()
    }
}
