package io.sparkled.persistence.sequence.impl.query

import io.sparkled.model.entity.RenderedStageProp
import io.sparkled.model.entity.Sequence
import io.sparkled.model.render.RenderedStagePropDataMap
import io.sparkled.model.util.TupleUtils
import io.sparkled.persistence.PersistenceQuery
import io.sparkled.persistence.QueryFactory
import io.sparkled.persistence.stage.impl.query.DeleteRenderedStagePropsQuery
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import javax.persistence.EntityManager
import java.util.*

import java.util.stream.Collectors.toMap

class SaveRenderedStagePropsQuery(private val sequence: Sequence, private val renderedStagePropDataMap: RenderedStagePropDataMap) : PersistenceQuery<Void> {

    @Override
    fun perform(queryFactory: QueryFactory): Void? {
        val entityManager = queryFactory.getEntityManager()

        // The stage prop IDs must be set, or new records will be created instead of updating existing records.
        val renderedStagePropIds = getRenderedStagePropIds(queryFactory)

        renderedStagePropDataMap.forEach({ key, value ->
            val renderedStageProp = RenderedStageProp()
                    .setId(renderedStagePropIds[key])
                    .setSequenceId(sequence.getId())
                    .setStagePropUuid(key)
                    .setLedCount(value.getLedCount())
                    .setData(value.getData())
            entityManager.merge(renderedStageProp)
        })

        logger.info("Saved {} rendered stage prop(s) for sequence {}.", renderedStagePropDataMap.size(), sequence.getId())

        deleteRemovedRenderedStageProps(queryFactory, sequence, renderedStagePropDataMap.keySet())
        return null
    }

    private fun getRenderedStagePropIds(queryFactory: QueryFactory): Map<UUID, Integer> {
        val stagePropUuids = renderedStagePropDataMap.keySet()
        return queryFactory
                .select(qRenderedStageProp.stagePropUuid, qRenderedStageProp.id)
                .from(qRenderedStageProp)
                .where(qRenderedStageProp.stagePropUuid.`in`(stagePropUuids))
                .fetch()
                .stream()
                .collect(toMap(
                        { tuple -> tuple.get(0, UUID::class.java) }
                ) { tuple -> TupleUtils.getInt(tuple, 1) }
                )
    }

    private fun deleteRemovedRenderedStageProps(queryFactory: QueryFactory, sequence: Sequence, uuidsToKeep: Collection<UUID>) {
        var uuidsToKeep = uuidsToKeep
        uuidsToKeep = if (uuidsToKeep.isEmpty()) noUuids else uuidsToKeep
        val idsToDelete = queryFactory
                .select(qRenderedStageProp.id)
                .from(qRenderedStageProp)
                .where(qRenderedStageProp.sequenceId.eq(sequence.getId()).and(qRenderedStageProp.stagePropUuid.notIn(uuidsToKeep)))
                .fetch()

        DeleteRenderedStagePropsQuery(idsToDelete).perform(queryFactory)
    }

    companion object {

        private val logger = LoggerFactory.getLogger(SaveRenderedStagePropsQuery::class.java)
    }
}
