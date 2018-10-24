package io.sparkled.persistence.sequence.impl.query;

import io.sparkled.model.entity.SequenceChannel;
import io.sparkled.persistence.PersistenceQuery;
import io.sparkled.persistence.QueryFactory;

import java.util.Optional;
import java.util.UUID;

public class GetSequenceChannelByUuidQuery implements PersistenceQuery<Optional<SequenceChannel>> {

    private final int sequenceId;
    private final UUID uuid;

    public GetSequenceChannelByUuidQuery(int sequenceId, UUID uuid) {
        this.sequenceId = sequenceId;
        this.uuid = uuid;
    }

    @Override
    public Optional<SequenceChannel> perform(QueryFactory queryFactory) {
        SequenceChannel sequenceChannel = queryFactory
                .selectFrom(qSequenceChannel)
                .where(qSequenceChannel.sequenceId.eq(sequenceId).and(qSequenceChannel.uuid.eq(uuid)))
                .fetchFirst();

        return Optional.ofNullable(sequenceChannel);
    }
}
