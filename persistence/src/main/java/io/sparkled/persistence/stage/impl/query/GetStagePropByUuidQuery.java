package io.sparkled.persistence.stage.impl.query;

import io.sparkled.model.entity.StageProp;
import io.sparkled.persistence.PersistenceQuery;
import io.sparkled.persistence.QueryFactory;

import java.util.Optional;
import java.util.UUID;

public class GetStagePropByUuidQuery implements PersistenceQuery<Optional<StageProp>> {

    private final int stageId;
    private final UUID uuid;

    public GetStagePropByUuidQuery(int stageId, UUID uuid) {
        this.stageId = stageId;
        this.uuid = uuid;
    }

    @Override
    public Optional<StageProp> perform(QueryFactory queryFactory) {
        StageProp sequenceChannel = queryFactory
                .selectFrom(qStageProp)
                .where(qStageProp.stageId.eq(stageId).and(qStageProp.uuid.eq(uuid)))
                .fetchFirst();

        return Optional.ofNullable(sequenceChannel);
    }
}
