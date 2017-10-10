package net.chrisparton.sparkled.persistence.song.impl.query;

import net.chrisparton.sparkled.model.entity.SongAnimation;
import net.chrisparton.sparkled.model.entity.SongAnimation_;
import net.chrisparton.sparkled.persistence.PersistenceQuery;
import net.chrisparton.sparkled.persistence.util.PersistenceUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Optional;

public class GetSongAnimationByIdQuery implements PersistenceQuery<Optional<SongAnimation>> {

    private final int songId;

    public GetSongAnimationByIdQuery(int songId) {
        this.songId = songId;
    }

    @Override
    public Optional<SongAnimation> perform(EntityManager entityManager) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<SongAnimation> cq = cb.createQuery(SongAnimation.class);
        Root<SongAnimation> SongAnimation = cq.from(SongAnimation.class);
        cq.where(
                cb.equal(SongAnimation.get(SongAnimation_.songId), songId)
        );

        TypedQuery<SongAnimation> query = entityManager.createQuery(cq);
        return PersistenceUtils.getSingleResult(query);
    }
}
