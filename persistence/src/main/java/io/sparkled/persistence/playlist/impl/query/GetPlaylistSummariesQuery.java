package io.sparkled.persistence.playlist.impl.query;

import com.querydsl.core.Tuple;
import io.sparkled.model.entity.PlaylistSummary;
import io.sparkled.model.util.TupleUtils;
import io.sparkled.persistence.PersistenceQuery;
import io.sparkled.persistence.QueryFactory;

import java.util.Map;

import static io.sparkled.model.constant.ModelConstants.MS_PER_SECOND;
import static java.util.stream.Collectors.toMap;

public class GetPlaylistSummariesQuery implements PersistenceQuery<Map<Integer, PlaylistSummary>> {

    @Override
    public Map<Integer, PlaylistSummary> perform(QueryFactory queryFactory) {
        return queryFactory
                .select(qPlaylist.id, qSequence.count(), qSong.durationMs.sum())
                .from(qPlaylist)
                .leftJoin(qPlaylistSequence).on(qPlaylist.id.eq(qPlaylistSequence.playlistId))
                .leftJoin(qSequence).on(qSequence.id.eq(qPlaylistSequence.sequenceId))
                .leftJoin(qSong).on(qSong.id.eq(qSequence.songId))
                .groupBy(qPlaylist.id)
                .fetch()
                .stream()
                .collect(toMap(this::toKey, this::toSummary));
    }

    private Integer toKey(Tuple tuple) {
        return TupleUtils.getInt(tuple, 0);
    }

    private PlaylistSummary toSummary(Tuple tuple) {
        return new PlaylistSummary()
                .setSequenceCount(TupleUtils.getInt(tuple, 1))
                .setDurationSeconds(TupleUtils.getInt(tuple, 2) / MS_PER_SECOND);
    }
}