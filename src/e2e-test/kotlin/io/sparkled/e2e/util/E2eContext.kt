package io.sparkled.e2e.util

import com.fasterxml.jackson.databind.ObjectMapper
import common.logging.getLogger
import io.micronaut.context.ApplicationContext
import io.micronaut.runtime.server.EmbeddedServer
import io.sparkled.persistence.DbService
import io.sparkled.persistence.cache.CacheService

object E2eContext {
    private val logger = getLogger<E2eContext>()
    private val appContext: ApplicationContext
    private val jsonMapper: ObjectMapper
    val embeddedServer: EmbeddedServer
    val caches: CacheService
    val db: DbService

    init {
        val properties = hashMapOf<String, Any>(
            "datasources.default.url" to "jdbc:sqlite::memory:",
        )

        embeddedServer = ApplicationContext.run(EmbeddedServer::class.java, properties, "e2eTest")
        appContext = embeddedServer.applicationContext
        caches = inject()
        db = inject()
        jsonMapper = inject()
    }

    inline fun <reified T : Any> inject(): T {
        return embeddedServer.applicationContext.findBean(T::class.java).get()
    }

    /**
     * Used to ensure this class has been initialised for end-to-end tests.
     */
    fun begin() {
        logger.info("Started E2E test.")
    }

    /**
     * Cleans up test data once a test spec has finished running.
     */
    fun end() {
        logger.info("Resetting database.")

        db.scheduledActions.deleteAll()
        db.settings.deleteAll()
        db.playlistSequences.deleteAll()
        db.sequenceChannels.deleteAll()
        db.sequences.deleteAll()
        db.stageProps.deleteAll()
        db.stages.deleteAll()
        db.songs.deleteAll()
        db.playlists.deleteAll()

        logger.info("Finished E2E test.")
    }
}
