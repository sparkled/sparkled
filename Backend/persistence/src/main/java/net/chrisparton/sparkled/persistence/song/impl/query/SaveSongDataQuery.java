package net.chrisparton.sparkled.persistence.song.impl.query;

import net.chrisparton.sparkled.entity.SongData;
import net.chrisparton.sparkled.persistence.PersistenceQuery;

import javax.persistence.EntityManager;

public class SaveSongDataQuery implements PersistenceQuery<Integer> {

    private final SongData songData;

    public SaveSongDataQuery(SongData songData) {
        this.songData = songData;
    }

    @Override
    public Integer perform(EntityManager entityManager) {
        SongData savedSongData = entityManager.merge(songData);
        return savedSongData.getSongId();
    }
}
