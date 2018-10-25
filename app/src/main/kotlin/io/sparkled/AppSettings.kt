package io.sparkled

import com.beust.jcommander.Parameter

/**
 * Settings provided to the application as command line arguments.
 */
internal class AppSettings {

    @Parameter(names = { "-h", "--help" }, description = "Show help", help = true)
    val isHelp = false

    @Parameter(names = { "-rp", "--restPort" }, description = "The REST server port")
    val restApiPort = 8080

    @Parameter(names = { "-up", "--udpPort" }, description = "The UDP server port")
    val udpPort = 12345

    @Parameter(names = { "-l", "--loglevel" }, description = "Logging level threshold (DEBUG, INFO, WARN, ERROR)")
    val logLevel = "INFO"
}
