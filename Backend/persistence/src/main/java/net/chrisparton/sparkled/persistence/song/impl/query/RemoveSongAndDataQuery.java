package net.chrisparton.sparkled.persistence.song.impl.query;

import net.chrisparton.sparkled.entity.Song;
import net.chrisparton.sparkled.entity.SongData;
import net.chrisparton.sparkled.persistence.PersistenceQuery;

import javax.persistence.EntityManager;
import java.util.Optional;

public class RemoveSongAndDataQuery implements PersistenceQuery<Boolean> {

    private final int songId;

    public RemoveSongAndDataQuery(int songId) {
        this.songId = songId;
    }

    @Override
    public Boolean perform(EntityManager entityManager) {
        Optional<Song> song = new GetSongByIdQuery(songId).perform(entityManager);
        if (song.isPresent()) {
            entityManager.remove(song.get());
        } else {
            return false;
        }

        Optional<SongData> songData = new GetSongDataByIdQuery(songId).perform(entityManager);
        if (songData.isPresent()) {
            entityManager.remove(songData.get());
            return true;
        }

        return false;
    }
}
