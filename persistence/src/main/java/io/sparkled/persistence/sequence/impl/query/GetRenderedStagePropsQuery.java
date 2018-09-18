package io.sparkled.persistence.sequence.impl.query;

import io.sparkled.model.entity.RenderedStageProp;
import io.sparkled.model.entity.Sequence;
import io.sparkled.model.render.RenderedStagePropData;
import io.sparkled.model.render.RenderedStagePropDataMap;
import io.sparkled.persistence.PersistenceQuery;
import io.sparkled.persistence.QueryFactory;

import java.util.List;

public class GetRenderedStagePropsQuery implements PersistenceQuery<RenderedStagePropDataMap> {

    private final Sequence sequence;

    public GetRenderedStagePropsQuery(Sequence sequence) {
        this.sequence = sequence;
    }

    @Override
    public RenderedStagePropDataMap perform(QueryFactory queryFactory) {
        List<RenderedStageProp> renderedStageProps = queryFactory
                .selectFrom(qRenderedStageProp)
                .where(qRenderedStageProp.sequenceId.eq(sequence.getId()))
                .fetch();

        RenderedStagePropDataMap renderedStagePropDataMap = new RenderedStagePropDataMap();
        renderedStageProps.forEach(stagePropData -> addToMap(renderedStagePropDataMap, stagePropData));
        return renderedStagePropDataMap;
    }

    private void addToMap(RenderedStagePropDataMap renderedStagePropDataMap, RenderedStageProp stagePropData) {
        RenderedStagePropData renderedStagePropData = new RenderedStagePropData(
                0,
                sequence.getDurationFrames() - 1,
                stagePropData.getLedCount(),
                stagePropData.getData()
        );

        renderedStagePropDataMap.put(stagePropData.getStagePropCode(), renderedStagePropData);
    }
}
