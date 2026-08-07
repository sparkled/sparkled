package io.sparkled.api

import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Delete
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.Put
import io.micronaut.scheduling.TaskExecutors
import io.micronaut.scheduling.annotation.ExecuteOn
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import io.micronaut.transaction.annotation.Transactional
import io.sparkled.model.PlaylistModel
import io.sparkled.model.UniqueId
import io.sparkled.model.util.IdUtils.uniqueId
import io.sparkled.persistence.DbService
import io.sparkled.persistence.repository.findByIdOrNull
import io.sparkled.viewmodel.PlaylistEditViewModel
import io.sparkled.viewmodel.PlaylistSummaryViewModel
import io.sparkled.viewmodel.PlaylistViewModel
import io.sparkled.viewmodel.error.ApiErrorCode
import io.sparkled.viewmodel.exception.HttpResponseException
import java.time.Instant

@ExecuteOn(TaskExecutors.BLOCKING)
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
            ?: throw HttpResponseException(ApiErrorCode.ERR_NOT_FOUND)

        return HttpResponse.ok(playlist)
    }

    @Post("/")
    @Transactional
    fun createPlaylist(
        @Body body: PlaylistEditViewModel,
    ): HttpResponse<Any> {
        val playlist = PlaylistModel(id = uniqueId(), name = body.name)
        db.playlists.save(playlist)

        val playlistSequences = body.insertions.map { it.toModel(playlistId = playlist.id) }
        db.playlistSequences.saveAll(playlistSequences)

        return HttpResponse.created(getPlaylistById(playlist.id))
    }

    private fun getPlaylistById(id: UniqueId): PlaylistViewModel? {
        val playlist = db.playlists.findByIdOrNull(id)
        val playlistSequences = db.playlistSequences.getPlaylistSequencesByPlaylistId(id)
        return playlist?.let { PlaylistViewModel.fromModel(it, playlistSequences) }
    }

    @Put("/{id}")
    @Transactional
    fun updatePlaylist(
        @PathVariable id: UniqueId,
        @Body body: PlaylistEditViewModel,
    ): HttpResponse<Any> {
        val existing = db.playlists.findByIdOrNull(id)
            ?: throw HttpResponseException(ApiErrorCode.ERR_NOT_FOUND)

        db.playlists.update(existing.copy(name = body.name, updatedAt = Instant.now()))

        // Delete playlist sequences that should be removed.
        body.deletions.forEach { db.playlistSequences.deleteById(it) }

        // Insert new playlist sequences.
        val insertions = body.insertions.map { it.toModel(playlistId = id) }
        db.playlistSequences.saveAll(insertions)

        return HttpResponse.noContent()
    }

    @Delete("/{id}")
    @Transactional
    fun deletePlaylist(id: UniqueId): HttpResponse<Any> {
        db.playlists.deleteById(id)
        return HttpResponse.noContent()
    }
}
