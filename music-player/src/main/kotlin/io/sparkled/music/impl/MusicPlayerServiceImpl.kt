package io.sparkled.music.impl

import io.sparkled.music.MusicPlayerService
import io.sparkled.music.PlaybackState
import javazoom.jl.player.advanced.PlaybackEvent
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import javax.inject.Inject
import javax.sound.sampled.*
import java.io.ByteArrayInputStream
import java.io.IOException
import java.io.InputStream
import java.util.HashSet
import java.util.function.Consumer

class MusicPlayerServiceImpl @Inject
constructor() : MusicPlayerService, LineListener {

    private val listeners = HashSet<LineListener>()
    private var clip: Clip? = null

    fun play(playbackState: PlaybackState) {
        stopPlayback()

        var byteStream: InputStream? = null
        var mp3Stream: AudioInputStream? = null
        var convertedStream: AudioInputStream? = null

        try {
            logger.info("Playing sequence {}.", playbackState.sequence.getName())

            byteStream = ByteArrayInputStream(playbackState.songAudio.getAudioData())
            mp3Stream = AudioSystem.getAudioInputStream(byteStream)

            val baseFormat = mp3Stream!!.format
            val decodedFormat = AudioFormat(AudioFormat.Encoding.PCM_SIGNED, baseFormat.sampleRate, 16, baseFormat.channels,
                    baseFormat.channels * 2, baseFormat.sampleRate, false)
            convertedStream = AudioSystem.getAudioInputStream(decodedFormat, mp3Stream)

            clip = AudioSystem.getClip()
            clip!!.open(convertedStream)
            clip!!.addLineListener(this)
            clip!!.start()
            logger.info("Sequence finished playing.")
        } catch (e: Exception) {
            logger.error("Failed to play sequence {}: {}.", playbackState.sequence.getName(), e.message)
        } finally {
            close(byteStream, mp3Stream, convertedStream)
        }
    }

    private fun close(vararg streams: InputStream) {
        for (i in streams.indices) {
            val stream = streams[i]
            if (stream != null) {
                try {
                    stream.close()
                } catch (e: IOException) {
                    logger.error("Failed to close stream #{}: {}.", i, e.message)
                }

            }
        }
    }

    override fun addLineListener(listener: LineListener) {
        this.listeners.add(listener)
        logger.info("Added line listener: {}.", listener)
    }

    override val sequenceProgress: Double
        get() {
            if (clip == null) {
                return 0.0
            } else {
                return Math.min(1.0, clip!!.framePosition / clip!!.frameLength.toDouble())
            }
        }

    override fun stopPlayback() {
        if (clip != null && clip!!.isOpen) {
            try {
                clip!!.close()
                logger.info("Clip closed.")
            } catch (e: Exception) {
                logger.error("Failed to close clip.")
            }

        } else {
            logger.info("Clip is already closed.")
        }

        clip = null
    }

    override fun update(event: LineEvent) {
        listeners.forEach { l -> l.update(event) }
    }

    companion object {

        private val logger = LoggerFactory.getLogger(MusicPlayerServiceImpl::class.java!!)
    }
}
