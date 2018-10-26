package io.sparkled.persistence.sequence.impl.query

import io.sparkled.model.entity.Sequence
import io.sparkled.model.entity.SequenceChannel
import io.sparkled.model.validator.SequenceChannelValidator
import io.sparkled.model.validator.exception.EntityValidationException
import io.sparkled.persistence.PersistenceQuery
import io.sparkled.persistence.PersistenceQuery.Companion.noUuids
import io.sparkled.persistence.PersistenceQuery.Companion.qSequenceChannel
import io.sparkled.persistence.QueryFactory
import org.slf4j.LoggerFactory
import java.util.*

class SaveSequenceChannelsQuery(private val sequence: Sequence, private val sequenceChannels: List<SequenceChannel>) : PersistenceQuery<Unit> {

    override fun perform(queryFactory: QueryFactory) {
        val sequenceChannelValidator = SequenceChannelValidator()
        sequenceChannels.forEach { sc -> sc.setSequenceId(sequence.getId()!!) }
        sequenceChannels.forEach(sequenceChannelValidator::validate)

        if (uuidAlreadyInUse(queryFactory)) {
            throw EntityValidationException("Sequence channel already exists on another sequence.")
        } else {
            val entityManager = queryFactory.entityManager
            sequenceChannels.forEach { entityManager.merge(it) }
            logger.info("Saved {} sequence channel(s) for sequence {}.", sequenceChannels.size, sequence.getId())

            deleteRemovedSequenceChannels(queryFactory)
        }
    }

    private fun uuidAlreadyInUse(queryFactory: QueryFactory): Boolean {
        var uuidsToCheck = sequenceChannels.asSequence().map(SequenceChannel::getUuid).toList()
        uuidsToCheck = if (uuidsToCheck.isEmpty()) noUuids else uuidsToCheck

        val uuidsInUse = queryFactory.select(qSequenceChannel)
                .from(qSequenceChannel)
                .where(qSequenceChannel.sequenceId.ne(sequence.getId()).and(qSequenceChannel.uuid.`in`(uuidsToCheck)))
                .fetchCount()
        return uuidsInUse > 0
    }

    private fun deleteRemovedSequenceChannels(queryFactory: QueryFactory) {
        val uuidsToDelete = getSequenceChannelUuidsToDelete(queryFactory)
        DeleteSequenceChannelsQuery(uuidsToDelete).perform(queryFactory)
    }

    private fun getSequenceChannelUuidsToDelete(queryFactory: QueryFactory): List<UUID> {
        var uuidsToKeep = sequenceChannels.asSequence().map(SequenceChannel::getUuid).toList()
        uuidsToKeep = if (uuidsToKeep.isEmpty()) noUuids else uuidsToKeep

        return queryFactory
                .select(qSequenceChannel.uuid)
                .from(qSequenceChannel)
                .where(qSequenceChannel.sequenceId.eq(sequence.getId()).and(qSequenceChannel.uuid.notIn(uuidsToKeep)))
                .fetch()
    }

    companion object {
        private val logger = LoggerFactory.getLogger(SaveSequenceChannelsQuery::class.java)
    }
}
