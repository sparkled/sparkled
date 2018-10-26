package io.sparkled.music.impl

import io.sparkled.music.MusicPlayerService
import io.sparkled.music.PlaybackState
import org.slf4j.LoggerFactory
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.util.HashSet
import javax.inject.Inject
import javax.sound.sampled.AudioFormat
import javax.sound.sampled.AudioInputStream
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.Clip
import javax.sound.sampled.LineEvent
import javax.sound.sampled.LineListener

class MusicPlayerServiceImpl @Inject
constructor() : MusicPlayerService, LineListener {

    private val listeners = HashSet<LineListener>()
    private var clip: Clip? = null

    override fun play(playbackState: PlaybackState) {
        stopPlayback()

        var byteStream: InputStream? = null
        var mp3Stream: AudioInputStream? = null
        var convertedStream: AudioInputStream? = null

        try {
            logger.info("Playing sequence {}.", playbackState.sequence?.getName())

            byteStream = ByteArrayInputStream(playbackState.songAudio?.getAudioData())
            mp3Stream = AudioSystem.getAudioInputStream(byteStream)

            val baseFormat = mp3Stream!!.format
            val decodedFormat = AudioFormat(AudioFormat.Encoding.PCM_SIGNED, baseFormat.sampleRate, 16, baseFormat.channels,
                    baseFormat.channels * 2, baseFormat.sampleRate, false)
            convertedStream = AudioSystem.getAudioInputStream(decodedFormat, mp3Stream)

            val clip = AudioSystem.getClip()
            this.clip = clip

            clip.open(convertedStream)
            clip.addLineListener(this)
            clip.start()
            logger.info("Sequence finished playing.")
        } catch (e: Exception) {
            logger.error("Failed to play sequence {}: {}.", playbackState.sequence?.getName(), e.message)
        } finally {
            byteStream?.close()
            mp3Stream?.close()
            convertedStream?.close()
        }
    }

    override fun addLineListener(listener: LineListener) {
        this.listeners.add(listener)
        logger.info("Added line listener: {}.", listener)
    }

    override val sequenceProgress: Double
        get() {
            return if (clip == null) {
                0.0
            } else {
                Math.min(1.0, clip!!.framePosition / clip!!.frameLength.toDouble())
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
        private val logger = LoggerFactory.getLogger(MusicPlayerServiceImpl::class.java)
    }
}
