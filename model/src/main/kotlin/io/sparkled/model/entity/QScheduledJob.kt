package io.sparkled.model.entity

import com.querydsl.core.types.PathMetadataFactory.forVariable
import com.querydsl.core.types.dsl.EntityPathBase
import com.querydsl.core.types.dsl.EnumPath
import com.querydsl.core.types.dsl.NumberPath
import com.querydsl.core.types.dsl.StringPath

/**
 * QScheduledJob is a Querydsl query type for ScheduledJob
 */
class QScheduledJob(variable: String) : EntityPathBase<ScheduledJob>(ScheduledJob::class.java, forVariable(variable)) {

    val action: EnumPath<ScheduledJobAction> = createEnum("action", ScheduledJobAction::class.java)
    val cronExpression: StringPath = createString("cronExpression")
    val id: NumberPath<Int> = createNumber("id", Int::class.java)
    val playlistId: NumberPath<Int> = createNumber("playlistId", Int::class.java)
    val value: StringPath = createString("value")

    companion object {
        val scheduledJob = QScheduledJob("scheduledJob")
    }
}
