package io.sparkled;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.google.inject.Guice;
import com.google.inject.Injector;
import io.sparkled.inject.RestApiServerModule;
import io.sparkled.music.inject.MusicPlayerModule;
import io.sparkled.persistence.inject.PersistenceModule;
import io.sparkled.schema.inject.SchemaModule;
import io.sparkled.udpserver.inject.UdpServerModule;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class Main {

    private static final String ROOT_LOG4J_APPENDER_NAME = "file";

    @Parameter(names = {"-rp", "--restPort"}, description = "The REST server port")
    private int restApiPort = 8080;

    @Parameter(names = {"-up", "--udpPort"}, description = "The UDP server port")
    private int udpPort = 12345;

    @Parameter(names = {"-l", "--loglevel"}, description = "Logging level threshold (DEBUG, INFO, WARN, ERROR)")
    private String logLevel = "INFO";

    @Parameter(names = {"-h", "--help"}, description = "Show help", help = true)
    private boolean help = false;

    public static void main(String[] args) throws Exception {
        new Main().run(args);
    }

    private void run(String[] args) throws Exception {
        JCommander jCommander = buildJCommander();
        if (loadCommandLineArguments(jCommander, args)) {
            if (help) {
                jCommander.usage();
            } else {
                setLoggerThreshold();
                createApp().start(restApiPort, udpPort);
            }
        }
    }

    private JCommander buildJCommander() {
        return JCommander.newBuilder().addObject(this).build();
    }

    private boolean loadCommandLineArguments(JCommander jCommander, String[] args) {
        try {
            jCommander.parse(args);
            return true;
        } catch (ParameterException e) {
            System.err.println(e.getMessage());
            e.usage();
            return false;
        }
    }

    private void setLoggerThreshold() {
        Level threshold = Level.toLevel(logLevel);
        ((AppenderSkeleton) Logger.getRootLogger().getAppender(ROOT_LOG4J_APPENDER_NAME)).setThreshold(threshold);
    }

    private App createApp() {
        Injector injector = Guice.createInjector(
                new PersistenceModule(),
                new SchemaModule(),
                new RestApiServerModule(),
                new UdpServerModule(),
                new MusicPlayerModule()
        );

        return injector.getInstance(App.class);
    }
}
