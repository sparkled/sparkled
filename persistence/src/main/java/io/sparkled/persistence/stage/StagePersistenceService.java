package io.sparkled.persistence.stage;

import io.sparkled.model.entity.Stage;
import io.sparkled.model.entity.StageProp;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StagePersistenceService {

    Stage createStage(Stage stage);

    List<Stage> getAllStages();

    Optional<Stage> getStageById(int stageId);

    List<StageProp> getStagePropsByStageId(Integer stageId);

    Optional<StageProp> getStagePropByUuid(Integer stageId, UUID uuid);

    void saveStage(Stage stage, List<StageProp> stageProps);

    void deleteStage(int stageId);
}
