package io.sparkled.persistence.sequence.impl.query;

import io.sparkled.model.entity.SequenceChannel;
import io.sparkled.persistence.PersistenceQuery;
import io.sparkled.persistence.QueryFactory;

import java.util.List;

public class GetSequenceChannelsBySequenceIdQuery implements PersistenceQuery<List<SequenceChannel>> {

    private final int sequenceId;

    public GetSequenceChannelsBySequenceIdQuery(int sequenceId) {
        this.sequenceId = sequenceId;
    }

    @Override
    public List<SequenceChannel> perform(QueryFactory queryFactory) {
        return queryFactory
                .selectFrom(qSequenceChannel)
                .where(qSequenceChannel.sequenceId.eq(sequenceId))
                .orderBy(qSequenceChannel.displayOrder.asc())
                .fetch();
    }
}
