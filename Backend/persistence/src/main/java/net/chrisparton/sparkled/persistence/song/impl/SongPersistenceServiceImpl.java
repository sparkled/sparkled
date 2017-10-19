package net.chrisparton.sparkled.persistence.song.impl;

import com.google.inject.persist.Transactional;
import net.chrisparton.sparkled.model.entity.RenderedSong;
import net.chrisparton.sparkled.model.entity.Song;
import net.chrisparton.sparkled.model.entity.SongAnimation;
import net.chrisparton.sparkled.model.entity.SongAudio;
import net.chrisparton.sparkled.persistence.song.SongPersistenceService;
import net.chrisparton.sparkled.persistence.song.impl.query.*;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

public class SongPersistenceServiceImpl implements SongPersistenceService {

    private Provider<EntityManager> entityManagerProvider;

    @Inject
    public SongPersistenceServiceImpl(Provider<EntityManager> entityManagerProvider) {
        this.entityManagerProvider = entityManagerProvider;
    }

    @Override
    @Transactional
    public List<Song> getAllSongs() {
        return new GetAllSongsQuery().perform(entityManagerProvider.get());
    }

    @Override
    @Transactional
    public Integer deleteSong(int songId) {
        return new DeleteSongQuery(songId).perform(entityManagerProvider.get());
    }

    @Override
    @Transactional
    public Optional<Song> getSongById(int songId) {
        return new GetSongByIdQuery(songId).perform(entityManagerProvider.get());
    }

    @Override
    @Transactional
    public Optional<SongAudio> getSongDataById(int songId) {
        return new GetSongDataBySongIdQuery(songId).perform(entityManagerProvider.get());
    }

    @Override
    @Transactional
    public Optional<SongAnimation> getSongAnimationById(int songId) {
        return new GetSongAnimationBySongIdQuery(songId).perform(entityManagerProvider.get());
    }

    @Override
    @Transactional
    public Optional<RenderedSong> getRenderedSongById(int songId) {
        return new GetRenderedSongBySongIdQuery(songId).perform(entityManagerProvider.get());
    }

    @Override
    @Transactional
    public Integer saveSong(Song song) {
        return new SaveSongQuery(song).perform(entityManagerProvider.get());
    }

    @Override
    @Transactional
    public Integer saveSongAudio(SongAudio songAudio) {
        return new SaveSongAudioQuery(songAudio).perform(entityManagerProvider.get());
    }

    @Override
    @Transactional
    public Integer saveSongAnimation(SongAnimation songAnimation) {
        return new SaveSongAnimationQuery(songAnimation).perform(entityManagerProvider.get());
    }

    @Override
    @Transactional
    public Integer saveRenderedSong(RenderedSong renderedSong) {
        return new SaveRenderedSongQuery(renderedSong).perform(entityManagerProvider.get());
    }
}
