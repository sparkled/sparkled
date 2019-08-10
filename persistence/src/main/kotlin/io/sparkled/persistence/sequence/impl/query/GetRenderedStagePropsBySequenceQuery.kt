package io.sparkled.persistence.sequence.impl.query

import io.sparkled.model.entity.QRenderedStageProp.Companion.renderedStageProp
import io.sparkled.model.entity.RenderedStageProp
import io.sparkled.model.entity.Sequence
import io.sparkled.model.entity.Song
import io.sparkled.model.render.RenderedStagePropData
import io.sparkled.model.render.RenderedStagePropDataMap
import io.sparkled.model.util.SequenceUtils
import io.sparkled.persistence.PersistenceQuery
import io.sparkled.persistence.QueryFactory

class GetRenderedStagePropsBySequenceQuery(private val sequence: Sequence, private val song: Song) :
    PersistenceQuery<RenderedStagePropDataMap> {

    override fun perform(queryFactory: QueryFactory): RenderedStagePropDataMap {
        val renderedStageProps = queryFactory
            .selectFrom(renderedStageProp)
            .where(renderedStageProp.sequenceId.eq(sequence.getId()))
            .fetch()

        val renderedStagePropDataMap = RenderedStagePropDataMap()
        renderedStageProps.forEach { stagePropData -> addToMap(renderedStagePropDataMap, stagePropData) }
        return renderedStagePropDataMap
    }

    private fun addToMap(renderedStagePropDataMap: RenderedStagePropDataMap, stagePropData: RenderedStageProp) {
        val renderedStagePropData = RenderedStagePropData(
            0,
            SequenceUtils.getFrameCount(song, sequence) - 1,
            stagePropData.getLedCount()!!,
            stagePropData.getData()!!
        )

        renderedStagePropDataMap[stagePropData.getStagePropUuid()!!] = renderedStagePropData
    }
}
