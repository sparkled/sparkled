package io.sparkled.persistence.stage.impl.query

import io.sparkled.persistence.PersistenceQuery
import io.sparkled.persistence.PersistenceQuery.Companion.noIds
import io.sparkled.persistence.PersistenceQuery.Companion.qRenderedStageProp
import io.sparkled.persistence.QueryFactory
import org.slf4j.LoggerFactory

class DeleteRenderedStagePropsQuery(renderedStagePropIds: Collection<Int>) : PersistenceQuery<Unit> {

    private val renderedStagePropIds: Collection<Int>

    init {
        this.renderedStagePropIds = if (renderedStagePropIds.isEmpty()) noIds else renderedStagePropIds
    }

    override fun perform(queryFactory: QueryFactory) {
        val deleted = queryFactory
                .delete(qRenderedStageProp)
                .where(qRenderedStageProp.id.`in`(renderedStagePropIds))
                .execute()

        logger.info("Deleted {} rendered stage prop(s).", deleted)
    }

    companion object {
        private val logger = LoggerFactory.getLogger(DeleteRenderedStagePropsQuery::class.java)
    }
}
