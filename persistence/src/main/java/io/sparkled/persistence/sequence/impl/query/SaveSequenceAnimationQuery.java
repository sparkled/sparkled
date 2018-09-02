package io.sparkled.persistence.sequence.impl.query;

import io.sparkled.model.entity.SequenceAnimation;
import io.sparkled.model.validator.SequenceAnimationValidator;
import io.sparkled.persistence.PersistenceQuery;

import javax.persistence.EntityManager;

public class SaveSequenceAnimationQuery implements PersistenceQuery<Integer> {

    private final SequenceAnimation sequenceAnimation;

    public SaveSequenceAnimationQuery(SequenceAnimation sequenceAnimation) {
        this.sequenceAnimation = sequenceAnimation;
    }

    @Override
    public Integer perform(EntityManager entityManager) {
        SequenceAnimationValidator sequenceAnimationValidator = new SequenceAnimationValidator();
        sequenceAnimationValidator.validate(sequenceAnimation);

        SequenceAnimation savedSequenceAnimation = entityManager.merge(sequenceAnimation);
        return savedSequenceAnimation.getSequenceId();
    }
}
