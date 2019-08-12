package io.sparkled.model.entity

import com.querydsl.core.types.PathMetadataFactory.forVariable
import com.querydsl.core.types.dsl.ComparablePath
import com.querydsl.core.types.dsl.EntityPathBase
import com.querydsl.core.types.dsl.NumberPath

/**
 * QPlaylistSequence is a Querydsl query type for PlaylistSequence
 */
class QPlaylistSequence(variable: String) :
    EntityPathBase<PlaylistSequence>(PlaylistSequence::class.java, forVariable(variable)) {

    val displayOrder: NumberPath<Int> = createNumber("displayOrder", Int::class.java)
    val playlistId: NumberPath<Int> = createNumber("playlistId", Int::class.java)
    val sequenceId: NumberPath<Int> = createNumber("sequenceId", Int::class.java)
    val uuid: ComparablePath<java.util.UUID> = createComparable("uuid", java.util.UUID::class.java)

    companion object {
        val playlistSequence = QPlaylistSequence("playlistSequence")
    }
}
