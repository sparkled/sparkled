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
                .select(qSongAudio)
                .from(qSequence)
                .innerJoin(qSongAudio).on(qSequence.songId.eq(qSongAudio.songId))
                .where(qSequence.id.eq(sequenceId))
                .fetchFirst();

        return Optional.ofNullable(songAudio);
    }
}
