package io.sparkled.music

import io.sparkled.model.entity.Playlist
import io.sparkled.model.entity.Sequence
import io.sparkled.model.entity.Song
import io.sparkled.model.entity.SongAudio
import io.sparkled.model.entity.StageProp
import io.sparkled.model.render.RenderedStagePropDataMap

/**
 * A container object holding all of the information pertaining to the current state of playback, in terms of audio
 * playback and associated rendered data for streaming to clients.
 */
data class PlaybackState(
    val playlist: Playlist? = null,
    val playlistIndex: Int = 0,
    private val progressFunction: () -> Double = { 0.0 },
    val sequence: Sequence? = null,
    val song: Song? = null,
    val songAudio: SongAudio? = null,
    val renderedStageProps: RenderedStagePropDataMap? = null,
    val stageProps: Map<String, StageProp> = emptyMap()
) {

    val isEmpty: Boolean
        get() = playlist == null || sequence == null || song == null || songAudio == null || renderedStageProps == null

    val progress: Double
        get() = progressFunction.invoke()
}
