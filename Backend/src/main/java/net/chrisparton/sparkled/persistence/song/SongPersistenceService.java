package net.chrisparton.sparkled.persistence.song;

import net.chrisparton.sparkled.entity.Song;
import net.chrisparton.sparkled.entity.SongData;
import net.chrisparton.sparkled.entity.SongData_;
import net.chrisparton.sparkled.entity.Song_;
import net.chrisparton.sparkled.persistence.PersistenceService;
import net.chrisparton.sparkled.preprocessor.SongPreprocessor;

import javax.persistence.EntityManager;
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

    public int saveSong(Song song) {
        SongPreprocessor preprocessor = new SongPreprocessor(song);
        preprocessor.validate();
        preprocessor.escapeText();

        return saveEntity(song).getId();
    }

    public int saveSongData(SongData songData) {
        return saveEntity(songData).getSongId();
    }

    private <T> T saveEntity(T entity) {
        return PersistenceService.instance().perform(
                entityManager -> entityManager.merge(entity)
        );
    }
}
