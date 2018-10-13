package io.sparkled.persistence.song.impl.query;

import io.sparkled.model.entity.Song;
import io.sparkled.persistence.PersistenceQuery;
import io.sparkled.persistence.QueryFactory;

import java.util.Optional;

public class GetSongBySequenceIdQuery implements PersistenceQuery<Optional<Song>> {

    private final int sequenceId;

    public GetSongBySequenceIdQuery(int sequenceId) {
        this.sequenceId = sequenceId;
    }

    @Override
    public Optional<Song> perform(QueryFactory queryFactory) {
        Song song = queryFactory
                .select(qSong)
                .from(qSequence)
                .innerJoin(qSong).on(qSequence.songId.eq(qSong.id))
                .where(qSequence.id.eq(sequenceId))
                .fetchFirst();

        return Optional.ofNullable(song);
    }
}
