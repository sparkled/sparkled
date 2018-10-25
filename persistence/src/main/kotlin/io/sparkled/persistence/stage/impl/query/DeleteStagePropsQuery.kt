package io.sparkled.persistence.stage.impl.query;

import io.sparkled.persistence.PersistenceQuery;
import io.sparkled.persistence.QueryFactory;
import io.sparkled.persistence.sequence.impl.query.DeleteSequenceChannelsQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class DeleteStagePropsQuery implements PersistenceQuery<Void> {

    private static final Logger logger = LoggerFactory.getLogger(DeleteStagePropsQuery.class);

    private final Collection<UUID> stagePropUuids;

    DeleteStagePropsQuery(Collection<UUID> stagePropUuids) {
        this.stagePropUuids = stagePropUuids.isEmpty() ? noUuids : stagePropUuids;
    }

    @Override
    public Void perform(QueryFactory queryFactory) {
        deleteRenderedStageProps(queryFactory);
        deleteSequenceChannels(queryFactory);
        deleteStageProps(queryFactory);
        return null;
    }

    private void deleteRenderedStageProps(QueryFactory queryFactory) {
        List<Integer> renderedStagePropIds = queryFactory
                .select(qRenderedStageProp.id)
                .from(qRenderedStageProp)
                .where(qRenderedStageProp.stagePropUuid.in(stagePropUuids))
                .fetch();
        new DeleteRenderedStagePropsQuery(renderedStagePropIds).perform(queryFactory);
    }

    private void deleteSequenceChannels(QueryFactory queryFactory) {
        List<UUID> sequenceChannelUuids = queryFactory
                .select(qSequenceChannel.uuid)
                .from(qSequenceChannel)
                .where(qSequenceChannel.stagePropUuid.in(stagePropUuids))
                .fetch();
        new DeleteSequenceChannelsQuery(sequenceChannelUuids).perform(queryFactory);
    }

    private void deleteStageProps(QueryFactory queryFactory) {
        long deleted = queryFactory
                .delete(qStageProp)
                .where(qStageProp.uuid.in(stagePropUuids))
                .execute();

        logger.info("Deleted {} stage prop(s).", deleted);
    }
}
