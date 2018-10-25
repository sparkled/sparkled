package io.sparkled;

import com.beust.jcommander.Parameter;

/**
 * Settings provided to the application as command line arguments.
 */
class AppSettings {

    @Parameter(names = {"-h", "--help"}, description = "Show help", help = true)
    private boolean help = false;

    @Parameter(names = {"-rp", "--restPort"}, description = "The REST server port")
    private int restApiPort = 8080;

    @Parameter(names = {"-up", "--udpPort"}, description = "The UDP server port")
    private int udpPort = 12345;

    @Parameter(names = {"-l", "--loglevel"}, description = "Logging level threshold (DEBUG, INFO, WARN, ERROR)")
    private String logLevel = "INFO";

    public int getRestApiPort() {
        return restApiPort;
    }

    public int getUdpPort() {
        return udpPort;
    }

    public String getLogLevel() {
        return logLevel;
    }

    public boolean isHelp() {
        return help;
    }
}
