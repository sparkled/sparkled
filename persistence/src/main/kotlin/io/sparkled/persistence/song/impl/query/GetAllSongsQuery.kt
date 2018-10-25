package io.sparkled.persistence.song.impl.query;

import io.sparkled.model.entity.Song;
import io.sparkled.persistence.PersistenceQuery;
import io.sparkled.persistence.QueryFactory;

import java.util.List;

public class GetAllSongsQuery implements PersistenceQuery<List<Song>> {

    @Override
    public List<Song> perform(QueryFactory queryFactory) {
        return queryFactory
                .selectFrom(qSong)
                .orderBy(
                        qSong.name.asc(),
                        qSong.album.asc(),
                        qSong.artist.asc()
                )
                .fetch();
    }
}
