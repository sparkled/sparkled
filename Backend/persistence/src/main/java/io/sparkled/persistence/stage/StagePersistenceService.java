package io.sparkled.persistence.stage;

import io.sparkled.model.entity.Stage;

import java.util.Optional;

public interface StagePersistenceService {

    Optional<Stage> getStageById(int stageId);
}
