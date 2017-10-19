package net.chrisparton.sparkled.persistence.song.impl.query;

import net.chrisparton.sparkled.model.entity.ScheduledSong;
import net.chrisparton.sparkled.model.entity.Song;
import net.chrisparton.sparkled.model.entity.SongAnimation;
import net.chrisparton.sparkled.model.entity.SongAudio;
import net.chrisparton.sparkled.persistence.PersistenceQuery;
import net.chrisparton.sparkled.persistence.scheduler.impl.query.GetScheduledSongsBySongIdQuery;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

public class DeleteSongQuery implements PersistenceQuery<Integer> {

    private final int songId;

    public DeleteSongQuery(int songId) {
        this.songId = songId;
    }

    @Override
    public Integer perform(EntityManager entityManager) {
        Optional<SongAudio> songData = new GetSongDataBySongIdQuery(songId).perform(entityManager);
        songData.ifPresent(entityManager::remove);

        Optional<SongAnimation> songAnimation = new GetSongAnimationBySongIdQuery(songId).perform(entityManager);
        songAnimation.ifPresent(entityManager::remove);

        List<ScheduledSong> scheduledSongs = new GetScheduledSongsBySongIdQuery(songId).perform(entityManager);
        scheduledSongs.forEach(entityManager::remove);

        Optional<Song> song = new GetSongByIdQuery(songId).perform(entityManager);
        song.ifPresent(entityManager::remove);
        return songId;
    }
}
