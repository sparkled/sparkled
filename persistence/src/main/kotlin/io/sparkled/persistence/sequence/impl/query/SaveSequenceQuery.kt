package io.sparkled.persistence.sequence.impl.query

import io.sparkled.model.entity.Sequence
import io.sparkled.model.validator.SequenceValidator
import io.sparkled.persistence.PersistenceQuery
import io.sparkled.persistence.QueryFactory
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import javax.persistence.EntityManager

class SaveSequenceQuery(private val sequence: Sequence) : PersistenceQuery<Sequence> {

    @Override
    fun perform(queryFactory: QueryFactory): Sequence {
        SequenceValidator().validate(sequence)

        val entityManager = queryFactory.getEntityManager()
        val savedSequence = entityManager.merge(sequence)

        logger.info("Saved sequence {} ({}).", savedSequence.getId(), savedSequence.getName())
        return savedSequence
    }

    companion object {

        private val logger = LoggerFactory.getLogger(SaveSequenceQuery::class.java)
    }
}
