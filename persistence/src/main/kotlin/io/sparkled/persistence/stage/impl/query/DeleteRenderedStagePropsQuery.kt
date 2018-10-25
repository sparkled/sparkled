package io.sparkled.persistence.stage.impl.query

import io.sparkled.persistence.PersistenceQuery
import io.sparkled.persistence.QueryFactory
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class DeleteRenderedStagePropsQuery(renderedStagePropIds: Collection<Integer>) : PersistenceQuery<Void> {

    private val renderedStagePropIds: Collection<Integer>

    init {
        this.renderedStagePropIds = if (renderedStagePropIds.isEmpty()) noIds else renderedStagePropIds
    }

    @Override
    fun perform(queryFactory: QueryFactory): Void? {
        val deleted = queryFactory
                .delete(qRenderedStageProp)
                .where(qRenderedStageProp.id.`in`(renderedStagePropIds))
                .execute()

        logger.info("Deleted {} rendered stage prop(s).", deleted)
        return null
    }

    companion object {

        private val logger = LoggerFactory.getLogger(DeleteRenderedStagePropsQuery::class.java)
    }
}
