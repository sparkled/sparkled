package io.sparkled.persistence.sequence.impl.query;

import io.sparkled.persistence.PersistenceQuery;
import io.sparkled.persistence.QueryFactory;
import io.sparkled.persistence.playlist.impl.query.DeletePlaylistSequencesQuery;
import io.sparkled.persistence.stage.impl.query.DeleteRenderedStagePropsQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class DeleteSequencesQuery implements PersistenceQuery<Void> {

    private static final Logger logger = LoggerFactory.getLogger(DeleteSequencesQuery.class);

    private final Collection<Integer> sequenceIds;

    public DeleteSequencesQuery(Collection<Integer> sequenceIds) {
        this.sequenceIds = sequenceIds.isEmpty() ? noIds : sequenceIds;
    }

    @Override
    public Void perform(QueryFactory queryFactory) {
        deletePlaylistSequences(queryFactory);
        deleteRenderedStageProps(queryFactory);
        deleteSequenceChannels(queryFactory);
        deleteSongAudios(queryFactory);
        deleteSequences(queryFactory);
        return null;
    }

    private void deletePlaylistSequences(QueryFactory queryFactory) {
        List<UUID> playlistSequenceUuids = queryFactory
                .select(qPlaylistSequence.uuid)
                .from(qPlaylistSequence)
                .where(qPlaylistSequence.sequenceId.in(sequenceIds))
                .fetch();
        new DeletePlaylistSequencesQuery(playlistSequenceUuids).perform(queryFactory);
    }

    private void deleteRenderedStageProps(QueryFactory queryFactory) {
        List<Integer> renderedStagePropIds = queryFactory
                .select(qRenderedStageProp.id)
                .from(qRenderedStageProp)
                .where(qRenderedStageProp.sequenceId.in(sequenceIds))
                .fetch();
        new DeleteRenderedStagePropsQuery(renderedStagePropIds).perform(queryFactory);
    }

    private void deleteSequenceChannels(QueryFactory queryFactory) {
        List<UUID> sequenceChannelUuids = queryFactory
                .select(qSequenceChannel.uuid)
                .from(qSequenceChannel)
                .where(qSequenceChannel.sequenceId.in(sequenceIds))
                .fetch();
        new DeleteSequenceChannelsQuery(sequenceChannelUuids).perform(queryFactory);
    }

    private void deleteSongAudios(QueryFactory queryFactory) {
        long deleted = queryFactory
                .delete(qSongAudio)
                .where(qSongAudio.sequenceId.in(sequenceIds))
                .execute();

        logger.info("Deleted {} song audio(s).", deleted);
    }

    private void deleteSequences(QueryFactory queryFactory) {
        long deleted = queryFactory
                .delete(qSequence)
                .where(qSequence.id.in(sequenceIds))
                .execute();

        logger.info("Deleted {} sequence(s).", deleted);
    }
}
