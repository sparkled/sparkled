package net.chrisparton.sparkled.persistence.scheduler.impl.query;

import net.chrisparton.sparkled.model.entity.ScheduledSong;
import net.chrisparton.sparkled.model.entity.ScheduledSong_;
import net.chrisparton.sparkled.model.entity.Song;
import net.chrisparton.sparkled.model.entity.Song_;
import net.chrisparton.sparkled.persistence.PersistenceQuery;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import java.util.List;

public class GetScheduledSongsBySongIdQuery implements PersistenceQuery<List<ScheduledSong>> {

    private final int songId;

    public GetScheduledSongsBySongIdQuery(int songId) {
        this.songId = songId;
    }

    @Override
    public List<ScheduledSong> perform(EntityManager entityManager) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<ScheduledSong> cq = cb.createQuery(ScheduledSong.class);
        Root<ScheduledSong> scheduledSong = cq.from(ScheduledSong.class);
        Join<ScheduledSong, Song> song = scheduledSong.join(ScheduledSong_.song);
        cq.where(
                cb.equal(song.get(Song_.id), songId)
        );

        TypedQuery<ScheduledSong> query = entityManager.createQuery(cq);
        return query.getResultList();
    }
}
