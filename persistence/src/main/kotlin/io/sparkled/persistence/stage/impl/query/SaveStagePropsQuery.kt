package io.sparkled.persistence.stage.impl.query

import io.sparkled.model.entity.QStageProp.stageProp
import io.sparkled.model.entity.Stage
import io.sparkled.model.entity.StageProp
import io.sparkled.model.validator.StagePropValidator
import io.sparkled.model.validator.exception.EntityValidationException
import io.sparkled.persistence.PersistenceQuery
import io.sparkled.persistence.PersistenceQuery.Companion.noUuids
import io.sparkled.persistence.QueryFactory
import org.slf4j.LoggerFactory
import java.util.UUID

class SaveStagePropsQuery(private val stage: Stage, private val stageProps: List<StageProp>) : PersistenceQuery<Unit> {

    override fun perform(queryFactory: QueryFactory) {
        val stagePropValidator = StagePropValidator()
        stageProps.forEach { sp -> sp.setStageId(stage.getId()!!) }
        stagePropValidator.validate(stageProps)

        if (uuidAlreadyInUse(queryFactory)) {
            throw EntityValidationException("Stage prop already exists on another stage.")
        } else {
            val entityManager = queryFactory.entityManager
            stageProps.forEach { entityManager.merge(it) }
            logger.info("Saved {} stage prop(s) for stage {}.", stageProps.size, stage.getId())

            deleteRemovedStageProps(queryFactory)
        }
    }

    private fun uuidAlreadyInUse(queryFactory: QueryFactory): Boolean {
        var uuidsToCheck = stageProps.asSequence().map(StageProp::getUuid).toList()
        uuidsToCheck = if (uuidsToCheck.isEmpty()) noUuids else uuidsToCheck

        val uuidsInUse = queryFactory.select(stageProp)
            .from(stageProp)
            .where(stageProp.stageId.ne(stage.getId()).and(stageProp.uuid.`in`(uuidsToCheck)))
            .fetchCount()
        return uuidsInUse > 0
    }

    private fun deleteRemovedStageProps(queryFactory: QueryFactory) {
        val uuidsToDelete = getStagePropUuidsToDelete(queryFactory)
        DeleteStagePropsQuery(uuidsToDelete).perform(queryFactory)
    }

    private fun getStagePropUuidsToDelete(queryFactory: QueryFactory): List<UUID> {
        var uuidsToKeep = stageProps.asSequence().map(StageProp::getUuid).toList()
        uuidsToKeep = if (uuidsToKeep.isEmpty()) noUuids else uuidsToKeep

        return queryFactory
            .select(stageProp.uuid)
            .from(stageProp)
            .where(stageProp.stageId.eq(stage.getId()).and(stageProp.uuid.notIn(uuidsToKeep)))
            .fetch()
    }

    companion object {
        private val logger = LoggerFactory.getLogger(SaveStagePropsQuery::class.java)
    }
}
