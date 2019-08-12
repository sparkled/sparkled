package io.sparkled.persistence.stage.impl.query

import io.sparkled.model.entity.QRenderedStageProp.Companion.renderedStageProp
import io.sparkled.model.util.IdUtils.NO_IDS
import io.sparkled.persistence.PersistenceQuery
import io.sparkled.persistence.QueryFactory
import org.slf4j.LoggerFactory

class DeleteRenderedStagePropsQuery(renderedStagePropIds: Collection<Int>) : PersistenceQuery<Unit> {

    private val renderedStagePropIds: Collection<Int> =
        if (renderedStagePropIds.isEmpty()) NO_IDS else renderedStagePropIds

    override fun perform(queryFactory: QueryFactory) {
        val deleted = queryFactory
            .delete(renderedStageProp)
            .where(renderedStageProp.id.`in`(renderedStagePropIds))
            .execute()

        logger.info("Deleted {} rendered stage prop(s).", deleted)
    }

    companion object {
        private val logger = LoggerFactory.getLogger(DeleteRenderedStagePropsQuery::class.java)
    }
}
