package io.sparkled.model.entity

import com.querydsl.core.types.PathMetadataFactory.forVariable
import com.querydsl.core.types.dsl.EntityPathBase
import com.querydsl.core.types.dsl.EnumPath
import com.querydsl.core.types.dsl.NumberPath
import com.querydsl.core.types.dsl.StringPath

/**
 * QSequence is a Querydsl query type for Sequence
 */
class QSequence(variable: String) : EntityPathBase<Sequence>(Sequence::class.java, forVariable(variable)) {

    val framesPerSecond: NumberPath<Int> = createNumber("framesPerSecond", Int::class.java)
    val id: NumberPath<Int> = createNumber("id", Int::class.java)
    val name: StringPath = createString("name")
    val songId: NumberPath<Int> = createNumber("songId", Int::class.java)
    val stageId: NumberPath<Int> = createNumber("stageId", Int::class.java)
    val status: EnumPath<SequenceStatus> = createEnum("status", SequenceStatus::class.java)

    companion object {
        val sequence = QSequence("sequence")
    }
}
