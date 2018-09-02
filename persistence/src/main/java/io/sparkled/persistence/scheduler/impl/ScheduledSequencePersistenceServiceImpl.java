package io.sparkled.persistence.scheduler.impl;

import com.google.inject.persist.Transactional;
import io.sparkled.model.entity.ScheduledSequence;
import io.sparkled.model.entity.Sequence;
import io.sparkled.persistence.scheduler.impl.query.*;
import io.sparkled.persistence.scheduler.ScheduledSequencePersistenceService;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.persistence.EntityManager;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class ScheduledSequencePersistenceServiceImpl implements ScheduledSequencePersistenceService {

    private Provider<EntityManager> entityManagerProvider;

    @Inject
    public ScheduledSequencePersistenceServiceImpl(Provider<EntityManager> entityManagerProvider) {
        this.entityManagerProvider = entityManagerProvider;
    }

    @Override
    @Transactional
    public Optional<ScheduledSequence> getNextScheduledSequence() {
        return new GetNextScheduledSequenceQuery().perform(entityManagerProvider.get());
    }

    @Override
    @Transactional
    public Optional<Sequence> getNextAutoSchedulableSequence(int lastSequenceId) {
        return new GetNextAutoSchedulableSequenceQuery(lastSequenceId).perform(entityManagerProvider.get());
    }

    @Override
    @Transactional
    public List<ScheduledSequence> getScheduledSequences(Date startDate, Date endDate) {
        return new GetScheduledSequencesQuery(startDate, endDate).perform(entityManagerProvider.get());
    }

    @Override
    @Transactional
    public Optional<ScheduledSequence> getScheduledSequenceAtTime(Date time) {
        return new GetScheduledSequenceAtTimeQuery(time).perform(entityManagerProvider.get());
    }

    @Override
    @Transactional
    public boolean removeScheduledSequence(int scheduledSequenceId) {
        return new RemoveScheduledSequenceQuery(scheduledSequenceId).perform(entityManagerProvider.get());
    }

    @Override
    @Transactional
    public boolean saveScheduledSequence(ScheduledSequence scheduledSequence) {
        return new SaveScheduledSequenceQuery(scheduledSequence).perform(entityManagerProvider.get());
    }
}
