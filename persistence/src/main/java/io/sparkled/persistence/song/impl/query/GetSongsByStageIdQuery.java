package io.sparkled.persistence.song.impl.query;

import io.sparkled.model.entity.Song;
import io.sparkled.model.entity.Song_;
import io.sparkled.persistence.PersistenceQuery;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class GetSongsByStageIdQuery implements PersistenceQuery<List<Song>> {

    private final int stageId;

    public GetSongsByStageIdQuery(int stageId) {
        this.stageId = stageId;
    }

    @Override
    public List<Song> perform(EntityManager entityManager) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Song> cq = cb.createQuery(Song.class);
        Root<Song> song = cq.from(Song.class);

        cq.where(cb.equal(song.get(Song_.stageId), stageId));

        TypedQuery<Song> query = entityManager.createQuery(cq);
        return query.getResultList();
    }
}
