package io.sparkled.persistence.song.impl.query;

import io.sparkled.model.entity.Song;
import io.sparkled.model.validator.SongValidator;
import io.sparkled.persistence.PersistenceQuery;
import io.sparkled.persistence.QueryFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;

public class SaveSongQuery implements PersistenceQuery<Song> {

    private static final Logger logger = LoggerFactory.getLogger(SaveSongQuery.class);

    private final Song song;

    public SaveSongQuery(Song song) {
        this.song = song;
    }

    @Override
    public Song perform(QueryFactory queryFactory) {
        new SongValidator().validate(song);

        final EntityManager entityManager = queryFactory.getEntityManager();
        Song savedSong = entityManager.merge(song);

        logger.info("Saved song {} ({}).", savedSong.getId(), savedSong.getName());
        return savedSong;
    }
}
