package io.sparkled.persistence

import io.sparkled.model.render.RenderedSequence

interface FileService {
    fun init()
    fun readSongAudio(songId: Int): ByteArray
    fun writeSongAudio(songId: Int, audio: ByteArray)
    fun deleteSongAudio(songId: Int)
    fun readRender(sequenceId: Int): RenderedSequence
    fun writeRender(sequenceId: Int, render: RenderedSequence)
    fun deleteRender(sequenceId: Int)
}
