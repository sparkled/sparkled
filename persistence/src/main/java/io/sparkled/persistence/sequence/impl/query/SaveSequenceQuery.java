package io.sparkled.persistence.sequence.impl.query;

import io.sparkled.model.entity.Sequence;
import io.sparkled.model.validator.SequenceValidator;
import io.sparkled.persistence.PersistenceQuery;

import javax.persistence.EntityManager;

public class SaveSequenceQuery implements PersistenceQuery<Integer> {

    private final Sequence sequence;

    public SaveSequenceQuery(Sequence sequence) {
        this.sequence = sequence;
    }

    @Override
    public Integer perform(EntityManager entityManager) {
        new SequenceValidator(sequence).validate();
        Sequence result = entityManager.merge(sequence);
        return result.getId();
    }
}
