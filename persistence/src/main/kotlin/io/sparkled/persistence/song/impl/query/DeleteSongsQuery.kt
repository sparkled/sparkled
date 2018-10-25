package io.sparkled.persistence.song.impl.query

import io.sparkled.persistence.PersistenceQuery
import io.sparkled.persistence.QueryFactory
import io.sparkled.persistence.sequence.impl.query.DeleteSequencesQuery
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class DeleteSongsQuery(songIds: Collection<Integer>) : PersistenceQuery<Void> {

    private val songIds: Collection<Integer>

    init {
        this.songIds = if (songIds.isEmpty()) noIds else songIds
    }

    @Override
    fun perform(queryFactory: QueryFactory): Void? {
        deleteSequences(queryFactory)
        deleteSongAudios(queryFactory)
        deleteSongs(queryFactory)
        return null
    }

    private fun deleteSequences(queryFactory: QueryFactory) {
        val sequenceIds = queryFactory
                .select(qSequence.id)
                .from(qSequence)
                .where(qSequence.songId.`in`(songIds))
                .fetch()
        DeleteSequencesQuery(sequenceIds).perform(queryFactory)
    }

    private fun deleteSongAudios(queryFactory: QueryFactory) {
        val deleted = queryFactory
                .delete(qSongAudio)
                .where(qSongAudio.songId.`in`(songIds))
                .execute()

        logger.info("Deleted {} song audio(s).", deleted)
    }

    private fun deleteSongs(queryFactory: QueryFactory) {
        val deleted = queryFactory
                .delete(qSong)
                .where(qSong.id.`in`(songIds))
                .execute()

        logger.info("Deleted {} song(s).", deleted)
    }

    companion object {

        private val logger = LoggerFactory.getLogger(DeleteSongsQuery::class.java)
    }
}
