package io.sparkled.persistence.sequence.impl

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
import io.sparkled.persistence.sequence.impl.query.GetSequenceStagePropUuidMapBySequenceIdQuery
import io.sparkled.persistence.sequence.impl.query.GetSongAudioBySequenceIdQuery
import io.sparkled.persistence.sequence.impl.query.GetStageBySequenceIdQuery
import io.sparkled.persistence.sequence.impl.query.SaveRenderedStagePropsQuery
import io.sparkled.persistence.sequence.impl.query.SaveSequenceChannelsQuery
import io.sparkled.persistence.sequence.impl.query.SaveSequenceQuery
import java.util.Collections
import java.util.Optional
import java.util.UUID
import javax.inject.Inject

class SequencePersistenceServiceImpl @Inject
constructor(private val queryFactory: QueryFactory) : SequencePersistenceService {

    override fun createSequence(sequence: Sequence): Sequence {
        return SaveSequenceQuery(sequence).perform(queryFactory)
    }

    override fun getAllSequences(): List<Sequence> {
        return GetAllSequencesQuery().perform(queryFactory)
    }

    override fun getSequenceById(sequenceId: Int): Optional<Sequence> {
        return GetSequenceByIdQuery(sequenceId).perform(queryFactory)
    }

    override fun getStageBySequenceId(sequenceId: Int): Optional<Stage> {
        return GetStageBySequenceIdQuery(sequenceId).perform(queryFactory)
    }

    override fun getSongAudioBySequenceId(sequenceId: Int): Optional<SongAudio> {
        return GetSongAudioBySequenceIdQuery(sequenceId).perform(queryFactory)
    }

    override fun getSequenceChannelsBySequenceId(sequenceId: Int): List<SequenceChannel> {
        return GetSequenceChannelsBySequenceIdQuery(sequenceId).perform(queryFactory)
    }

    override fun getSequenceChannelByUuid(sequenceId: Int, uuid: UUID): Optional<SequenceChannel> {
        return GetSequenceChannelByUuidQuery(sequenceId, uuid).perform(queryFactory)
    }

    override fun getRenderedStagePropsBySequenceAndSong(sequence: Sequence, song: Song): RenderedStagePropDataMap {
        return GetRenderedStagePropsBySequenceQuery(sequence, song).perform(queryFactory)
    }

    override fun getSequenceStagePropUuidMapBySequenceId(sequenceId: Int): Map<String, UUID> {
        return GetSequenceStagePropUuidMapBySequenceIdQuery(sequenceId).perform(queryFactory)
    }

    override fun saveSequence(sequence: Sequence, sequenceChannels: List<SequenceChannel>) {
        val savedSequence = SaveSequenceQuery(sequence).perform(queryFactory)
        SaveSequenceChannelsQuery(savedSequence, sequenceChannels).perform(queryFactory)
    }

    override fun publishSequence(sequence: Sequence, sequenceChannels: List<SequenceChannel>, renderedStageProps: RenderedStagePropDataMap) {
        val savedSequence = SaveSequenceQuery(sequence).perform(queryFactory)
        SaveSequenceChannelsQuery(savedSequence, sequenceChannels).perform(queryFactory)
        SaveRenderedStagePropsQuery(savedSequence, renderedStageProps).perform(queryFactory)
    }

    override fun deleteSequence(sequenceId: Int) {
        DeleteSequencesQuery(Collections.singletonList(sequenceId)).perform(queryFactory)
    }
}
