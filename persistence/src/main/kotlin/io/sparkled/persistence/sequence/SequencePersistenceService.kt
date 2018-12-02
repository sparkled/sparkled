package io.sparkled.persistence.sequence

import io.sparkled.model.entity.Sequence
import io.sparkled.model.entity.SequenceChannel
import io.sparkled.model.entity.Song
import io.sparkled.model.entity.SongAudio
import io.sparkled.model.entity.Stage
import io.sparkled.model.render.RenderedStagePropDataMap
import java.util.UUID

interface SequencePersistenceService {

    fun getAllSequences(): List<Sequence>

    fun getSequenceById(sequenceId: Int): Sequence?

    fun getStageBySequenceId(sequenceId: Int): Stage?

    fun getSongAudioBySequenceId(sequenceId: Int): SongAudio?

    fun getSequenceChannelsBySequenceId(sequenceId: Int): List<SequenceChannel>

    fun getSequenceChannelByUuid(sequenceId: Int, uuid: UUID): SequenceChannel?

    fun getRenderedStagePropsBySequenceAndSong(sequence: Sequence, song: Song): RenderedStagePropDataMap

    fun getSequenceStagePropUuidMapBySequenceId(sequenceId: Int): Map<String, UUID>

    fun saveSequence(sequence: Sequence, sequenceChannels: List<SequenceChannel>): Sequence

    fun publishSequence(
        sequence: Sequence,
        sequenceChannels: List<SequenceChannel>,
        renderedStageProps: RenderedStagePropDataMap
    )

    fun deleteSequence(sequenceId: Int)
}
