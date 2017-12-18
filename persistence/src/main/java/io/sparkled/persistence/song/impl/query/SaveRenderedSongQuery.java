package io.sparkled.persistence.song.impl.query;

import io.sparkled.model.entity.RenderedSong;
import io.sparkled.persistence.PersistenceQuery;

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
