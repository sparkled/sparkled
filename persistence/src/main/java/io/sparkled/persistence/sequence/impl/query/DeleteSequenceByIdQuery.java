package io.sparkled.persistence.sequence.impl.query;

import io.sparkled.persistence.PersistenceQuery;
import io.sparkled.persistence.QueryFactory;

public class DeleteSequenceByIdQuery implements PersistenceQuery<Void> {

    private final int sequenceId;

    public DeleteSequenceByIdQuery(int sequenceId) {
        this.sequenceId = sequenceId;
    }

    @Override
    public Void perform(QueryFactory queryFactory) {
        deleteSongAudio(queryFactory);
        deleteSequenceChannels(queryFactory);
        deletePlaylistSequences(queryFactory);
        deleteRenderedStageProps(queryFactory);
        deleteSequence(queryFactory);
        return null;
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

    private void deletePlaylistSequences(QueryFactory queryFactory) {
        queryFactory
                .delete(qPlaylistSequence)
                .where(qPlaylistSequence.sequenceId.eq(sequenceId))
                .execute();
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
