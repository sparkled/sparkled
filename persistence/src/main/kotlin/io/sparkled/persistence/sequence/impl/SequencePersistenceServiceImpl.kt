package io.sparkled.persistence.sequence.impl

import com.fasterxml.jackson.databind.ObjectMapper
import io.sparkled.model.entity.Sequence
import io.sparkled.model.entity.SequenceChannel
import io.sparkled.model.entity.Song
import io.sparkled.model.entity.SongAudio
import io.sparkled.model.entity.Stage
import io.sparkled.model.render.RenderedStagePropDataMap
import io.sparkled.persistence.QueryFactory
import io.sparkled.persistence.sequence.SequencePersistenceService
import io.sparkled.persistence.sequence.impl.query.DeleteSequencesQuery
import io.sparkled.persistence.sequence.impl.query.GetAllSequencesQuery
import io.sparkled.persistence.sequence.impl.query.GetRenderedStagePropsBySequenceQuery
import io.sparkled.persistence.sequence.impl.query.GetSequenceByIdQuery
import io.sparkled.persistence.sequence.impl.query.GetSequenceChannelByUuidQuery
import io.sparkled.persistence.sequence.impl.query.GetSequenceChannelsBySequenceIdQuery
import io.sparkled.persistence.sequence.impl.query.GetSongAudioBySequenceIdQuery
import io.sparkled.persistence.sequence.impl.query.GetStageBySequenceIdQuery
import io.sparkled.persistence.sequence.impl.query.SaveRenderedStagePropsQuery
import io.sparkled.persistence.sequence.impl.query.SaveSequenceChannelsQuery
import io.sparkled.persistence.sequence.impl.query.SaveSequenceQuery
import java.util.UUID
import javax.inject.Singleton

@Singleton
class SequencePersistenceServiceImpl(
    private val queryFactory: QueryFactory,
    private val objectMapper: ObjectMapper
) : SequencePersistenceService {

    override fun getAllSequences(): List<Sequence> {
        return GetAllSequencesQuery().perform(queryFactory)
    }

    override fun getSequenceById(sequenceId: Int): Sequence? {
        return GetSequenceByIdQuery(sequenceId).perform(queryFactory)
    }

    override fun getStageBySequenceId(sequenceId: Int): Stage? {
        return GetStageBySequenceIdQuery(sequenceId).perform(queryFactory)
    }

    override fun getSongAudioBySequenceId(sequenceId: Int): SongAudio? {
        return GetSongAudioBySequenceIdQuery(sequenceId).perform(queryFactory)
    }

    override fun getSequenceChannelsBySequenceId(sequenceId: Int): List<SequenceChannel> {
        return GetSequenceChannelsBySequenceIdQuery(sequenceId).perform(queryFactory)
    }

    override fun getSequenceChannelByUuid(sequenceId: Int, uuid: UUID): SequenceChannel? {
        return GetSequenceChannelByUuidQuery(sequenceId, uuid).perform(queryFactory)
    }

    override fun getRenderedStagePropsBySequenceAndSong(sequence: Sequence, song: Song): RenderedStagePropDataMap {
        return GetRenderedStagePropsBySequenceQuery(sequence, song).perform(queryFactory)
    }

    override fun saveSequence(sequence: Sequence, sequenceChannels: List<SequenceChannel>): Sequence {
        val savedSequence = SaveSequenceQuery(sequence).perform(queryFactory)
        SaveSequenceChannelsQuery(savedSequence, sequenceChannels, objectMapper).perform(queryFactory)
        return savedSequence
    }

    override fun publishSequence(sequence: Sequence, channels: List<SequenceChannel>, data: RenderedStagePropDataMap) {
        val savedSequence = SaveSequenceQuery(sequence).perform(queryFactory)
        SaveSequenceChannelsQuery(savedSequence, channels, objectMapper).perform(queryFactory)
        SaveRenderedStagePropsQuery(savedSequence, data).perform(queryFactory)
    }

    override fun deleteSequence(sequenceId: Int) {
        DeleteSequencesQuery(listOf(sequenceId)).perform(queryFactory)
    }
}
