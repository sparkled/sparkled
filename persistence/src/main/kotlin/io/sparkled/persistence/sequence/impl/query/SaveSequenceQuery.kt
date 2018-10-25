package io.sparkled.persistence.sequence.impl.query

import io.sparkled.model.entity.Sequence
import io.sparkled.model.validator.SequenceValidator
import io.sparkled.persistence.PersistenceQuery
import io.sparkled.persistence.QueryFactory
import org.slf4j.LoggerFactory

class SaveSequenceQuery(private val sequence: Sequence) : PersistenceQuery<Sequence> {

    override fun perform(queryFactory: QueryFactory): Sequence {
        SequenceValidator().validate(sequence)

        val entityManager = queryFactory.entityManager
        val savedSequence = entityManager.merge(sequence)

        logger.info("Saved sequence {} ({}).", savedSequence.getId(), savedSequence.getName())
        return savedSequence
    }

    companion object {
        private val logger = LoggerFactory.getLogger(SaveSequenceQuery::class.java)
    }
}
