package io.sparkled.persistence

import io.sparkled.model.entity.*

import java.util.Collections
import java.util.UUID

interface PersistenceQuery<T> {

    fun perform(queryFactory: QueryFactory): T

    companion object {

        /**
         * Useful for IN queries where no UUIDs will match.
         */
        val noUuids = Collections.singletonList(UUID(0, 0))

        /**
         * Useful for IN queries where no IDs will match.
         */
        val noIds = Collections.singletonList(-1)

        val qPlaylist = QPlaylist.playlist
        val qPlaylistSequence = QPlaylistSequence.playlistSequence
        val qRenderedStageProp = QRenderedStageProp.renderedStageProp
        val qSequence = QSequence.sequence
        val qSequenceChannel = QSequenceChannel.sequenceChannel
        val qSetting = QSetting.setting
        val qSong = QSong.song
        val qSongAudio = QSongAudio.songAudio
        val qStage = QStage.stage
        val qStageProp = QStageProp.stageProp
    }
}
