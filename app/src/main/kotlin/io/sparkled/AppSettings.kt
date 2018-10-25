package io.sparkled

import com.beust.jcommander.Parameter

/**
 * Settings provided to the application as command line arguments.
 */
internal class AppSettings {

    @Parameter(names = ["-h", "--help"], description = "Show help", help = true)
    var isHelp = false

    @Parameter(names = ["-rp", "--restPort"], description = "The REST server port")
    var restApiPort = 8080

    @Parameter(names = ["-up", "--udpPort"], description = "The UDP server port")
    var udpPort = 12345

    @Parameter(names = ["-l", "--logLevel"], description = "Logging level threshold (DEBUG, INFO, WARN, ERROR)")
    var logLevel = "INFO"
}
