package io.sparkled.persistence.playlist.impl.query;

import io.sparkled.model.entity.Sequence;
import io.sparkled.model.entity.Stage;
import io.sparkled.persistence.PersistenceQuery;
import io.sparkled.persistence.QueryFactory;

import java.util.Optional;

public class GetSequenceAtPlaylistIndexQuery implements PersistenceQuery<Optional<Sequence>> {

    private final int playlistId;
    private final int index;

    public GetSequenceAtPlaylistIndexQuery(int playlistId, int index) {
        this.playlistId = playlistId;
        this.index = index;
    }

    @Override
    public Optional<Sequence> perform(QueryFactory queryFactory) {
        final Sequence sequence = queryFactory
                .selectFrom(qSequence)
                .innerJoin(qPlaylistSequence).on(qSequence.id.eq(qPlaylistSequence.sequenceId))
                .where(qPlaylistSequence.playlistId.eq(playlistId))
                .orderBy(qPlaylistSequence.displayOrder.asc())
                .offset(index)
                .limit(1)
                .fetchFirst();

        return Optional.ofNullable(sequence);
    }
}
