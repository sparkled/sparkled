package io.sparkled.model.entity

import com.querydsl.core.types.PathMetadataFactory.forVariable
import com.querydsl.core.types.dsl.EntityPathBase
import com.querydsl.core.types.dsl.NumberPath
import com.querydsl.core.types.dsl.StringPath

/**
 * QStage is a Querydsl query type for Stage
 */
class QStage(variable: String) : EntityPathBase<Stage>(Stage::class.java, forVariable(variable)) {

    val height: NumberPath<Int> = createNumber("height", Int::class.java)
    val id: NumberPath<Int> = createNumber("id", Int::class.java)
    val name: StringPath = createString("name")
    val width: NumberPath<Int> = createNumber("width", Int::class.java)

    companion object {
        val stage = QStage("stage")
    }
}
