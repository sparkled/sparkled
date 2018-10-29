package io.sparkled.persistence.sequence

import io.sparkled.model.entity.Sequence
import io.sparkled.model.entity.SequenceChannel
import io.sparkled.model.entity.Song
import io.sparkled.model.entity.SongAudio
import io.sparkled.model.entity.Stage
import io.sparkled.model.render.RenderedStagePropDataMap
import java.util.Optional
import java.util.UUID

interface SequencePersistenceService {

    fun createSequence(sequence: Sequence): Sequence

    fun getAllSequences(): List<Sequence>

    fun getSequenceById(sequenceId: Int): Optional<Sequence>

    fun getStageBySequenceId(sequenceId: Int): Optional<Stage>

    fun getSongAudioBySequenceId(sequenceId: Int): Optional<SongAudio>

    fun getSequenceChannelsBySequenceId(sequenceId: Int): List<SequenceChannel>

    fun getSequenceChannelByUuid(sequenceId: Int, uuid: UUID): Optional<SequenceChannel>

    fun getRenderedStagePropsBySequenceAndSong(sequence: Sequence, song: Song): RenderedStagePropDataMap

    fun getSequenceStagePropUuidMapBySequenceId(sequenceId: Int): Map<String, UUID>

    fun saveSequence(sequence: Sequence, sequenceChannels: List<SequenceChannel>)

    fun publishSequence(
        sequence: Sequence,
        sequenceChannels: List<SequenceChannel>,
        renderedStageProps: RenderedStagePropDataMap
    )

    fun deleteSequence(sequenceId: Int)
}
