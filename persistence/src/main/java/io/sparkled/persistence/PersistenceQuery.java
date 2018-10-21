package io.sparkled.persistence;

import io.sparkled.model.entity.*;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public interface PersistenceQuery<T> {

    /**
     * Useful for IN queries where no UUIDs will match.
     */
    List<UUID> noUuids = Collections.singletonList(new UUID(0, 0));

    /**
     * Useful for IN queries where no IDs will match.
     */
    List<Integer> noIds = Collections.singletonList(-1);

    QPlaylist qPlaylist = QPlaylist.playlist;
    QPlaylistSequence qPlaylistSequence = QPlaylistSequence.playlistSequence;
    QRenderedStageProp qRenderedStageProp = QRenderedStageProp.renderedStageProp;
    QSequence qSequence = QSequence.sequence;
    QSequenceChannel qSequenceChannel = QSequenceChannel.sequenceChannel;
    QSetting qSetting = QSetting.setting;
    QSong qSong = QSong.song;
    QSongAudio qSongAudio = QSongAudio.songAudio;
    QStage qStage = QStage.stage;
    QStageProp qStageProp = QStageProp.stageProp;

    T perform(QueryFactory queryFactory);
}
