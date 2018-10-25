package io.sparkled.persistence.playlist.impl.query

import com.querydsl.core.Tuple
import io.sparkled.model.playlist.PlaylistSummary
import io.sparkled.model.util.TupleUtils
import io.sparkled.persistence.PersistenceQuery
import io.sparkled.persistence.QueryFactory

import io.sparkled.model.constant.ModelConstants.MS_PER_SECOND
import java.util.stream.Collectors.toMap

class GetPlaylistSummariesQuery : PersistenceQuery<Map<Integer, PlaylistSummary>> {

    @Override
    fun perform(queryFactory: QueryFactory): Map<Integer, PlaylistSummary> {
        return queryFactory
                .select(qPlaylist.id, qSequence.count(), qSong.durationMs.sum())
                .from(qPlaylist)
                .leftJoin(qPlaylistSequence).on(qPlaylist.id.eq(qPlaylistSequence.playlistId))
                .leftJoin(qSequence).on(qSequence.id.eq(qPlaylistSequence.sequenceId))
                .leftJoin(qSong).on(qSong.id.eq(qSequence.songId))
                .groupBy(qPlaylist.id)
                .fetch()
                .stream()
                .collect(toMap(???({ this.toKey(it) }), ???({ this.toSummary(it) })))
    }

    private fun toKey(tuple: Tuple): Integer {
        return TupleUtils.getInt(tuple, 0)
    }

    private fun toSummary(tuple: Tuple): PlaylistSummary {
        return PlaylistSummary()
                .setSequenceCount(TupleUtils.getInt(tuple, 1))
                .setDurationSeconds(TupleUtils.getInt(tuple, 2) / MS_PER_SECOND)
    }
}