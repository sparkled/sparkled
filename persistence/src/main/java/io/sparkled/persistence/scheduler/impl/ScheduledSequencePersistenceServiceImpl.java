package io.sparkled.persistence.scheduler.impl;

import com.google.inject.persist.Transactional;
import io.sparkled.model.entity.ScheduledSequence;
import io.sparkled.model.entity.Sequence;
import io.sparkled.persistence.QueryFactory;
import io.sparkled.persistence.scheduler.ScheduledSequencePersistenceService;
import io.sparkled.persistence.scheduler.impl.query.*;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class ScheduledSequencePersistenceServiceImpl implements ScheduledSequencePersistenceService {

    private QueryFactory queryFactory;

    @Inject
    public ScheduledSequencePersistenceServiceImpl(QueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    @Transactional
    public Optional<ScheduledSequence> getNextScheduledSequence() {
        return new GetNextScheduledSequenceQuery().perform(queryFactory);
    }

    @Override
    @Transactional
    public Optional<Sequence> getNextAutoSchedulableSequence(int lastSequenceId) {
        return new GetNextAutoSchedulableSequenceQuery(lastSequenceId).perform(queryFactory);
    }

    @Override
    @Transactional
    public List<ScheduledSequence> getScheduledSequences(Date startDate, Date endDate) {
        return new GetScheduledSequencesQuery(startDate, endDate).perform(queryFactory);
    }

    @Override
    @Transactional
    public Optional<ScheduledSequence> getScheduledSequenceAtTime(Date time) {
        return new GetScheduledSequenceAtTimeQuery(time).perform(queryFactory);
    }

    @Override
    @Transactional
    public boolean removeScheduledSequence(int scheduledSequenceId) {
        return new RemoveScheduledSequenceQuery(scheduledSequenceId).perform(queryFactory);
    }

    @Override
    @Transactional
    public boolean saveScheduledSequence(ScheduledSequence scheduledSequence) {
        return new SaveScheduledSequenceQuery(scheduledSequence).perform(queryFactory);
    }
}
