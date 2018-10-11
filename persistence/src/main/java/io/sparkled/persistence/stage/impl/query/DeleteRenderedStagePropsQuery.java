package io.sparkled.persistence.stage.impl.query;

import io.sparkled.persistence.PersistenceQuery;
import io.sparkled.persistence.QueryFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

public class DeleteRenderedStagePropsQuery implements PersistenceQuery<Void> {

    private static final Logger logger = LoggerFactory.getLogger(DeleteRenderedStagePropsQuery.class);

    private final Collection<Integer> renderedStagePropIds;

    public DeleteRenderedStagePropsQuery(Collection<Integer> renderedStagePropIds) {
        this.renderedStagePropIds = renderedStagePropIds.isEmpty() ? noIds : renderedStagePropIds;
    }

    @Override
    public Void perform(QueryFactory queryFactory) {
        long deleted = queryFactory
                .delete(qRenderedStageProp)
                .where(qRenderedStageProp.id.in(renderedStagePropIds))
                .execute();

        logger.info("Deleted {} rendered stage prop(s).", deleted);
        return null;
    }
}
