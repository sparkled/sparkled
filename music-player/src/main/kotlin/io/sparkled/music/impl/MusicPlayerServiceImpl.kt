package io.sparkled.music.impl

import io.sparkled.common.logging.getLogger
import io.sparkled.music.MusicPlayerService
import io.sparkled.music.SequencePlaybackState
import jakarta.inject.Singleton
import java.io.ByteArrayInputStream
import javax.sound.sampled.AudioFormat
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.Clip
import javax.sound.sampled.LineEvent
import javax.sound.sampled.LineListener
import kotlin.math.min

@Singleton
class MusicPlayerServiceImpl : MusicPlayerService, LineListener {

    private val listeners = HashSet<LineListener>()
    private val clip by lazy {
        val clip = AudioSystem.getClip()
        clip.addLineListener(this)
        clip
    }

    private var lastFramePosition = 0
    private var lastProgressUpdate = 0L

    override fun play(playbackState: SequencePlaybackState) {
        stopPlayback()

        val sequence = playbackState.sequence
        logger.debug("Playing sequence.", "name" to sequence.name)

        try {
            playAudio(clip, playbackState.songAudio.array())
        } catch (e: Exception) {
            logger.error("Failed to play sequence.", e, "name" to sequence.name)
        }
    }

    override fun playOneShot(songAudio: ByteArray) {
        val clip = AudioSystem.getClip()
        try {
            playAudio(clip, songAudio)
            clip.addLineListener {
                if (it.type == LineEvent.Type.STOP) {
                    clip.close()
                }
            }
        } catch (e: Exception) {
            logger.error("Error playing one-shot audio.", e)
            runCatching { clip.close() }
        }
    }

    private fun playAudio(clip: Clip, audio: ByteArray) {
        val byteStream = ByteArrayInputStream(audio)
        AudioSystem.getAudioInputStream(byteStream).use { mp3Stream ->
            val baseFormat = mp3Stream.format
            val decodedFormat = AudioFormat(
                AudioFormat.Encoding.PCM_SIGNED, baseFormat.sampleRate, 16, baseFormat.channels,
                baseFormat.channels * 2, baseFormat.sampleRate, false,
            )

            AudioSystem.getAudioInputStream(decodedFormat, mp3Stream).use { convertedStream ->
                clip.close()
                clip.open(convertedStream)
                clip.start()
            }
        }
    }

    override fun addLineListener(listener: LineListener) {
        this.listeners.add(listener)
        logger.info("Added line listener.", "listener" to listener)
    }

    override val sequenceProgress: Double
        get() {
            return when {
                !clip.isActive -> 0.0
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
        clip.close()
    }

    override fun update(event: LineEvent) {
        listeners.forEach { it.update(event) }
    }

    companion object {
        private val logger = getLogger<MusicPlayerServiceImpl>()
    }
}
