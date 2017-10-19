package net.chrisparton.sparkled.persistence.song.impl.query;

import net.chrisparton.sparkled.model.entity.Song_;
import net.chrisparton.sparkled.model.entity.Song;
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
        Root<Song> song = cq.from(Song.class);

        cq.orderBy(
                cb.asc(song.get(Song_.artist)),
                cb.asc(song.get(Song_.album)),
                cb.asc(song.get(Song_.name))
        );

        TypedQuery<Song> query = entityManager.createQuery(cq);
        return query.getResultList();
    }
}
