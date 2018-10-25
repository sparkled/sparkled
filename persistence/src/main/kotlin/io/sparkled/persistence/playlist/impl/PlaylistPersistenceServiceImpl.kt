package io.sparkled.persistence.playlist.impl

import io.sparkled.model.entity.Playlist
import io.sparkled.model.entity.PlaylistSequence
import io.sparkled.model.playlist.PlaylistSummary
import io.sparkled.model.entity.Sequence
import io.sparkled.persistence.QueryFactory
import io.sparkled.persistence.playlist.PlaylistPersistenceService
import io.sparkled.persistence.playlist.impl.query.*

import javax.inject.Inject
import java.util.Optional
import java.util.UUID

class PlaylistPersistenceServiceImpl @Inject
constructor(private val queryFactory: QueryFactory) : PlaylistPersistenceService {

    @Override
    fun createPlaylist(playlist: Playlist): Playlist {
        return SavePlaylistQuery(playlist).perform(queryFactory)
    }

    val allPlaylists: List<Playlist>
        @Override
        get() = GetAllPlaylistsQuery().perform(queryFactory)

    val playlistSummaries: Map<Integer, PlaylistSummary>
        @Override
        get() = GetPlaylistSummariesQuery().perform(queryFactory)

    @Override
    fun getPlaylistById(playlistId: Int): Optional<Playlist> {
        return GetPlaylistByIdQuery(playlistId).perform(queryFactory)
    }

    @Override
    fun getSequenceAtPlaylistIndex(playlistId: Int, index: Int): Optional<Sequence> {
        return GetSequenceAtPlaylistIndexQuery(playlistId, index).perform(queryFactory)
    }

    @Override
    fun getPlaylistSequencesByPlaylistId(playlistId: Int): List<PlaylistSequence> {
        return GetPlaylistSequencesByPlaylistIdQuery(playlistId).perform(queryFactory)
    }

    @Override
    fun getPlaylistSequenceByUuid(sequenceId: Int, uuid: UUID): Optional<PlaylistSequence> {
        return GetPlaylistSequenceByUuidQuery(sequenceId, uuid).perform(queryFactory)
    }

    @Override
    fun savePlaylist(playlist: Playlist, playlistSequences: List<PlaylistSequence>) {
        var playlist = playlist
        playlist = SavePlaylistQuery(playlist).perform(queryFactory)
        SavePlaylistSequencesQuery(playlist, playlistSequences).perform(queryFactory)
    }

    @Override
    fun deletePlaylist(playlistId: Int) {
        DeletePlaylistQuery(playlistId).perform(queryFactory)
    }
}
