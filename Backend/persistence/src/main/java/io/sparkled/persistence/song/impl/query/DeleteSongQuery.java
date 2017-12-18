package io.sparkled.persistence.song.impl.query;

import io.sparkled.model.entity.*;
import io.sparkled.persistence.PersistenceQuery;
import io.sparkled.persistence.scheduler.impl.query.GetScheduledSongsBySongIdQuery;

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

        Optional<RenderedSong> renderedSong = new GetRenderedSongBySongIdQuery(songId).perform(entityManager);
        renderedSong.ifPresent(entityManager::remove);

        Optional<Song> song = new GetSongByIdQuery(songId).perform(entityManager);
        song.ifPresent(entityManager::remove);
        return songId;
    }
}
