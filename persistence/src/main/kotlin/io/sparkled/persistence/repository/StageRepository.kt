package io.sparkled.persistence.repository

import io.micronaut.data.annotation.Query
import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.repository.CrudRepository
import io.sparkled.model.StageModel
import io.sparkled.model.UniqueId

@JdbcRepository(dialect = Dialect.ANSI)
abstract class StageRepository : CrudRepository<StageModel, String> {

    @Query(
        """
        DELETE FROM SEQUENCE WHERE stage_id = :id
        DELETE FROM STAGE_PROP WHERE stage_id = :id
        DELETE FROM STAGE WHERE id = :id
    """
    )
    abstract fun deleteStageAndDependentsById(id: UniqueId)

    @Query(
        """
        SELECT s.*
            FROM STAGE s
            JOIN SEQUENCE sq on s.id = sq.stage_id
            WHERE sq.id = :sequenceId
    """
    )
    abstract fun findBySequenceId(sequenceId: UniqueId): StageModel?
}
