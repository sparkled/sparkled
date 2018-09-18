package io.sparkled.persistence.sequence.impl.query;

import io.sparkled.persistence.PersistenceQuery;
import io.sparkled.persistence.QueryFactory;

public class DeleteSequenceQuery implements PersistenceQuery<Integer> {

    private final int sequenceId;

    public DeleteSequenceQuery(int sequenceId) {
        this.sequenceId = sequenceId;
    }

    @Override
    public Integer perform(QueryFactory queryFactory) {
        deleteSongAudio(queryFactory);
        deleteSequenceChannels(queryFactory);
        deleteScheduledSequences(queryFactory);
        deleteRenderedStageProps(queryFactory);
        deleteSequence(queryFactory);

        return sequenceId;
    }

    private void deleteSongAudio(QueryFactory queryFactory) {
        queryFactory
                .delete(qSongAudio)
                .where(qSongAudio.sequenceId.eq(sequenceId))
                .execute();
    }

    private void deleteSequenceChannels(QueryFactory queryFactory) {
        queryFactory
                .delete(qSequenceChannel)
                .where(qSequenceChannel.sequenceId.eq(sequenceId))
                .execute();
    }

    private void deleteScheduledSequences(QueryFactory queryFactory) {
        // TODO: I didn't bother converting this query over, as the scheduler will be rewritten soon.
    }

    private void deleteRenderedStageProps(QueryFactory queryFactory) {
        queryFactory
                .delete(qRenderedStageProp)
                .where(qRenderedStageProp.sequenceId.eq(sequenceId))
                .execute();
    }

    private void deleteSequence(QueryFactory queryFactory) {
        queryFactory
                .delete(qSequence)
                .where(qSequence.id.eq(sequenceId))
                .execute();
    }
}
