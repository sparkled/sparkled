package net.chrisparton.sparkled.persistence.song.impl.query;

import net.chrisparton.sparkled.entity.RenderedSong;
import net.chrisparton.sparkled.entity.Song;
import net.chrisparton.sparkled.persistence.PersistenceQuery;

import javax.persistence.EntityManager;

public class SaveSongQuery implements PersistenceQuery<Integer> {

    private final Song song;
    private final RenderedSong renderedSong;

    public SaveSongQuery(Song song, RenderedSong renderedSong) {
        this.song = song;
        this.renderedSong = renderedSong;
    }

    @Override
    public Integer perform(EntityManager entityManager) {
        Song result = entityManager.merge(song);
        renderedSong.setSongId(result.getId());
        entityManager.merge(renderedSong);
        return result.getId();
    }
}
