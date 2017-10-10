package net.chrisparton.sparkled.persistence.song.impl.query;

import net.chrisparton.sparkled.model.entity.Song;
import net.chrisparton.sparkled.model.validator.SongValidator;
import net.chrisparton.sparkled.persistence.PersistenceQuery;

import javax.persistence.EntityManager;

public class SaveSongQuery implements PersistenceQuery<Integer> {

    private final Song song;

    public SaveSongQuery(Song song) {
        this.song = song;
    }

    @Override
    public Integer perform(EntityManager entityManager) {
        new SongValidator(song).validate();
        Song result = entityManager.merge(song);
        return result.getId();
    }
}
