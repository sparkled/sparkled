package io.sparkled.persistence.sequence.impl;

import io.sparkled.model.entity.*;
import io.sparkled.model.render.RenderedStagePropDataMap;
import io.sparkled.persistence.QueryFactory;
import io.sparkled.persistence.sequence.SequencePersistenceService;
import io.sparkled.persistence.sequence.impl.query.*;

import javax.inject.Inject;
import java.util.*;

public class SequencePersistenceServiceImpl implements SequencePersistenceService {

    private QueryFactory queryFactory;

    @Inject
    public SequencePersistenceServiceImpl(QueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public Sequence createSequence(Sequence sequence) {
        return new SaveSequenceQuery(sequence).perform(queryFactory);
    }

    @Override
    public List<Sequence> getAllSequences() {
        return new GetAllSequencesQuery().perform(queryFactory);
    }

    @Override
    public Optional<Sequence> getSequenceById(int sequenceId) {
        return new GetSequenceByIdQuery(sequenceId).perform(queryFactory);
    }

    @Override
    public Optional<Stage> getStageBySequenceId(int sequenceId) {
        return new GetStageBySequenceIdQuery(sequenceId).perform(queryFactory);
    }

    @Override
    public Optional<SongAudio> getSongAudioBySequenceId(int sequenceId) {
        return new GetSongAudioBySequenceIdQuery(sequenceId).perform(queryFactory);
    }

    @Override
    public List<SequenceChannel> getSequenceChannelsBySequenceId(int sequenceId) {
        return new GetSequenceChannelsBySequenceIdQuery(sequenceId).perform(queryFactory);
    }

    @Override
    public Optional<SequenceChannel> getSequenceChannelByUuid(int sequenceId, UUID uuid) {
        return new GetSequenceChannelByUuidQuery(sequenceId, uuid).perform(queryFactory);
    }

    @Override
    public RenderedStagePropDataMap getRenderedStagePropsBySequenceAndSong(Sequence sequence, Song song) {
        return new GetRenderedStagePropsBySequenceQuery(sequence, song).perform(queryFactory);
    }

    @Override
    public Map<String, UUID> getSequenceStagePropUuidMapBySequenceId(int sequenceId) {
        return new GetSequenceStagePropUuidMapBySequenceIdQuery(sequenceId).perform(queryFactory);
    }

    @Override
    public void saveSequence(Sequence sequence, List<SequenceChannel> sequenceChannels) {
        sequence = new SaveSequenceQuery(sequence).perform(queryFactory);
        new SaveSequenceChannelsQuery(sequence, sequenceChannels).perform(queryFactory);
    }

    @Override
    public void publishSequence(Sequence sequence, List<SequenceChannel> sequenceChannels, RenderedStagePropDataMap renderedStageProps) {
        sequence = new SaveSequenceQuery(sequence).perform(queryFactory);
        new SaveSequenceChannelsQuery(sequence, sequenceChannels).perform(queryFactory);
        new SaveRenderedStagePropsQuery(sequence, renderedStageProps).perform(queryFactory);
    }

    @Override
    public void deleteSequence(int sequenceId) {
        new DeleteSequencesQuery(Collections.singletonList(sequenceId)).perform(queryFactory);
    }
}
