package io.sparkled.persistence.sequence.impl.query;

import io.sparkled.model.entity.RenderedStageProp;
import io.sparkled.model.entity.Sequence;
import io.sparkled.model.render.RenderedStagePropDataMap;
import io.sparkled.persistence.PersistenceQuery;
import io.sparkled.persistence.QueryFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import java.util.concurrent.atomic.AtomicInteger;

public class SaveRenderedStagePropsQuery implements PersistenceQuery<Void> {

    private static final Logger logger = LoggerFactory.getLogger(SaveRenderedStagePropsQuery.class);

    private final Sequence sequence;
    private final RenderedStagePropDataMap renderedStagePropDataMap;

    public SaveRenderedStagePropsQuery(Sequence sequence, RenderedStagePropDataMap renderedStagePropDataMap) {
        this.sequence = sequence;
        this.renderedStagePropDataMap = renderedStagePropDataMap;
    }

    @Override
    public Void perform(QueryFactory queryFactory) {
        final EntityManager entityManager = queryFactory.getEntityManager();
        final AtomicInteger recordsSaved = new AtomicInteger(0);

        renderedStagePropDataMap.forEach((key, value) -> {
            RenderedStageProp renderedStageProp = new RenderedStageProp()
                    .setSequenceId(sequence.getId())
                    .setStagePropCode(key)
                    .setLedCount(value.getLedCount())
                    .setData(value.getData());
            entityManager.merge(renderedStageProp);
        });

        logger.info("Saved {} record(s) for sequence {}.", recordsSaved.get(), sequence.getId());
        return null;
    }
}
