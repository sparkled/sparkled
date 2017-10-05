package net.chrisparton.sparkled.persistence.song;

import com.google.inject.persist.Transactional;
import net.chrisparton.sparkled.entity.*;

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
        final EntityManager entityManager = entityManagerProvider.get();

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Song> cq = cb.createQuery(Song.class);
        Root<Song> songRoot = cq.from(Song.class);
        cq.multiselect(
                songRoot.get(Song_.id),
                songRoot.get(Song_.name),
                songRoot.get(Song_.artist),
                songRoot.get(Song_.album),
                songRoot.get(Song_.durationFrames),
                songRoot.get(Song_.framesPerSecond)
        );

        cq.orderBy(
                cb.asc(songRoot.get(Song_.artist)),
                cb.asc(songRoot.get(Song_.album)),
                cb.asc(songRoot.get(Song_.name))
        );

        TypedQuery<Song> query = entityManager.createQuery(cq);
        return query.getResultList();
    }

    @Override
    @Transactional
    public boolean removeSongAndData(int songId) {
        final EntityManager entityManager = entityManagerProvider.get();

        Optional<Song> song = getSongById(songId);
        if (song.isPresent()) {
            entityManager.remove(song.get());
        } else {
            return false;
        }

        Optional<SongData> songData = getSongDataById(songId);
        if (songData.isPresent()) {
            entityManager.remove(songData.get());
            return true;
        }

        return false;
    }

    @Override
    @Transactional
    public Optional<Song> getSongById(int songId) {
        final EntityManager entityManager = entityManagerProvider.get();

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Song> cq = cb.createQuery(Song.class);
        Root<Song> song = cq.from(Song.class);
        cq.where(
                cb.equal(song.get(Song_.id), songId)
        );

        TypedQuery<Song> query = entityManager.createQuery(cq);

        List<Song> songs = query.getResultList();
        if (!songs.isEmpty()) {
            return Optional.of(songs.get(0));
        } else {
            return Optional.empty();
        }
    }

    @Override
    @Transactional
    public Optional<SongData> getSongDataById(int songId) {
        final EntityManager entityManager = entityManagerProvider.get();

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<SongData> cq = cb.createQuery(SongData.class);
        Root<SongData> songData = cq.from(SongData.class);
        cq.where(
                cb.equal(songData.get(SongData_.songId), songId)
        );

        TypedQuery<SongData> query = entityManager.createQuery(cq);

        List<SongData> songDataList = query.getResultList();
        if (!songDataList.isEmpty()) {
            return Optional.of(songDataList.get(0));
        } else {
            return Optional.empty();
        }
    }

    @Override
    @Transactional
    public Optional<RenderedSong> getRenderedSongById(int songId) {
        final EntityManager entityManager = entityManagerProvider.get();

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<RenderedSong> cq = cb.createQuery(RenderedSong.class);
        Root<RenderedSong> renderedSong = cq.from(RenderedSong.class);
        cq.where(
                cb.equal(renderedSong.get(RenderedSong_.songId), songId)
        );

        TypedQuery<RenderedSong> query = entityManager.createQuery(cq);

        try {
            return Optional.ofNullable(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    @Transactional
    public int saveSong(Song song, RenderedSong renderedSong) {
        final EntityManager entityManager = entityManagerProvider.get();

        Song result = entityManager.merge(song);
        renderedSong.setSongId(result.getId());
        entityManager.merge(renderedSong);
        return result.getId();
    }

    @Override
    @Transactional
    public int saveSongData(SongData songData) {
        final EntityManager entityManager = entityManagerProvider.get();
        SongData savedSongData = entityManager.merge(songData);
        return savedSongData.getSongId();
    }
}
