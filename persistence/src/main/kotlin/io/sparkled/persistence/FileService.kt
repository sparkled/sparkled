package io.sparkled.persistence

import io.sparkled.model.UniqueId
import io.sparkled.model.render.RenderedSequence

interface FileService {
    fun init()
    fun readSongAudio(songId: UniqueId): ByteArray
    fun writeSongAudio(songId: UniqueId, audio: ByteArray)
    fun deleteSongAudio(songId: UniqueId)
    fun readRender(sequenceId: UniqueId): RenderedSequence
    fun writeRender(sequenceId: UniqueId, render: RenderedSequence)
    fun deleteRender(sequenceId: UniqueId)
}
