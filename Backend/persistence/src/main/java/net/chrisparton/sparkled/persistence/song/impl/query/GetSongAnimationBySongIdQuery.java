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

public class GetSongAnimationBySongIdQuery implements PersistenceQuery<Optional<SongAnimation>> {

    private final int songId;

    public GetSongAnimationBySongIdQuery(int songId) {
        this.songId = songId;
    }

    @Override
    public Optional<SongAnimation> perform(EntityManager entityManager) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<SongAnimation> cq = cb.createQuery(SongAnimation.class);
        Root<SongAnimation> songAnimation = cq.from(SongAnimation.class);
        cq.where(
                cb.equal(songAnimation.get(SongAnimation_.songId), songId)
        );

        TypedQuery<SongAnimation> query = entityManager.createQuery(cq);
        return PersistenceUtils.getSingleResult(query);
    }
}
