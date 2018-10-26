package io.sparkled.persistence

import io.sparkled.model.entity.QPlaylist
import io.sparkled.model.entity.QPlaylistSequence
import io.sparkled.model.entity.QRenderedStageProp
import io.sparkled.model.entity.QSequence
import io.sparkled.model.entity.QSequenceChannel
import io.sparkled.model.entity.QSetting
import io.sparkled.model.entity.QSong
import io.sparkled.model.entity.QSongAudio
import io.sparkled.model.entity.QStage
import io.sparkled.model.entity.QStageProp
import java.util.Collections
import java.util.UUID

interface PersistenceQuery<out T> {

    fun perform(queryFactory: QueryFactory): T

    companion object {

        /**
         * Useful for IN queries where no UUIDs will match.
         */
        val noUuids: List<UUID> = Collections.singletonList(UUID(0, 0))

        /**
         * Useful for IN queries where no IDs will match.
         */
        val noIds: List<Int> = Collections.singletonList(-1)

        val qPlaylist = QPlaylist.playlist!!
        val qPlaylistSequence = QPlaylistSequence.playlistSequence!!
        val qRenderedStageProp = QRenderedStageProp.renderedStageProp!!
        val qSequence = QSequence.sequence!!
        val qSequenceChannel = QSequenceChannel.sequenceChannel!!
        val qSetting = QSetting.setting!!
        val qSong = QSong.song!!
        val qSongAudio = QSongAudio.songAudio!!
        val qStage = QStage.stage!!
        val qStageProp = QStageProp.stageProp!!
    }
}
