package io.sparkled.persistence.sequence.impl.query;

import io.sparkled.model.entity.Sequence;
import io.sparkled.model.validator.SequenceValidator;
import io.sparkled.persistence.PersistenceQuery;
import io.sparkled.persistence.QueryFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;

public class SaveSequenceQuery implements PersistenceQuery<Sequence> {

    private static final Logger logger = LoggerFactory.getLogger(SaveSequenceQuery.class);
    private final Sequence sequence;

    public SaveSequenceQuery(Sequence sequence) {
        this.sequence = sequence;
    }

    @Override
    public Sequence perform(QueryFactory queryFactory) {
        new SequenceValidator().validate(sequence);

        final EntityManager entityManager = queryFactory.getEntityManager();
        Sequence result = entityManager.merge(sequence);

        logger.info("Saved sequence {} ({}).", sequence.getId(), sequence.getName());
        return result;
    }
}
