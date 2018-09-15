package io.sparkled.persistence.sequence.impl;

import com.google.inject.persist.Transactional;
import io.sparkled.model.entity.*;
import io.sparkled.model.render.RenderedStagePropDataMap;
import io.sparkled.persistence.BulkDeleteQuery;
import io.sparkled.persistence.sequence.SequencePersistenceService;
import io.sparkled.persistence.sequence.impl.query.*;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class SequencePersistenceServiceImpl implements SequencePersistenceService {

    private Provider<EntityManager> entityManagerProvider;

    @Inject
    public SequencePersistenceServiceImpl(Provider<EntityManager> entityManagerProvider) {
        this.entityManagerProvider = entityManagerProvider;
    }

    @Override
    @Transactional
    public List<Sequence> getAllSequences() {
        return new GetAllSequencesQuery().perform(entityManagerProvider.get());
    }

    @Override
    @Transactional
    public Integer deleteSequence(int sequenceId) {
        return new DeleteSequenceQuery(sequenceId).perform(entityManagerProvider.get());
    }

    @Override
    @Transactional
    public Optional<Sequence> getSequenceById(int sequenceId) {
        return new GetSequenceByIdQuery(sequenceId).perform(entityManagerProvider.get());
    }

    @Override
    @Transactional
    public Optional<Stage> getStageBySequenceId(int sequenceId) {
        return new GetStageBySequenceIdQuery(sequenceId).perform(entityManagerProvider.get());
    }

    @Override
    @Transactional
    public Optional<SongAudio> getSongAudioBySequenceId(int sequenceId) {
        return new GetSongAudioBySequenceIdQuery(sequenceId).perform(entityManagerProvider.get());
    }

    @Override
    @Transactional
    public Integer saveSequence(Sequence sequence, List<SequenceChannel> sequenceChannels) {
        new SaveSequenceChannelsQuery(sequenceChannels).perform(entityManagerProvider.get());
        return new SaveSequenceQuery(sequence).perform(entityManagerProvider.get());
    }

    @Override
    @Transactional
    public Integer saveSongAudio(SongAudio songAudio) {
        return new SaveSongAudioQuery(songAudio).perform(entityManagerProvider.get());
    }

    @Override
    @Transactional
    public RenderedStagePropDataMap getRenderedStageProps(Sequence sequence) {
        return new GetRenderedStagePropsQuery(sequence).perform(entityManagerProvider.get());
    }

    @Override
    @Transactional
    public void saveRenderedChannels(Sequence sequence, RenderedStagePropDataMap renderedStagePropDataMap) {
        deleteRenderedStageProps(sequence.getId());
        new SaveRenderedStagePropQuery(sequence, renderedStagePropDataMap).perform(entityManagerProvider.get());
    }

    @Override
    @Transactional
    public void deleteRenderedStageProps(int sequenceId) {
        EntityManager entityManager = entityManagerProvider.get();
        BulkDeleteQuery.from(RenderedStageProp.class).where(RenderedStageProp_.sequenceId).is(sequenceId).perform(entityManager);
    }

    @Override
    @Transactional
    public Optional<SequenceChannel> getSequenceChannelByUuid(int sequenceId, UUID channelUuid) {
        return new GetSequenceChannelByUuidQuery(sequenceId, channelUuid).perform(entityManagerProvider.get());
    }

    @Override
    public List<SequenceChannel> getSequenceChannelsBySequenceId(int sequenceId) {
        return new GetSequenceChannelsBySequenceIdQuery(sequenceId).perform(entityManagerProvider.get());
    }
}
