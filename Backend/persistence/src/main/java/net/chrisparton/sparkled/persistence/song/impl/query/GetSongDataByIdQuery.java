package net.chrisparton.sparkled.persistence.song.impl.query;

import net.chrisparton.sparkled.model.entity.SongAudio;
import net.chrisparton.sparkled.model.entity.SongAudio_;
import net.chrisparton.sparkled.persistence.PersistenceQuery;
import net.chrisparton.sparkled.persistence.util.PersistenceUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Optional;

public class GetSongDataByIdQuery implements PersistenceQuery<Optional<SongAudio>> {

    private final int songId;

    public GetSongDataByIdQuery(int songId) {
        this.songId = songId;
    }

    @Override
    public Optional<SongAudio> perform(EntityManager entityManager) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<SongAudio> cq = cb.createQuery(SongAudio.class);
        Root<SongAudio> songData = cq.from(SongAudio.class);
        cq.where(
                cb.equal(songData.get(SongAudio_.songId), songId)
        );

        TypedQuery<SongAudio> query = entityManager.createQuery(cq);
        return PersistenceUtils.getSingleResult(query);
    }
}
