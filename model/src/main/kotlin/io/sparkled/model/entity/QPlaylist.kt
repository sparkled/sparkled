package io.sparkled.model.entity

import com.querydsl.core.types.PathMetadataFactory.forVariable
import com.querydsl.core.types.dsl.EntityPathBase
import com.querydsl.core.types.dsl.NumberPath
import com.querydsl.core.types.dsl.StringPath

/**
 * QPlaylist is a Querydsl query type for Playlist
 */
@SuppressWarnings("unused")
class QPlaylist(variable: String) : EntityPathBase<Playlist>(Playlist::class.java, forVariable(variable)) {

    val id: NumberPath<Int> = createNumber("id", Int::class.java)
    val name: StringPath = createString("name")

    companion object {
        val playlist = QPlaylist("playlist")
    }
}
