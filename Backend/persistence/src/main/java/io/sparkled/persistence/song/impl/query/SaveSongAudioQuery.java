package io.sparkled.persistence.song.impl.query;

import io.sparkled.model.entity.SongAudio;
import io.sparkled.persistence.PersistenceQuery;

import javax.persistence.EntityManager;

public class SaveSongAudioQuery implements PersistenceQuery<Integer> {

    private final SongAudio songAudio;

    public SaveSongAudioQuery(SongAudio songAudio) {
        this.songAudio = songAudio;
    }

    @Override
    public Integer perform(EntityManager entityManager) {
        SongAudio savedSongAudio = entityManager.merge(songAudio);
        return savedSongAudio.getSongId();
    }
}
