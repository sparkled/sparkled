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
import java.util.Optional;

public class GetSongByIdQuery implements PersistenceQuery<Optional<Song>> {

    private final int songId;

    public GetSongByIdQuery(int songId) {
        this.songId = songId;
    }

    @Override
    public Optional<Song> perform(EntityManager entityManager) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Song> cq = cb.createQuery(Song.class);
        Root<Song> song = cq.from(Song.class);
        cq.where(
                cb.equal(song.get(Song_.id), songId)
        );

        TypedQuery<Song> query = entityManager.createQuery(cq);

        List<Song> songs = query.getResultList();
        if (!songs.isEmpty()) {
            return Optional.of(songs.get(0));
        } else {
            return Optional.empty();
        }
    }
}
