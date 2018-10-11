package io.sparkled.persistence.sequence.impl.query;

import io.sparkled.model.entity.RenderedStageProp;
import io.sparkled.model.entity.Sequence;
import io.sparkled.model.render.RenderedStagePropDataMap;
import io.sparkled.persistence.PersistenceQuery;
import io.sparkled.persistence.QueryFactory;
import io.sparkled.persistence.stage.impl.query.DeleteRenderedStagePropsQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import java.util.*;
import java.util.stream.Collectors;

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

        // The stage prop IDs must be set, or new records will be created instead of updating existing records.
        Map<UUID, Integer> renderedStagePropIds = getRenderedStagePropIds(queryFactory);

        renderedStagePropDataMap.forEach((key, value) -> {
            RenderedStageProp renderedStageProp = new RenderedStageProp()
                    .setId(renderedStagePropIds.get(key))
                    .setSequenceId(sequence.getId())
                    .setStagePropUuid(key)
                    .setLedCount(value.getLedCount())
                    .setData(value.getData());
            entityManager.merge(renderedStageProp);
        });

        logger.info("Saved {} rendered stage prop(s) for sequence {}.", renderedStagePropDataMap.size(), sequence.getId());

        deleteRemovedRenderedStageProps(queryFactory, sequence, renderedStagePropDataMap.keySet());
        return null;
    }

    private Map<UUID, Integer> getRenderedStagePropIds(QueryFactory queryFactory) {
        Set<UUID> stagePropUuids = renderedStagePropDataMap.keySet();
        return queryFactory
                .select(qRenderedStageProp.stagePropUuid, qRenderedStageProp.id)
                .from(qRenderedStageProp)
                .where(qRenderedStageProp.stagePropUuid.in(stagePropUuids))
                .fetch()
                .stream()
                .collect(Collectors.toMap(
                        tuple -> tuple.get(0, UUID.class),
                        tuple -> tuple.get(1, Integer.class))
                );
    }

    private void deleteRemovedRenderedStageProps(QueryFactory queryFactory, Sequence sequence, Collection<UUID> uuidsToKeep) {
        uuidsToKeep = uuidsToKeep.isEmpty() ? noUuids : uuidsToKeep;
        List<Integer> idsToDelete = queryFactory
                .select(qRenderedStageProp.id)
                .from(qRenderedStageProp)
                .where(qRenderedStageProp.sequenceId.eq(sequence.getId()).and(qRenderedStageProp.stagePropUuid.notIn(uuidsToKeep)))
                .fetch();

        new DeleteRenderedStagePropsQuery(idsToDelete).perform(queryFactory);
    }
}
