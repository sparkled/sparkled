package io.sparkled.rest

import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.*
import io.sparkled.model.entity.v2.PlaylistEntity
import io.sparkled.model.entity.v2.PlaylistSequenceEntity
import io.sparkled.model.entity.v2.SequenceEntity
import io.sparkled.model.entity.v2.SongEntity
import io.sparkled.persistence.*
import io.sparkled.persistence.query.playlist.GetPlaylistSequencesByPlaylistIdQuery
import io.sparkled.rest.response.IdResponse
import io.sparkled.viewmodel.PlaylistSummaryViewModel
import io.sparkled.viewmodel.PlaylistViewModel
import org.springframework.transaction.annotation.Transactional

@Controller("/api/playlists")
open class PlaylistController(
    private val db: DbService
) {

    @Get("/")
    @Transactional(readOnly = true)
    open fun getAllPlaylists(): HttpResponse<Any> {
        val playlists = db.getAll<PlaylistEntity>(orderBy = "name")
        val playlistSequences = db.getAll<PlaylistSequenceEntity>().groupBy { it.playlistId }
        val sequences = db.getAll<SequenceEntity>()
        val songs = db.getAll<SongEntity>()

        val playlistSummaries = playlists.map {
            PlaylistSummaryViewModel.fromModel(
                it,
                playlistSequences[it.id] ?: emptyList(),
                sequences,
                songs
            )
        }

        return HttpResponse.ok(playlistSummaries)
    }

    @Get("/{id}")
    @Transactional(readOnly = true)
    open fun getPlaylist(id: Int): HttpResponse<Any> {
        val playlist = db.getById<PlaylistEntity>(id)
        val playlistSequences = db.query(GetPlaylistSequencesByPlaylistIdQuery(id))

        return if (playlist != null) {
            val viewModel = PlaylistViewModel.fromModel(playlist, playlistSequences)
            HttpResponse.ok(viewModel)
        } else HttpResponse.notFound("Playlist not found.")
    }

    @Post("/")
    @Transactional
    open fun createPlaylist(playlistViewModel: PlaylistViewModel): HttpResponse<Any> {
        val (playlist, playlistSequences) = playlistViewModel.toModel()
        val savedId = db.insert(playlist).toInt()
        playlistSequences.forEach { db.insert(it.copy(playlistId = savedId)) }

        return HttpResponse.ok(IdResponse(savedId))
    }

    @Put("/{id}")
    @Transactional
    open fun updatePlaylist(id: Int, playlistViewModel: PlaylistViewModel): HttpResponse<Any> {
        val playlistAndSequences = playlistViewModel.copy(id = id).toModel()
        val playlist = playlistAndSequences.first.copy(id = id)

        // Update playlist.
        db.update(playlist)

        val existingSequences = db.query(GetPlaylistSequencesByPlaylistIdQuery(id)).associateBy { it.uuid }
        val newSequences = playlistAndSequences.second.map { it.copy(playlistId = id) }.associateBy { it.uuid }

        // Delete playlist sequences that no longer exist.
        (existingSequences.keys - newSequences.keys).forEach { db.delete(existingSequences.getValue(it)) }

        // Insert playlist sequences that didn't exist previously.
        (newSequences.keys - existingSequences.keys).forEach { db.insert(newSequences.getValue(it)) }

        // Update playlist sequences that exist
        (newSequences.keys.intersect(existingSequences.keys)).forEach { db.update(newSequences.getValue(it)) }

        return HttpResponse.ok()
    }

    @Delete("/{id}")
    @Transactional
    open fun deletePlaylist(id: Int): HttpResponse<Any> {
        db.getById<PlaylistEntity>(id)?.let {
            db.delete(it)
        }

        return HttpResponse.ok()
    }
}
