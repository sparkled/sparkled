package io.sparkled.persistence.sequence.impl.query;

import io.sparkled.model.entity.StageProp;
import io.sparkled.persistence.PersistenceQuery;
import io.sparkled.persistence.QueryFactory;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static java.util.stream.Collectors.toMap;

public class GetSequenceStagePropUuidMapBySequenceIdQuery implements PersistenceQuery<Map<String, UUID>> {

    private final int sequenceId;

    public GetSequenceStagePropUuidMapBySequenceIdQuery(int sequenceId) {
        this.sequenceId = sequenceId;
    }

    @Override
    public Map<String, UUID> perform(QueryFactory queryFactory) {
        List<StageProp> stageProps = queryFactory
                .select(qStageProp)
                .from(qSequence)
                .innerJoin(qStage).on(qSequence.stageId.eq(qStage.id))
                .innerJoin(qStageProp).on(qStage.id.eq(qStageProp.stageId))
                .where(qSequence.id.eq(sequenceId))
                .fetch();

        return stageProps.stream().collect(toMap(StageProp::getCode, StageProp::getUuid));
    }
}
