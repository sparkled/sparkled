package net.chrisparton.sparkled.persistence.song.impl.query;

import net.chrisparton.sparkled.model.entity.RenderedSong;
import net.chrisparton.sparkled.persistence.PersistenceQuery;

import javax.persistence.EntityManager;

public class SaveRenderedSongQuery implements PersistenceQuery<Integer> {

    private final RenderedSong renderedSong;

    public SaveRenderedSongQuery(RenderedSong renderedSong) {
        this.renderedSong = renderedSong;
    }

    @Override
    public Integer perform(EntityManager entityManager) {
        RenderedSong savedRenderedSong = entityManager.merge(renderedSong);
        return savedRenderedSong.getSongId();
    }
}
