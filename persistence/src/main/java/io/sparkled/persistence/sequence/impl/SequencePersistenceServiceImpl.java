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
    public List<Sequence> getAllSequences() {
        return new GetAllSequencesQuery().perform(queryFactory);
    }

    @Override
    @Transactional
    public Integer deleteSequence(int sequenceId) {
        return new DeleteSequenceQuery(sequenceId).perform(queryFactory);
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
    public Integer saveSequence(Sequence sequence, List<SequenceChannel> sequenceChannels) {
        new SaveSequenceChannelsQuery(sequenceChannels).perform(queryFactory);
        return new SaveSequenceQuery(sequence).perform(queryFactory);
    }

    @Override
    @Transactional
    public Integer saveSongAudio(SongAudio songAudio) {
        return new SaveSongAudioQuery(songAudio).perform(queryFactory);
    }

    @Override
    @Transactional
    public RenderedStagePropDataMap getRenderedStageProps(Sequence sequence) {
        return new GetRenderedStagePropsQuery(sequence).perform(queryFactory);
    }

    @Override
    @Transactional
    public void saveRenderedChannels(Sequence sequence, RenderedStagePropDataMap renderedStagePropDataMap) {
        deleteRenderedStageProps(sequence.getId());
        new SaveRenderedStagePropQuery(sequence, renderedStagePropDataMap).perform(queryFactory);
    }

    @Override
    @Transactional
    public void deleteRenderedStageProps(int sequenceId) {
        new DeleteRenderedStagePropsBySequenceIdQuery(sequenceId).perform(queryFactory);
    }

    @Override
    @Transactional
    public Optional<SequenceChannel> getSequenceChannelByUuid(int sequenceId, UUID channelUuid) {
        return new GetSequenceChannelByUuidQuery(sequenceId, channelUuid).perform(queryFactory);
    }

    @Override
    public List<SequenceChannel> getSequenceChannelsBySequenceId(int sequenceId) {
        return new GetSequenceChannelsBySequenceIdQuery(sequenceId).perform(queryFactory);
    }
}
