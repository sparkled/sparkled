package io.sparkled.model.entity

import com.querydsl.core.types.PathMetadataFactory.forVariable
import com.querydsl.core.types.dsl.ArrayPath
import com.querydsl.core.types.dsl.ComparablePath
import com.querydsl.core.types.dsl.EntityPathBase
import com.querydsl.core.types.dsl.NumberPath

/**
 * QRenderedStageProp is a Querydsl query type for RenderedStageProp
 */
class QRenderedStageProp(variable: String) :
    EntityPathBase<RenderedStageProp>(RenderedStageProp::class.java, forVariable(variable)) {

    val data: ArrayPath<ByteArray, Byte> = createArray("data", ByteArray::class.java)
    val id: NumberPath<Int> = createNumber("id", Int::class.java)
    val ledCount: NumberPath<Int> = createNumber("ledCount", Int::class.java)
    val sequenceId: NumberPath<Int> = createNumber("sequenceId", Int::class.java)
    val stagePropUuid: ComparablePath<java.util.UUID> = createComparable("stagePropUuid", java.util.UUID::class.java)

    companion object {
        val renderedStageProp = QRenderedStageProp("renderedStageProp")
    }
}
