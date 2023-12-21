package io.sparkled.persistence.cache.impl

import io.sparkled.persistence.DbService
import io.sparkled.persistence.FileService
import io.sparkled.persistence.cache.Cache
import java.time.Instant

class SongAudiosCache(
    private val db: DbService,
    private val fileService: FileService,
) : Cache<LinkedHashMap<String, ByteArray>>(
    name = "SongAudios",
    fallback = linkedMapOf(),
) {
    override fun reload(lastLoadedAt: Instant?): LinkedHashMap<String, ByteArray> {
        val songs = db.inTransaction { db.songs.findAll() }
        return songs.associateTo(LinkedHashMap(songs.size)) { it.id to fileService.readSongAudio(it.id) }
    }
}
