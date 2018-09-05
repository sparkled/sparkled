package io.sparkled.persistence.sequence.impl.query;

import io.sparkled.model.entity.SequenceChannel;
import io.sparkled.model.entity.SequenceChannel_;
import io.sparkled.persistence.PersistenceQuery;
import io.sparkled.persistence.util.PersistenceUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Optional;
import java.util.UUID;

public class GetSequenceChannelByUuidQuery implements PersistenceQuery<Optional<SequenceChannel>> {

    private final int sequenceId;
    private final UUID channelUuid;

    public GetSequenceChannelByUuidQuery(int sequenceId, UUID channelUuid) {
        this.sequenceId = sequenceId;
        this.channelUuid = channelUuid;
    }

    @Override
    public Optional<SequenceChannel> perform(EntityManager entityManager) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<SequenceChannel> cq = cb.createQuery(SequenceChannel.class);
        Root<SequenceChannel> sequenceChannel = cq.from(SequenceChannel.class);
        cq.where(
                cb.and(
                        cb.equal(sequenceChannel.get(SequenceChannel_.sequenceId), sequenceId),
                        cb.equal(sequenceChannel.get(SequenceChannel_.uuid), channelUuid)
                )
        );

        TypedQuery<SequenceChannel> query = entityManager.createQuery(cq);
        return PersistenceUtils.getSingleResult(query);
    }
}
