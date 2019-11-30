package io.sparkled.persistence.sequence.impl.query

import io.sparkled.model.entity.QSequenceChannel.Companion.sequenceChannel
import io.sparkled.model.entity.Sequence
import io.sparkled.model.entity.SequenceChannel
import io.sparkled.model.util.IdUtils.NO_UUIDS
import io.sparkled.model.validator.SequenceChannelValidator
import io.sparkled.model.validator.exception.EntityValidationException
import io.sparkled.persistence.PersistenceQuery
import io.sparkled.persistence.QueryFactory
import java.util.UUID
import org.slf4j.LoggerFactory

class SaveSequenceChannelsQuery(
    private val sequence: Sequence,
    private val sequenceChannels: List<SequenceChannel>
) : PersistenceQuery<Unit> {

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
        uuidsToCheck = if (uuidsToCheck.isEmpty()) NO_UUIDS else uuidsToCheck

        val uuidsInUse = queryFactory.select(sequenceChannel)
            .from(sequenceChannel)
            .where(sequenceChannel.sequenceId.ne(sequence.getId()).and(sequenceChannel.uuid.`in`(uuidsToCheck)))
            .fetchCount()
        return uuidsInUse > 0
    }

    private fun deleteRemovedSequenceChannels(queryFactory: QueryFactory) {
        val uuidsToDelete = getSequenceChannelUuidsToDelete(queryFactory)
        DeleteSequenceChannelsQuery(uuidsToDelete).perform(queryFactory)
    }

    private fun getSequenceChannelUuidsToDelete(queryFactory: QueryFactory): List<UUID> {
        var uuidsToKeep = sequenceChannels.asSequence().map(SequenceChannel::getUuid).toList()
        uuidsToKeep = if (uuidsToKeep.isEmpty()) NO_UUIDS else uuidsToKeep

        return queryFactory
            .select(sequenceChannel.uuid)
            .from(sequenceChannel)
            .where(sequenceChannel.sequenceId.eq(sequence.getId()).and(sequenceChannel.uuid.notIn(uuidsToKeep)))
            .fetch()
    }

    companion object {
        private val logger = LoggerFactory.getLogger(SaveSequenceChannelsQuery::class.java)
    }
}
