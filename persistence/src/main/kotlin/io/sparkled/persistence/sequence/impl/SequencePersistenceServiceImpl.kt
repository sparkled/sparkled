package io.sparkled.persistence.sequence.impl

import io.sparkled.model.entity.*
import io.sparkled.model.render.RenderedStagePropDataMap
import io.sparkled.persistence.QueryFactory
import io.sparkled.persistence.sequence.SequencePersistenceService
import io.sparkled.persistence.sequence.impl.query.*

import javax.inject.Inject
import java.util.*

class SequencePersistenceServiceImpl @Inject
constructor(private val queryFactory: QueryFactory) : SequencePersistenceService {

    @Override
    fun createSequence(sequence: Sequence): Sequence {
        return SaveSequenceQuery(sequence).perform(queryFactory)
    }

    val allSequences: List<Sequence>
        @Override
        get() = GetAllSequencesQuery().perform(queryFactory)

    @Override
    fun getSequenceById(sequenceId: Int): Optional<Sequence> {
        return GetSequenceByIdQuery(sequenceId).perform(queryFactory)
    }

    @Override
    fun getStageBySequenceId(sequenceId: Int): Optional<Stage> {
        return GetStageBySequenceIdQuery(sequenceId).perform(queryFactory)
    }

    @Override
    fun getSongAudioBySequenceId(sequenceId: Int): Optional<SongAudio> {
        return GetSongAudioBySequenceIdQuery(sequenceId).perform(queryFactory)
    }

    @Override
    fun getSequenceChannelsBySequenceId(sequenceId: Int): List<SequenceChannel> {
        return GetSequenceChannelsBySequenceIdQuery(sequenceId).perform(queryFactory)
    }

    @Override
    fun getSequenceChannelByUuid(sequenceId: Int, uuid: UUID): Optional<SequenceChannel> {
        return GetSequenceChannelByUuidQuery(sequenceId, uuid).perform(queryFactory)
    }

    @Override
    fun getRenderedStagePropsBySequenceAndSong(sequence: Sequence, song: Song): RenderedStagePropDataMap {
        return GetRenderedStagePropsBySequenceQuery(sequence, song).perform(queryFactory)
    }

    @Override
    fun getSequenceStagePropUuidMapBySequenceId(sequenceId: Int): Map<String, UUID> {
        return GetSequenceStagePropUuidMapBySequenceIdQuery(sequenceId).perform(queryFactory)
    }

    @Override
    fun saveSequence(sequence: Sequence, sequenceChannels: List<SequenceChannel>) {
        var sequence = sequence
        sequence = SaveSequenceQuery(sequence).perform(queryFactory)
        SaveSequenceChannelsQuery(sequence, sequenceChannels).perform(queryFactory)
    }

    @Override
    fun publishSequence(sequence: Sequence, sequenceChannels: List<SequenceChannel>, renderedStageProps: RenderedStagePropDataMap) {
        var sequence = sequence
        sequence = SaveSequenceQuery(sequence).perform(queryFactory)
        SaveSequenceChannelsQuery(sequence, sequenceChannels).perform(queryFactory)
        SaveRenderedStagePropsQuery(sequence, renderedStageProps).perform(queryFactory)
    }

    @Override
    fun deleteSequence(sequenceId: Int) {
        DeleteSequencesQuery(Collections.singletonList(sequenceId)).perform(queryFactory)
    }
}
