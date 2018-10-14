package io.sparkled.persistence.song.impl.query;

import io.sparkled.model.entity.Song;
import io.sparkled.model.entity.SongAudio;
import io.sparkled.model.validator.SongAudioValidator;
import io.sparkled.persistence.PersistenceQuery;
import io.sparkled.persistence.QueryFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;

public class SaveSongAudioQuery implements PersistenceQuery<Void> {

    private static final Logger logger = LoggerFactory.getLogger(SaveSongQuery.class);

    private final Song song;
    private final byte[] audioData;

    public SaveSongAudioQuery(Song song, byte[] audioData) {
        this.song = song;
        this.audioData = audioData;
    }

    @Override
    public Void perform(QueryFactory queryFactory) {
        SongAudio songAudio = new SongAudio().setSongId(song.getId()).setAudioData(audioData);
        new SongAudioValidator().validate(songAudio);

        final EntityManager entityManager = queryFactory.getEntityManager();
        SongAudio savedSongAudio = entityManager.merge(songAudio);

        logger.info("Saved song audio {} for song {}.", savedSongAudio.getSongId(), song.getName());
        return null;
    }
}
