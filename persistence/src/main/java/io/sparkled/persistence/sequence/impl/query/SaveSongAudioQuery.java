package io.sparkled.persistence.sequence.impl.query;

import io.sparkled.model.entity.SongAudio;
import io.sparkled.persistence.PersistenceQuery;
import io.sparkled.persistence.QueryFactory;

import javax.persistence.EntityManager;

public class SaveSongAudioQuery implements PersistenceQuery<Integer> {

    private final SongAudio songAudio;

    public SaveSongAudioQuery(SongAudio songAudio) {
        this.songAudio = songAudio;
    }

    @Override
    public Integer perform(QueryFactory queryFactory) {
        final EntityManager entityManager = queryFactory.getEntityManager();

        SongAudio savedSongAudio = entityManager.merge(songAudio);
        return savedSongAudio.getSequenceId();
    }
}
