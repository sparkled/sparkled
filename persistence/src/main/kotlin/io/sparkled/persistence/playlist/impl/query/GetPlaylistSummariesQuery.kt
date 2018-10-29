package io.sparkled.persistence.playlist.impl.query

import com.querydsl.core.Tuple
import io.sparkled.model.constant.ModelConstants.Companion.MS_PER_SECOND
import io.sparkled.model.entity.QPlaylist.playlist
import io.sparkled.model.entity.QPlaylistSequence.playlistSequence
import io.sparkled.model.entity.QSequence.sequence
import io.sparkled.model.entity.QSong.song
import io.sparkled.model.playlist.PlaylistSummary
import io.sparkled.model.util.TupleUtils
import io.sparkled.persistence.PersistenceQuery
import io.sparkled.persistence.QueryFactory

class GetPlaylistSummariesQuery : PersistenceQuery<Map<Int, PlaylistSummary>> {

    override fun perform(queryFactory: QueryFactory): Map<Int, PlaylistSummary> {
        return queryFactory
            .select(playlist.id, sequence.count(), song.durationMs.sum())
            .from(playlist)
            .leftJoin(playlistSequence).on(playlist.id.eq(playlistSequence.playlistId))
            .leftJoin(sequence).on(sequence.id.eq(playlistSequence.sequenceId))
            .leftJoin(song).on(song.id.eq(sequence.songId))
            .groupBy(playlist.id)
            .fetch()
            .associateBy(this::toKey, this::toSummary)
    }

    private fun toKey(tuple: Tuple): Int {
        return TupleUtils.getInt(tuple, 0)
    }

    private fun toSummary(tuple: Tuple): PlaylistSummary {
        return PlaylistSummary()
            .setSequenceCount(TupleUtils.getInt(tuple, 1))
            .setDurationSeconds(TupleUtils.getInt(tuple, 2) / MS_PER_SECOND)
    }
}