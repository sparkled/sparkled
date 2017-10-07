package net.chrisparton.sparkled.persistence.song.impl.query;

import net.chrisparton.sparkled.entity.SongData;
import net.chrisparton.sparkled.entity.SongData_;
import net.chrisparton.sparkled.persistence.PersistenceQuery;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

public class GetSongDataByIdQuery implements PersistenceQuery<Optional<SongData>> {

    private final int songId;

    public GetSongDataByIdQuery(int songId) {
        this.songId = songId;
    }

    @Override
    public Optional<SongData> perform(EntityManager entityManager) {
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
}
