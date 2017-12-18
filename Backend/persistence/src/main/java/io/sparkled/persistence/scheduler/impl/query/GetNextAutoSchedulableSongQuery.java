package io.sparkled.persistence.scheduler.impl.query;

import io.sparkled.model.entity.Song;
import io.sparkled.model.entity.Song_;
import io.sparkled.persistence.PersistenceQuery;
import io.sparkled.persistence.util.PersistenceUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Optional;

public class GetNextAutoSchedulableSongQuery implements PersistenceQuery<Optional<Song>> {

    private int previousSongId;

    public GetNextAutoSchedulableSongQuery(int previousSongId) {
        this.previousSongId = previousSongId;
    }

    @Override
    public Optional<Song> perform(EntityManager entityManager) {
        Optional<Song> result = query(entityManager, previousSongId);
        if (!result.isPresent()) {
            // The last auto schedulable song has been played, so start from the beginning.
            result = query(entityManager, 0);
        }
        return result;
    }

    private Optional<Song> query(EntityManager entityManager, int previousSongId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Song> cq = cb.createQuery(Song.class);
        Root<Song> song = cq.from(Song.class);

        cq.where(
                cb.and(
                        cb.equal(song.get(Song_.autoSchedulable), true),
                        cb.greaterThan(song.get(Song_.id), previousSongId)
                )
        );

        cq.orderBy(
                cb.asc(song.get(Song_.id))
        );

        TypedQuery<Song> query = entityManager.createQuery(cq);
        query.setMaxResults(1);

        return PersistenceUtils.getSingleResult(query);
    }
}
