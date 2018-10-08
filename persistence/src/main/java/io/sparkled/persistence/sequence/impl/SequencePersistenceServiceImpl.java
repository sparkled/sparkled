package io.sparkled.persistence.sequence.impl;

import com.google.inject.persist.Transactional;
import io.sparkled.model.entity.Sequence;
import io.sparkled.model.entity.SequenceChannel;
import io.sparkled.model.entity.SongAudio;
import io.sparkled.model.entity.Stage;
import io.sparkled.model.render.RenderedStagePropDataMap;
import io.sparkled.persistence.QueryFactory;
import io.sparkled.persistence.sequence.SequencePersistenceService;
import io.sparkled.persistence.sequence.impl.query.*;

import javax.inject.Inject;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class SequencePersistenceServiceImpl implements SequencePersistenceService {

    private QueryFactory queryFactory;

    @Inject
    public SequencePersistenceServiceImpl(QueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    @Transactional
    public Integer createSequence(Sequence sequence, byte[] songAudioData) {
        int sequenceId = saveSequence(sequence, Collections.emptyList());

        SongAudio songAudio = new SongAudio().setSequenceId(sequenceId).setAudioData(songAudioData);
        return new SaveSongAudioQuery(songAudio).perform(queryFactory);
    }

    @Override
    @Transactional
    public List<Sequence> getAllSequences() {
        return new GetAllSequencesQuery().perform(queryFactory);
    }

    @Override
    @Transactional
    public Optional<Sequence> getSequenceById(int sequenceId) {
        return new GetSequenceByIdQuery(sequenceId).perform(queryFactory);
    }

    @Override
    @Transactional
    public Optional<Stage> getStageBySequenceId(int sequenceId) {
        return new GetStageBySequenceIdQuery(sequenceId).perform(queryFactory);
    }

    @Override
    @Transactional
    public Optional<SongAudio> getSongAudioBySequenceId(int sequenceId) {
        return new GetSongAudioBySequenceIdQuery(sequenceId).perform(queryFactory);
    }

    @Override
    @Transactional
    public List<SequenceChannel> getSequenceChannelsBySequenceId(int sequenceId) {
        return new GetSequenceChannelsBySequenceIdQuery(sequenceId).perform(queryFactory);
    }

    @Override
    @Transactional
    public Optional<SequenceChannel> getSequenceChannelByUuid(int sequenceId, UUID channelUuid) {
        return new GetSequenceChannelByUuidQuery(sequenceId, channelUuid).perform(queryFactory);
    }

    @Override
    @Transactional
    public RenderedStagePropDataMap getRenderedStageProps(Sequence sequence) {
        return new GetRenderedStagePropsQuery(sequence).perform(queryFactory);
    }

    @Override
    @Transactional
    public Integer saveSequence(Sequence sequence, List<SequenceChannel> sequenceChannels) {
        new SaveSequenceChannelsQuery(sequenceChannels).perform(queryFactory);
        return new SaveSequenceQuery(sequence).perform(queryFactory);
    }

    @Override
    @Transactional
    public Integer publishSequence(Sequence sequence, Stage stage, List<SequenceChannel> sequenceChannels, RenderedStagePropDataMap renderedStageProps) {
        Integer sequenceId = new SaveSequenceQuery(sequence).perform(queryFactory);
        new SaveSequenceChannelsQuery(sequenceChannels).perform(queryFactory);
        new SaveRenderedStagePropsQuery(sequence, renderedStageProps).perform(queryFactory);
        return sequenceId;
    }

    @Override
    @Transactional
    public void deleteSequence(int sequenceId) {
        new DeleteSequenceByIdQuery(sequenceId).perform(queryFactory);
    }
}
