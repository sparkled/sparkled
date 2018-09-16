package io.sparkled.persistence.sequence.impl.query;

import io.sparkled.model.entity.Sequence;
import io.sparkled.model.validator.SequenceValidator;
import io.sparkled.persistence.PersistenceQuery;
import io.sparkled.persistence.QueryFactory;

import javax.persistence.EntityManager;

public class SaveSequenceQuery implements PersistenceQuery<Integer> {

    private final Sequence sequence;

    public SaveSequenceQuery(Sequence sequence) {
        this.sequence = sequence;
    }

    @Override
    public Integer perform(QueryFactory queryFactory) {
        final EntityManager entityManager = queryFactory.getEntityManager();

        new SequenceValidator(sequence).validate();
        Sequence result = entityManager.merge(sequence);
        return result.getId();
    }
}
