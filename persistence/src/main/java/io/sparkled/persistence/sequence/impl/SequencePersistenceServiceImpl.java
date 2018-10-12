package io.sparkled.persistence.sequence.impl;

import io.sparkled.model.entity.Sequence;
import io.sparkled.model.entity.SequenceChannel;
import io.sparkled.model.entity.SongAudio;
import io.sparkled.model.entity.Stage;
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
    public Integer createSequence(Sequence sequence, byte[] songAudioData) {
        sequence = saveSequence(sequence, Collections.emptyList());

        SongAudio songAudio = new SongAudio().setSequenceId(sequence.getId()).setAudioData(songAudioData);
        return new SaveSongAudioQuery(songAudio).perform(queryFactory);
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
    public RenderedStagePropDataMap getRenderedStagePropsBySequence(Sequence sequence) {
        return new GetRenderedStagePropsBySequenceQuery(sequence).perform(queryFactory);
    }

    @Override
    public Map<String, UUID> getSequenceStagePropUuidMapBySequenceId(Integer sequenceId) {
        return new GetSequenceStagePropUuidMapBySequenceIdQuery(sequenceId).perform(queryFactory);
    }

    @Override
    public Sequence saveSequence(Sequence sequence, List<SequenceChannel> sequenceChannels) {
        sequence = new SaveSequenceQuery(sequence).perform(queryFactory);
        new SaveSequenceChannelsQuery(sequence, sequenceChannels).perform(queryFactory);
        return sequence;
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
