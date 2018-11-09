package io.sparkled.persistence.song.impl.query

import io.sparkled.model.entity.QSong.song
import io.sparkled.model.entity.Song
import io.sparkled.persistence.PersistenceQuery
import io.sparkled.persistence.QueryFactory

class GetSongByIdQuery(private val songId: Int) : PersistenceQuery<Song?> {

    override fun perform(queryFactory: QueryFactory): Song? {
        return queryFactory
            .selectFrom(song)
            .where(song.id.eq(songId))
            .fetchFirst()
    }
}
