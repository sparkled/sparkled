package io.sparkled.persistence.sequence.impl.query;

import io.sparkled.model.entity.SequenceChannel;
import io.sparkled.model.validator.SequenceChannelValidator;
import io.sparkled.persistence.PersistenceQuery;
import io.sparkled.persistence.QueryFactory;

import javax.persistence.EntityManager;
import java.util.List;

public class SaveSequenceChannelsQuery implements PersistenceQuery<Void> {

    private final List<SequenceChannel> sequenceChannels;

    public SaveSequenceChannelsQuery(List<SequenceChannel> sequenceChannels) {
        this.sequenceChannels = sequenceChannels;
    }

    @Override
    public Void perform(QueryFactory queryFactory) {
        final EntityManager entityManager = queryFactory.getEntityManager();

        SequenceChannelValidator sequenceChannelValidator = new SequenceChannelValidator();
        sequenceChannels.forEach(sequenceChannelValidator::validate);
        sequenceChannels.forEach(entityManager::merge);
        return null;
    }
}
