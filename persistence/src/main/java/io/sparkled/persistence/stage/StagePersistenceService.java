package io.sparkled.persistence.stage;

import io.sparkled.model.entity.Stage;

import java.util.List;
import java.util.Optional;

public interface StagePersistenceService {

    List<Stage> getAllStages();

    Integer deleteStage(int stageId);

    Optional<Stage> getStageById(int stageId);

    Integer saveStage(Stage stage);
}
