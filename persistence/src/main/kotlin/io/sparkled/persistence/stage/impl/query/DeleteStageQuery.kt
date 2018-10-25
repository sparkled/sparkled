package io.sparkled.persistence.stage.impl.query

import io.sparkled.persistence.PersistenceQuery
import io.sparkled.persistence.PersistenceQuery.Companion.qSequence
import io.sparkled.persistence.PersistenceQuery.Companion.qStage
import io.sparkled.persistence.PersistenceQuery.Companion.qStageProp
import io.sparkled.persistence.QueryFactory
import io.sparkled.persistence.sequence.impl.query.DeleteSequencesQuery
import org.slf4j.LoggerFactory

class DeleteStageQuery(private val stageId: Int) : PersistenceQuery<Unit> {

    override fun perform(queryFactory: QueryFactory) {
        deleteSequences(queryFactory)
        deleteStageProps(queryFactory)
        deleteStage(queryFactory)
    }

    private fun deleteSequences(queryFactory: QueryFactory) {
        val sequenceIds = queryFactory
                .select(qSequence.id)
                .from(qSequence)
                .where(qSequence.stageId.eq(stageId))
                .fetch()
        DeleteSequencesQuery(sequenceIds).perform(queryFactory)
    }

    private fun deleteStageProps(queryFactory: QueryFactory) {
        val stagePropUuids = queryFactory
                .select(qStageProp.uuid)
                .from(qStageProp)
                .where(qStageProp.stageId.eq(stageId))
                .fetch()
        DeleteStagePropsQuery(stagePropUuids).perform(queryFactory)
    }

    private fun deleteStage(queryFactory: QueryFactory) {
        val deleted = queryFactory
                .delete(qStage)
                .where(qStage.id.eq(stageId))
                .execute()

        logger.info("Deleted {} stage(s).", deleted)
    }

    companion object {

        private val logger = LoggerFactory.getLogger(DeleteStageQuery::class.java)
    }
}
