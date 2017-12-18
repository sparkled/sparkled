package io.sparkled.persistence.song.impl.query;

import io.sparkled.model.entity.RenderedSong;
import io.sparkled.model.entity.RenderedSong_;
import io.sparkled.persistence.PersistenceQuery;
import io.sparkled.persistence.util.PersistenceUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Optional;

public class GetRenderedSongBySongIdQuery implements PersistenceQuery<Optional<RenderedSong>> {

    private final int songId;

    public GetRenderedSongBySongIdQuery(int songId) {
        this.songId = songId;
    }

    @Override
    public Optional<RenderedSong> perform(EntityManager entityManager) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<RenderedSong> cq = cb.createQuery(RenderedSong.class);
        Root<RenderedSong> renderedSong = cq.from(RenderedSong.class);
        cq.where(
                cb.equal(renderedSong.get(RenderedSong_.songId), songId)
        );

        TypedQuery<RenderedSong> query = entityManager.createQuery(cq);
        return PersistenceUtils.getSingleResult(query);
    }
}
