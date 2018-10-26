package io.sparkled

import com.beust.jcommander.JCommander
import com.beust.jcommander.ParameterException
import com.google.inject.Guice
import io.sparkled.music.inject.MusicPlayerModule
import io.sparkled.persistence.inject.PersistenceModule
import io.sparkled.rest.inject.RestApiServerModule
import io.sparkled.schema.inject.SchemaModule
import io.sparkled.udpserver.inject.UdpServerModule
import org.apache.log4j.AppenderSkeleton
import org.apache.log4j.Level
import org.slf4j.LoggerFactory

class Main {

    @Throws(Exception::class)
    private fun run(args: Array<String>) {
        val settings = AppSettings()
        val jCommander = buildJCommander(settings)

        if (loadCommandLineArguments(jCommander, args)) {
            if (settings.isHelp) {
                jCommander.usage()
            } else {
                setLoggerThreshold(settings.logLevel)
                createApp().start(settings)
            }
        }
    }

    private fun buildJCommander(settings: AppSettings): JCommander {
        return JCommander.newBuilder().addObject(settings).build()
    }

    private fun loadCommandLineArguments(jCommander: JCommander, args: Array<String>): Boolean {
        return try {
            jCommander.parse(*args)
            true
        } catch (e: ParameterException) {
            logger.error(e.message)
            e.usage()
            false
        }
    }

    private fun setLoggerThreshold(level: String) {
        val threshold = Level.toLevel(level)
        val rootLogger = org.apache.log4j.Logger.getRootLogger()
        (rootLogger.getAppender(ROOT_LOG4J_APPENDER_NAME) as AppenderSkeleton).threshold = threshold
    }

    private fun createApp(): App {
        val injector = Guice.createInjector(
                PersistenceModule(),
                SchemaModule(),
                RestApiServerModule(),
                UdpServerModule(),
                MusicPlayerModule()
        )

        return injector.getInstance(App::class.java)
    }

    companion object {
        private val logger = LoggerFactory.getLogger(Main::class.java)
        private const val ROOT_LOG4J_APPENDER_NAME = "file"

        @JvmStatic
        fun main(args: Array<String>) {
            Main().run(args)
        }
    }
}
