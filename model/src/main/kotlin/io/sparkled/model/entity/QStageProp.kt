package io.sparkled.model.entity

import com.querydsl.core.types.PathMetadataFactory.forVariable
import com.querydsl.core.types.dsl.BooleanPath
import com.querydsl.core.types.dsl.ComparablePath
import com.querydsl.core.types.dsl.EntityPathBase
import com.querydsl.core.types.dsl.NumberPath
import com.querydsl.core.types.dsl.StringPath

/**
 * QStageProp is a Querydsl query type for StageProp
 */
class QStageProp(variable: String) : EntityPathBase<StageProp>(StageProp::class.java, forVariable(variable)) {

    val brightness: NumberPath<Int> = createNumber("brightness", Int::class.java)
    val code: StringPath = createString("code")
    val displayOrder: NumberPath<Int> = createNumber("displayOrder", Int::class.java)
    val ledCount: NumberPath<Int> = createNumber("ledCount", Int::class.java)
    val name: StringPath = createString("name")
    val positionX: NumberPath<Int> = createNumber("positionX", Int::class.java)
    val positionY: NumberPath<Int> = createNumber("positionY", Int::class.java)
    val reverse: BooleanPath = createBoolean("reverse")
    val rotation: NumberPath<Int> = createNumber("rotation", Int::class.java)
    val scaleX: NumberPath<Float> = createNumber("scaleX", Float::class.java)
    val scaleY: NumberPath<Float> = createNumber("scaleY", Float::class.java)
    val stageId: NumberPath<Int> = createNumber("stageId", Int::class.java)
    val type: StringPath = createString("type")
    val uuid: ComparablePath<java.util.UUID> = createComparable("uuid", java.util.UUID::class.java)

    companion object {
        val stageProp = QStageProp("stageProp")
    }
}
