package io.sparkled.persistence.song.impl.query;

import io.sparkled.model.entity.SongAudio;
import io.sparkled.model.entity.SongAudio_;
import io.sparkled.persistence.PersistenceQuery;
import io.sparkled.persistence.util.PersistenceUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Optional;

public class GetSongDataBySongIdQuery implements PersistenceQuery<Optional<SongAudio>> {

    private final int songId;

    public GetSongDataBySongIdQuery(int songId) {
        this.songId = songId;
    }

    @Override
    public Optional<SongAudio> perform(EntityManager entityManager) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<SongAudio> cq = cb.createQuery(SongAudio.class);
        Root<SongAudio> songAudio = cq.from(SongAudio.class);
        cq.where(
                cb.equal(songAudio.get(SongAudio_.songId), songId)
        );

        TypedQuery<SongAudio> query = entityManager.createQuery(cq);
        return PersistenceUtils.getSingleResult(query);
    }
}
