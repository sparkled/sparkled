package com.kinephonics.db.repository

import com.kinephonics.model.LearnerSessionTakeModel
import com.kinephonics.model.enumeration.VideoProcessingStatus
import io.micronaut.data.annotation.Query
import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.repository.CrudRepository
import java.util.UUID

@JdbcRepository(dialect = Dialect.SQL_SERVER)
abstract class LearnerSessionTakeRepository : CrudRepository<LearnerSessionTakeModel, UUID> {
    @Query(
        """
        UPDATE LEARNER_SESSION_TAKE
        SET processing_status = :status,
            processing_error_count = 0,
            updated_at = CURRENT_TIMESTAMP
        WHERE id = :takeId
        AND processing_status <> :status
    """
    )
    abstract fun markAsPendingProcessing(
        takeId: UUID,
        status: VideoProcessingStatus = VideoProcessingStatus.PENDING,
    )

    @Query(
        """
        SELECT *
        FROM LEARNER_SESSION_TAKE WITH (NOLOCK)
        WHERE processing_status = :pendingStatus
        ORDER BY updated_at
        OFFSET 0 ROWS FETCH NEXT 1 ROWS ONLY
    """
    )
    abstract fun getNextPendingTake(
        pendingStatus: VideoProcessingStatus = VideoProcessingStatus.PENDING,
    ): LearnerSessionTakeModel?

    @Query(
        """
        UPDATE LEARNER_SESSION_TAKE
        SET processing_status = :processingStatus,
        processing_error_count = :processingErrorCount,
        updated_at = CURRENT_TIMESTAMP
        WHERE id = :takeId
    """
    )
    abstract fun setProcessingStatus(
        takeId: UUID,
        processingStatus: VideoProcessingStatus,
        processingErrorCount: Int,
    )

    @Query(
        """
        UPDATE LEARNER_SESSION_TAKE
        SET processing_status = 'PROCESSED',
            video_duration_ms = :videoDurationMs,
            processed_video_blob_container = :processedVideoBlobContainer,
            processed_video_blob_path = :processedVideoBlobPath,
            processed_video_width = :processedVideoWidth,
            processed_video_height = :processedVideoHeight,
            processed_video_type = :processedVideoType,
            thumbnail_blob_path = :thumbnailBlobPath,
            thumbnail_width = :thumbnailWidth,
            thumbnail_height = :thumbnailHeight,
            updated_at = CURRENT_TIMESTAMP
        WHERE id = :id
    """
    )
    abstract fun markAsProcessed(take: LearnerSessionTakeModel)
}
