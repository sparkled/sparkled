package io.sparkled.persistence.sequence.impl.query;

import io.sparkled.model.entity.RenderedStageProp;
import io.sparkled.model.entity.Sequence;
import io.sparkled.model.render.RenderedStagePropDataMap;
import io.sparkled.persistence.PersistenceQuery;

import javax.persistence.EntityManager;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

public class SaveRenderedStagePropQuery implements PersistenceQuery<Integer> {

    private static final Logger logger = Logger.getLogger(SaveRenderedStagePropQuery.class.getName());

    private final Sequence sequence;
    private final RenderedStagePropDataMap renderedStagePropDataMap;

    public SaveRenderedStagePropQuery(Sequence sequence, RenderedStagePropDataMap renderedStagePropDataMap) {
        this.sequence = sequence;
        this.renderedStagePropDataMap = renderedStagePropDataMap;
    }

    @Override
    public Integer perform(EntityManager entityManager) {
        AtomicInteger recordsSaved = new AtomicInteger(0);
        renderedStagePropDataMap.forEach((key, value) -> {
            RenderedStageProp renderedStageProp = new RenderedStageProp();
            renderedStageProp.setSequenceId(sequence.getId()).setStagePropCode(key).setLedCount(value.getLedCount()).setData(value.getData());
            entityManager.merge(renderedStageProp);
            recordsSaved.incrementAndGet();
        });

        String className = RenderedStageProp.class.getSimpleName();
        logger.info("Saved " + recordsSaved.get() + " " + className + " record(s) for sequence " + sequence.getId());
        return recordsSaved.get();
    }
}
