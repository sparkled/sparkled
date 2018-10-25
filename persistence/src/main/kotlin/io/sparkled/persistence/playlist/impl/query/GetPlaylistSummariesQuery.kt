package io.sparkled.persistence.playlist.impl.query

import com.querydsl.core.Tuple
import io.sparkled.model.constant.ModelConstants.Companion.MS_PER_SECOND
import io.sparkled.model.playlist.PlaylistSummary
import io.sparkled.model.util.TupleUtils
import io.sparkled.persistence.PersistenceQuery
import io.sparkled.persistence.PersistenceQuery.Companion.qPlaylist
import io.sparkled.persistence.PersistenceQuery.Companion.qPlaylistSequence
import io.sparkled.persistence.PersistenceQuery.Companion.qSequence
import io.sparkled.persistence.PersistenceQuery.Companion.qSong
import io.sparkled.persistence.QueryFactory

class GetPlaylistSummariesQuery : PersistenceQuery<Map<Int, PlaylistSummary>> {

    override fun perform(queryFactory: QueryFactory): Map<Int, PlaylistSummary> {
        return queryFactory
                .select(qPlaylist.id, qSequence.count(), qSong.durationMs.sum())
                .from(qPlaylist)
                .leftJoin(qPlaylistSequence).on(qPlaylist.id.eq(qPlaylistSequence.playlistId))
                .leftJoin(qSequence).on(qSequence.id.eq(qPlaylistSequence.sequenceId))
                .leftJoin(qSong).on(qSong.id.eq(qSequence.songId))
                .groupBy(qPlaylist.id)
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