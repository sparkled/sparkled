package io.sparkled.persistence.stage.impl.query

import io.sparkled.model.entity.Stage
import io.sparkled.model.validator.StageValidator
import io.sparkled.persistence.PersistenceQuery
import io.sparkled.persistence.QueryFactory
import org.slf4j.LoggerFactory

class SaveStageQuery(private val stage: Stage) : PersistenceQuery<Stage> {

    override fun perform(queryFactory: QueryFactory): Stage {
        StageValidator().validate(stage)

        val entityManager = queryFactory.entityManager
        val savedStage = entityManager.merge(stage)

        logger.info("Saved stage {} ({}).", savedStage.getId(), savedStage.getName())
        return savedStage
    }

    companion object {
        private val logger = LoggerFactory.getLogger(SaveStageQuery::class.java)
    }
}
