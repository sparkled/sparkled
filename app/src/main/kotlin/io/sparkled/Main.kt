package io.sparkled

import com.beust.jcommander.JCommander
import com.beust.jcommander.ParameterException
import com.google.inject.Guice
import com.google.inject.Injector
import io.sparkled.music.inject.MusicPlayerModule
import io.sparkled.persistence.inject.PersistenceModule
import io.sparkled.rest.inject.RestApiServerModule
import io.sparkled.schema.inject.SchemaModule
import io.sparkled.udpserver.inject.UdpServerModule
import org.apache.log4j.AppenderSkeleton
import org.apache.log4j.Level
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class Main {

    @Throws(Exception::class)
    private fun run(args: Array<String>) {
        val settings = AppSettings()
        val jCommander = buildJCommander(settings)

        if (loadCommandLineArguments(jCommander, args)) {
            if (settings.isHelp()) {
                jCommander.usage()
            } else {
                setLoggerThreshold(settings.getLogLevel())
                createApp().start(settings)
            }
        }
    }

    private fun buildJCommander(settings: AppSettings): JCommander {
        return JCommander.newBuilder().addObject(settings).build()
    }

    private fun loadCommandLineArguments(jCommander: JCommander, args: Array<String>): Boolean {
        try {
            jCommander.parse(args)
            return true
        } catch (e: ParameterException) {
            logger.error(e.getMessage())
            e.usage()
            return false
        }

    }

    private fun setLoggerThreshold(level: String) {
        val threshold = Level.toLevel(level)
        val rootLogger = org.apache.log4j.Logger.getRootLogger()
        (rootLogger.getAppender(ROOT_LOG4J_APPENDER_NAME) as AppenderSkeleton).setThreshold(threshold)
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
        private val ROOT_LOG4J_APPENDER_NAME = "file"

        @Throws(Exception::class)
        fun main(args: Array<String>) {
            Main().run(args)
        }
    }
}
