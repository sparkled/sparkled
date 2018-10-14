package io.sparkled.persistence.song.impl.query;

import io.sparkled.model.entity.Song;
import io.sparkled.persistence.PersistenceQuery;
import io.sparkled.persistence.QueryFactory;

import java.util.Optional;

public class GetSongByIdQuery implements PersistenceQuery<Optional<Song>> {

    private final int songId;

    public GetSongByIdQuery(int songId) {
        this.songId = songId;
    }

    @Override
    public Optional<Song> perform(QueryFactory queryFactory) {
        Song song = queryFactory
                .selectFrom(qSong)
                .where(qSong.id.eq(songId))
                .fetchFirst();

        return Optional.ofNullable(song);
    }
}
