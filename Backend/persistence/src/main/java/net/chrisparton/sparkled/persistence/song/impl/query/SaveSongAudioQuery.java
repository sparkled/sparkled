package net.chrisparton.sparkled.persistence.song.impl.query;

import net.chrisparton.sparkled.model.entity.SongAudio;
import net.chrisparton.sparkled.persistence.PersistenceQuery;

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
