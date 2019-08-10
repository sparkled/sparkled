package io.sparkled.model.entity

import com.querydsl.core.types.PathMetadataFactory.forVariable
import com.querydsl.core.types.dsl.EntityPathBase
import com.querydsl.core.types.dsl.NumberPath
import com.querydsl.core.types.dsl.StringPath

/**
 * QSong is a Querydsl query type for Song
 */
class QSong(variable: String) : EntityPathBase<Song>(Song::class.java, forVariable(variable)) {

    val album: StringPath = createString("album")
    val artist: StringPath = createString("artist")
    val durationMs: NumberPath<Int> = createNumber("durationMs", Int::class.java)
    val id: NumberPath<Int> = createNumber("id", Int::class.java)
    val name: StringPath = createString("name")

    companion object {
        val song = QSong("song")
    }
}
