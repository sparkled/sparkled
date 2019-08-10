package io.sparkled.model.entity

import com.querydsl.core.types.PathMetadataFactory.forVariable
import com.querydsl.core.types.dsl.ArrayPath
import com.querydsl.core.types.dsl.EntityPathBase
import com.querydsl.core.types.dsl.NumberPath

/**
 * QSongAudio is a Querydsl query type for SongAudio
 */
class QSongAudio(variable: String) : EntityPathBase<SongAudio>(SongAudio::class.java, forVariable(variable)) {

    val audioData: ArrayPath<ByteArray, Byte> = createArray("audioData", ByteArray::class.java)
    val songId: NumberPath<Int> = createNumber("songId", Int::class.java)

    companion object {
        val songAudio = QSongAudio("songAudio")
    }
}
