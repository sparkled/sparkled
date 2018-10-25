package io.sparkled.persistence.playlist.impl.query;

import io.sparkled.model.entity.PlaylistSequence;
import io.sparkled.persistence.PersistenceQuery;
import io.sparkled.persistence.QueryFactory;

import java.util.Optional;
import java.util.UUID;

public class GetPlaylistSequenceByUuidQuery implements PersistenceQuery<Optional<PlaylistSequence>> {

    private final int sequenceId;
    private final UUID uuid;

    public GetPlaylistSequenceByUuidQuery(int sequenceId, UUID uuid) {
        this.sequenceId = sequenceId;
        this.uuid = uuid;
    }

    @Override
    public Optional<PlaylistSequence> perform(QueryFactory queryFactory) {
        PlaylistSequence playlistSequence = queryFactory
                .selectFrom(qPlaylistSequence)
                .where(qPlaylistSequence.sequenceId.eq(sequenceId).and(qPlaylistSequence.uuid.eq(uuid)))
                .fetchFirst();

        return Optional.ofNullable(playlistSequence);
    }
}
