package net.chrisparton.sparkled.persistence.song.impl.query;

import net.chrisparton.sparkled.model.entity.Song;
import net.chrisparton.sparkled.model.entity.SongAnimation;
import net.chrisparton.sparkled.model.entity.SongAudio;
import net.chrisparton.sparkled.persistence.PersistenceQuery;

import javax.persistence.EntityManager;
import java.util.Optional;

public class DeleteSongQuery implements PersistenceQuery<Void> {

    private final int songId;

    public DeleteSongQuery(int songId) {
        this.songId = songId;
    }

    @Override
    public Void perform(EntityManager entityManager) {
        Optional<SongAudio> songData = new GetSongDataByIdQuery(songId).perform(entityManager);
        songData.ifPresent(entityManager::remove);

        Optional<SongAnimation> songAnimation = new GetSongAnimationByIdQuery(songId).perform(entityManager);
        songAnimation.ifPresent(entityManager::remove);

        Optional<Song> song = new GetSongByIdQuery(songId).perform(entityManager);
        song.ifPresent(entityManager::remove);
        return null;
    }


}
