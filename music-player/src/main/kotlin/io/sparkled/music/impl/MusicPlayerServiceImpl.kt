package io.sparkled.music.impl

import io.sparkled.music.MusicPlayerService
import io.sparkled.music.PlaybackState
import org.slf4j.LoggerFactory
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.util.*
import javax.inject.Singleton
import javax.sound.sampled.*
import kotlin.math.min

@Singleton
class MusicPlayerServiceImpl : MusicPlayerService, LineListener {

    private val listeners = HashSet<LineListener>()
    private var clip: Clip? = null
    private var lastFramePosition = 0
    private var lastProgressUpdate = 0L

    override fun play(playbackState: PlaybackState) {
        stopPlayback()

        var byteStream: InputStream? = null
        var mp3Stream: AudioInputStream? = null
        var convertedStream: AudioInputStream? = null

        try {
            logger.debug("Playing sequence {}.", playbackState.sequence?.name)

            byteStream = ByteArrayInputStream(playbackState.songAudio)
            mp3Stream = AudioSystem.getAudioInputStream(byteStream)

            val baseFormat = mp3Stream!!.format
            val decodedFormat = AudioFormat(
                AudioFormat.Encoding.PCM_SIGNED, baseFormat.sampleRate, 16, baseFormat.channels,
                baseFormat.channels * 2, baseFormat.sampleRate, false
            )
            convertedStream = AudioSystem.getAudioInputStream(decodedFormat, mp3Stream)

            val clip = AudioSystem.getClip()
            this.clip = clip

            clip.open(convertedStream)
            clip.addLineListener(this)
            clip.start()
        } catch (e: Exception) {
            logger.error("Failed to play sequence {}: {}.", playbackState.sequence?.name, e.message)
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
            val clip = this.clip
            return when {
                clip == null -> 0.0
                System.currentTimeMillis() - lastProgressUpdate < 1000 -> {
                    // Looking up playback position can be slow (up to 50ms), so the position is only looked up once per
                    // second, and progress is inferred from that.
                    val newFrames = (System.currentTimeMillis() - lastProgressUpdate) * (clip.format.frameRate / 1000.0)
                    val frame = lastFramePosition + newFrames
                    min(1.0, frame / clip.frameLength.toDouble())
                }
                else -> {
                    lastFramePosition = clip.framePosition
                    lastProgressUpdate = System.currentTimeMillis()
                    min(1.0, lastFramePosition / clip.frameLength.toDouble())
                }
            }
        }

    override fun stopPlayback() {
        lastProgressUpdate = 0

        val clip = this.clip
        if (clip != null && clip.isOpen) {
            try {
                clip.close()
                logger.debug("Clip closed.")
            } catch (e: Exception) {
                logger.error("Failed to close clip.")
            }
        } else {
            logger.warn("Clip is already closed.")
        }

        this.clip = null
    }

    override fun update(event: LineEvent) {
        listeners.forEach { it.update(event) }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(MusicPlayerServiceImpl::class.java)
    }
}
