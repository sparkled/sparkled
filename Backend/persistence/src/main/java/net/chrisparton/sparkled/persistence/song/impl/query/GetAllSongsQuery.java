package net.chrisparton.sparkled.persistence.song.impl.query;

import net.chrisparton.sparkled.entity.Song;
import net.chrisparton.sparkled.entity.Song_;
import net.chrisparton.sparkled.persistence.PersistenceQuery;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class GetAllSongsQuery implements PersistenceQuery<List<Song>> {

    @Override
    public List<Song> perform(EntityManager entityManager) {
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
}
