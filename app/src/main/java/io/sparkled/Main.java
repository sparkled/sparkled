package io.sparkled;

import com.beust.jcommander.JCommander;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private static final String ROOT_LOG4J_APPENDER_NAME = "file";

    public static void main(String[] args) throws Exception {
        new Main().run(args);
    }

    private void run(String[] args) throws Exception {
        AppSettings settings = new AppSettings();
        JCommander jCommander = buildJCommander(settings);

        if (loadCommandLineArguments(jCommander, args)) {
            if (settings.isHelp()) {
                jCommander.usage();
            } else {
                setLoggerThreshold(settings.getLogLevel());
                createApp().start(settings);
            }
        }
    }

    private JCommander buildJCommander(AppSettings settings) {
        return JCommander.newBuilder().addObject(settings).build();
    }

    private boolean loadCommandLineArguments(JCommander jCommander, String[] args) {
        try {
            jCommander.parse(args);
            return true;
        } catch (ParameterException e) {
            logger.error(e.getMessage());
            e.usage();
            return false;
        }
    }

    private void setLoggerThreshold(String level) {
        Level threshold = Level.toLevel(level);
        org.apache.log4j.Logger rootLogger = org.apache.log4j.Logger.getRootLogger();
        ((AppenderSkeleton) rootLogger.getAppender(ROOT_LOG4J_APPENDER_NAME)).setThreshold(threshold);
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
