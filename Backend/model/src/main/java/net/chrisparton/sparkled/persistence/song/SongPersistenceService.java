package net.chrisparton.sparkled.persistence.song;

import net.chrisparton.sparkled.entity.*;
import net.chrisparton.sparkled.persistence.PersistenceService;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

public class SongPersistenceService {

    public List<Song> getAllSongs() {
        return PersistenceService.instance().perform(this::getAllSongs);
    }

    private List<Song> getAllSongs(EntityManager entityManager) {
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

    public boolean removeSongAndData(int songId) {
        return PersistenceService.instance().perform(
                entityManager -> removeSongData(entityManager, songId) && removeSong(entityManager, songId)
        );
    }

    private boolean removeSong(EntityManager entityManager, int songId) {
        Optional<Song> song = getSongById(entityManager, songId);
        if (song.isPresent()) {
            entityManager.remove(song.get());
            return true;
        }

        return false;
    }

    private boolean removeSongData(EntityManager entityManager, int songId) {
        Optional<SongData> songData = getSongDataById(entityManager, songId);
        if (songData.isPresent()) {
            entityManager.remove(songData.get());
            return true;
        }

        return false;
    }

    public Optional<Song> getSongById(int songId) {
        return PersistenceService.instance().perform(
                entityManager -> getSongById(entityManager, songId)
        );
    }

    private Optional<Song> getSongById(EntityManager entityManager, int songId) {
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

    public Optional<SongData> getSongDataById(int songId) {
        return PersistenceService.instance().perform(
                entityManager -> getSongDataById(entityManager, songId)
        );
    }

    private Optional<SongData> getSongDataById(EntityManager entityManager, int songId) {
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

    public Optional<RenderedSong> getRenderedSongById(int songId) {
        return PersistenceService.instance().perform(
                entityManager -> getRenderedSongById(entityManager, songId)
        );
    }

    private Optional<RenderedSong> getRenderedSongById(EntityManager entityManager, int songId) {
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

    public int saveSong(Song song, RenderedSong renderedSong) {
        Song savedSong = PersistenceService.instance().perform(
                entityManager -> {
                    Song result = entityManager.merge(song);
                    renderedSong.setSongId(result.getId());
                    entityManager.merge(renderedSong);
                    return result;
                }
        );

        return savedSong.getId();
    }

    public int saveSongData(SongData songData) {
        SongData savedSongData = PersistenceService.instance().perform(
                entityManager -> entityManager.merge(songData)
        );

        return savedSongData.getSongId();
    }
}
