package io.sparkled.persistence;

import io.sparkled.model.entity.*;

public interface PersistenceQuery<T> {

    QRenderedStageProp qRenderedStageProp = QRenderedStageProp.renderedStageProp;
    QScheduledSequence qScheduledSequence = QScheduledSequence.scheduledSequence;
    QSequence qSequence = QSequence.sequence;
    QSequenceChannel qSequenceChannel = QSequenceChannel.sequenceChannel;
    QSongAudio qSongAudio = QSongAudio.songAudio;
    QStage qStage = QStage.stage;
    QStageProp qStageProp = QStageProp.stageProp;

    T perform(QueryFactory queryFactory);
}
