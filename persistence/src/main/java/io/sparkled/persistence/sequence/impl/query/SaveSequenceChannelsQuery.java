package io.sparkled.persistence.sequence.impl.query;

import io.sparkled.model.entity.Sequence;
import io.sparkled.model.entity.SequenceChannel;
import io.sparkled.model.validator.SequenceChannelValidator;
import io.sparkled.model.validator.exception.EntityValidationException;
import io.sparkled.persistence.PersistenceQuery;
import io.sparkled.persistence.QueryFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

public class SaveSequenceChannelsQuery implements PersistenceQuery<Void> {

    private static final Logger logger = LoggerFactory.getLogger(SaveSequenceChannelsQuery.class);

    private final Sequence sequence;
    private final List<SequenceChannel> sequenceChannels;

    public SaveSequenceChannelsQuery(Sequence sequence, List<SequenceChannel> sequenceChannels) {
        this.sequence = sequence;
        this.sequenceChannels = sequenceChannels;
    }

    @Override
    public Void perform(QueryFactory queryFactory) {
        SequenceChannelValidator sequenceChannelValidator = new SequenceChannelValidator();
        sequenceChannels.forEach(sc -> sc.setSequenceId(sequence.getId()));
        sequenceChannels.forEach(sequenceChannelValidator::validate);

        if (uuidAlreadyInUse(queryFactory)) {
            throw new EntityValidationException("Sequence channel already exists on another sequence.");
        } else {
            final EntityManager entityManager = queryFactory.getEntityManager();
            sequenceChannels.forEach(entityManager::merge);
            logger.info("Saved {} sequence channel(s) for sequence {}.", sequenceChannels.size(), sequence.getId());

            deleteRemovedSequenceChannels(queryFactory);
            return null;
        }
    }

    private boolean uuidAlreadyInUse(QueryFactory queryFactory) {
        List<UUID> uuidsToCheck = sequenceChannels.stream().map(SequenceChannel::getUuid).collect(toList());
        uuidsToCheck = uuidsToCheck.isEmpty() ? noUuids : uuidsToCheck;

        long uuidsInUse = queryFactory.select(qSequenceChannel)
                .from(qSequenceChannel)
                .where(qSequenceChannel.sequenceId.ne(sequence.getId()).and(qSequenceChannel.uuid.in(uuidsToCheck)))
                .fetchCount();
        return uuidsInUse > 0;
    }

    private void deleteRemovedSequenceChannels(QueryFactory queryFactory) {
        List<UUID> uuidsToDelete = getSequenceChannelUuidsToDelete(queryFactory);
        new DeleteSequenceChannelsQuery(uuidsToDelete).perform(queryFactory);
    }

    private List<UUID> getSequenceChannelUuidsToDelete(QueryFactory queryFactory) {
        List<UUID> uuidsToKeep = sequenceChannels.stream().map(SequenceChannel::getUuid).collect(toList());
        uuidsToKeep = uuidsToKeep.isEmpty() ? noUuids : uuidsToKeep;

        return queryFactory
                .select(qSequenceChannel.uuid)
                .from(qSequenceChannel)
                .where(qSequenceChannel.sequenceId.eq(sequence.getId()).and(qSequenceChannel.uuid.notIn(uuidsToKeep)))
                .fetch();
    }
}
