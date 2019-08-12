package io.sparkled.model.entity

import com.querydsl.core.types.PathMetadataFactory.forVariable
import com.querydsl.core.types.dsl.ComparablePath
import com.querydsl.core.types.dsl.EntityPathBase
import com.querydsl.core.types.dsl.NumberPath
import com.querydsl.core.types.dsl.StringPath

/**
 * QSequenceChannel is a Querydsl query type for SequenceChannel
 */
class QSequenceChannel(variable: String) :
    EntityPathBase<SequenceChannel>(SequenceChannel::class.java, forVariable(variable)) {

    val channelJson: StringPath = createString("channelJson")
    val displayOrder: NumberPath<Int> = createNumber("displayOrder", Int::class.java)
    val name: StringPath = createString("name")
    val sequenceId: NumberPath<Int> = createNumber("sequenceId", Int::class.java)
    val stagePropUuid: ComparablePath<java.util.UUID> = createComparable("stagePropUuid", java.util.UUID::class.java)
    val uuid: ComparablePath<java.util.UUID> = createComparable("uuid", java.util.UUID::class.java)

    companion object {
        val sequenceChannel = QSequenceChannel("sequenceChannel")
    }
}
