package io.sparkled.persistence.song.impl.query;

import io.sparkled.persistence.PersistenceQuery;
import io.sparkled.persistence.QueryFactory;
import io.sparkled.persistence.sequence.impl.query.DeleteSequencesQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;

public class DeleteSongsQuery implements PersistenceQuery<Void> {

    private static final Logger logger = LoggerFactory.getLogger(DeleteSongsQuery.class);

    private final Collection<Integer> songIds;

    public DeleteSongsQuery(Collection<Integer> songIds) {
        this.songIds = songIds.isEmpty() ? noIds : songIds;
    }

    @Override
    public Void perform(QueryFactory queryFactory) {
        deleteSequences(queryFactory);
        deleteSongAudios(queryFactory);
        deleteSongs(queryFactory);
        return null;
    }

    private void deleteSequences(QueryFactory queryFactory) {
        List<Integer> sequenceIds = queryFactory
                .select(qSequence.id)
                .from(qSequence)
                .where(qSequence.songId.in(songIds))
                .fetch();
        new DeleteSequencesQuery(sequenceIds).perform(queryFactory);
    }

    private void deleteSongAudios(QueryFactory queryFactory) {
        long deleted = queryFactory
                .delete(qSongAudio)
                .where(qSongAudio.songId.in(songIds))
                .execute();

        logger.info("Deleted {} song audio(s).", deleted);
    }

    private void deleteSongs(QueryFactory queryFactory) {
        long deleted = queryFactory
                .delete(qSong)
                .where(qSong.id.in(songIds))
                .execute();

        logger.info("Deleted {} song(s).", deleted);
    }
}
