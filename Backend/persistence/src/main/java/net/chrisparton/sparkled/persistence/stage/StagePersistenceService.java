package net.chrisparton.sparkled.persistence.stage;

import net.chrisparton.sparkled.entity.Stage;

import java.util.Optional;

public interface StagePersistenceService {

    Optional<Stage> getStageById(int stageId);
}
