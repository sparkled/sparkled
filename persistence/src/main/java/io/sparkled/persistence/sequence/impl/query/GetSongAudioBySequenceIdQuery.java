package io.sparkled.persistence.sequence.impl.query;

import io.sparkled.model.entity.SongAudio;
import io.sparkled.persistence.PersistenceQuery;
import io.sparkled.persistence.QueryFactory;

import java.util.Optional;

public class GetSongAudioBySequenceIdQuery implements PersistenceQuery<Optional<SongAudio>> {

    private final int sequenceId;

    public GetSongAudioBySequenceIdQuery(int sequenceId) {
        this.sequenceId = sequenceId;
    }

    @Override
    public Optional<SongAudio> perform(QueryFactory queryFactory) {
        SongAudio songAudio = queryFactory
                .selectFrom(qSongAudio)
                .where(qSongAudio.sequenceId.eq(sequenceId))
                .fetchFirst();

        return Optional.ofNullable(songAudio);
    }
}
