package io.sparkled.persistence.sequence.impl.query;

import io.sparkled.persistence.PersistenceQuery;
import io.sparkled.persistence.QueryFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.UUID;

public class DeleteSequenceChannelsQuery implements PersistenceQuery<Void> {

    private static final Logger logger = LoggerFactory.getLogger(DeleteSequenceChannelsQuery.class);

    private final Collection<UUID> sequenceChannelUuids;

    public DeleteSequenceChannelsQuery(Collection<UUID> sequenceChannelUuids) {
        this.sequenceChannelUuids = sequenceChannelUuids.isEmpty() ? noUuids : sequenceChannelUuids;
    }

    @Override
    public Void perform(QueryFactory queryFactory) {
        long deleted = queryFactory
                .delete(qSequenceChannel)
                .where(qSequenceChannel.uuid.in(sequenceChannelUuids))
                .execute();

        logger.info("Deleted {} sequence channel(s).", deleted);
        return null;
    }
}
