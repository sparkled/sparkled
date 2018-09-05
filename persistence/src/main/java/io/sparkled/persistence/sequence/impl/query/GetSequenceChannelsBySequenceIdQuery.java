package io.sparkled.persistence.sequence.impl.query;

import io.sparkled.model.entity.SequenceChannel;
import io.sparkled.model.entity.SequenceChannel_;
import io.sparkled.persistence.PersistenceQuery;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class GetSequenceChannelsBySequenceIdQuery implements PersistenceQuery<List<SequenceChannel>> {

    private final int sequenceId;

    public GetSequenceChannelsBySequenceIdQuery(int sequenceId) {
        this.sequenceId = sequenceId;
    }

    @Override
    public List<SequenceChannel> perform(EntityManager entityManager) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<SequenceChannel> cq = cb.createQuery(SequenceChannel.class);
        Root<SequenceChannel> sequenceChannel = cq.from(SequenceChannel.class);

        cq.where(
                cb.equal(sequenceChannel.get(SequenceChannel_.sequenceId), sequenceId)
        );

        cq.orderBy(
                cb.asc(sequenceChannel.get(SequenceChannel_.displayOrder))
        );

        TypedQuery<SequenceChannel> query = entityManager.createQuery(cq);
        return query.getResultList();
    }
}
