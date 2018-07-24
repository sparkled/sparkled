package io.sparkled.persistence.stage.impl.query;

import io.sparkled.model.entity.Stage;
import io.sparkled.model.validator.StageValidator;
import io.sparkled.persistence.PersistenceQuery;

import javax.persistence.EntityManager;

public class SaveStageQuery implements PersistenceQuery<Integer> {

    private final Stage stage;

    public SaveStageQuery(Stage stage) {
        this.stage = stage;
    }

    @Override
    public Integer perform(EntityManager entityManager) {
        new StageValidator(stage).validate();
        Stage result = entityManager.merge(stage);
        return result.getId();
    }
}
