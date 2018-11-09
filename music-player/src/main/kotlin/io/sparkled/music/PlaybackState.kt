package io.sparkled.music

import io.sparkled.model.entity.Playlist
import io.sparkled.model.entity.Sequence
import io.sparkled.model.entity.Song
import io.sparkled.model.entity.SongAudio
import io.sparkled.model.render.RenderedStagePropDataMap
import java.util.Objects
import java.util.UUID
import java.util.function.Supplier

/**
 * A container object holding all of the information pertaining to the current state of playback, in terms of audio
 * playback and associated rendered data for streaming to clients.
 */
class PlaybackState constructor(
    val playlist: Playlist? = null,
    val playlistIndex: Int = 0,
    private val progressFunction: Supplier<Double>? = null,
    val sequence: Sequence? = null,
    val song: Song? = null,
    val songAudio: SongAudio? = null,
    val renderedStageProps: RenderedStagePropDataMap? = null,
    val stagePropUuids: Map<String, UUID> = mapOf()
) {

    val isEmpty: Boolean
        get() = playlist == null || sequence == null || song == null || songAudio == null || renderedStageProps == null

    val progress: Double
        get() = progressFunction!!.get()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as PlaybackState?
        return playlistIndex == that!!.playlistIndex &&
            playlist == that.playlist &&
            progressFunction == that.progressFunction &&
            sequence == that.sequence &&
            song == that.song &&
            songAudio == that.songAudio &&
            renderedStageProps == that.renderedStageProps &&
            stagePropUuids == that.stagePropUuids
    }

    override fun hashCode(): Int {
        return Objects.hash(
            playlist,
            playlistIndex,
            progressFunction,
            sequence,
            song,
            songAudio,
            renderedStageProps,
            stagePropUuids
        )
    }

    override fun toString(): String {
        return "PlaybackState{" +
            "playlist=" + playlist +
            ", playlistIndex=" + playlistIndex +
            ", progressFunction=" + progressFunction +
            ", sequence=" + sequence +
            ", song=" + song +
            ", songAudio=" + songAudio +
            ", renderedStageProps=" + renderedStageProps +
            ", stagePropUuids=" + stagePropUuids +
            '}'
    }
}
