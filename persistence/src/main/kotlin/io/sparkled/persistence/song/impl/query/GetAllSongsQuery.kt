package io.sparkled.persistence.song.impl.query

import io.sparkled.model.entity.QSong.Companion.song
import io.sparkled.model.entity.Song
import io.sparkled.persistence.PersistenceQuery
import io.sparkled.persistence.QueryFactory

class GetAllSongsQuery : PersistenceQuery<List<Song>> {

    override fun perform(queryFactory: QueryFactory): List<Song> {
        return queryFactory
            .selectFrom(song)
            .orderBy(
                song.name.asc(),
                song.album.asc(),
                song.artist.asc()
            )
            .fetch()
    }
}
