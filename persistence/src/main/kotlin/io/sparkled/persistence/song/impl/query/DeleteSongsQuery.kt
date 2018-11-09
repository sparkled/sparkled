package io.sparkled.persistence.song.impl.query

import io.sparkled.model.entity.QSequence.sequence
import io.sparkled.model.entity.QSong.song
import io.sparkled.model.entity.QSongAudio.songAudio
import io.sparkled.model.util.IdUtils.NO_IDS
import io.sparkled.persistence.PersistenceQuery
import io.sparkled.persistence.QueryFactory
import io.sparkled.persistence.sequence.impl.query.DeleteSequencesQuery
import org.slf4j.LoggerFactory

class DeleteSongsQuery(songIds: Collection<Int>) : PersistenceQuery<Unit> {

    private val songIds: Collection<Int> = if (songIds.isEmpty()) NO_IDS else songIds

    override fun perform(queryFactory: QueryFactory) {
        deleteSequences(queryFactory)
        deleteSongAudios(queryFactory)
        deleteSongs(queryFactory)
    }

    private fun deleteSequences(queryFactory: QueryFactory) {
        val sequenceIds = queryFactory
            .select(sequence.id)
            .from(sequence)
            .where(sequence.songId.`in`(songIds))
            .fetch()
        DeleteSequencesQuery(sequenceIds).perform(queryFactory)
    }

    private fun deleteSongAudios(queryFactory: QueryFactory) {
        val deleted = queryFactory
            .delete(songAudio)
            .where(songAudio.songId.`in`(songIds))
            .execute()

        logger.info("Deleted {} song audio(s).", deleted)
    }

    private fun deleteSongs(queryFactory: QueryFactory) {
        val deleted = queryFactory
            .delete(song)
            .where(song.id.`in`(songIds))
            .execute()

        logger.info("Deleted {} song(s).", deleted)
    }

    companion object {
        private val logger = LoggerFactory.getLogger(DeleteSongsQuery::class.java)
    }
}
