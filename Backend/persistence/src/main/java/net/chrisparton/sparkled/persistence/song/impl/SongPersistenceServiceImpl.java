package net.chrisparton.sparkled.persistence.song.impl;

import com.google.inject.persist.Transactional;
import net.chrisparton.sparkled.entity.*;
import net.chrisparton.sparkled.persistence.song.SongPersistenceService;
import net.chrisparton.sparkled.persistence.song.impl.query.*;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
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
    public boolean removeSongAndData(int songId) {
        return new RemoveSongAndDataQuery(songId).perform(entityManagerProvider.get());
    }

    @Override
    @Transactional
    public Optional<Song> getSongById(int songId) {
        return new GetSongByIdQuery(songId).perform(entityManagerProvider.get());
    }

    @Override
    @Transactional
    public Optional<SongData> getSongDataById(int songId) {
        return new GetSongDataByIdQuery(songId).perform(entityManagerProvider.get());
    }

    @Override
    @Transactional
    public Optional<RenderedSong> getRenderedSongById(int songId) {
        return new GetRenderedSongByIdQuery(songId).perform(entityManagerProvider.get());
    }

    @Override
    @Transactional
    public int saveSong(Song song, RenderedSong renderedSong) {
        return new SaveSongQuery(song, renderedSong).perform(entityManagerProvider.get());
    }

    @Override
    @Transactional
    public int saveSongData(SongData songData) {
        return new SaveSongDataQuery(songData).perform(entityManagerProvider.get());
    }
}
