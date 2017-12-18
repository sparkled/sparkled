package io.sparkled;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.google.inject.Guice;
import com.google.inject.Injector;
import io.sparkled.udpserver.inject.UdpServerModule;
import io.sparkled.inject.RestApiServerModule;
import io.sparkled.music.inject.MusicPlayerModule;
import io.sparkled.persistence.inject.PersistenceModule;
import io.sparkled.schema.inject.SchemaModule;

public class Main {

    @Parameter(names = {"-rp", "--restPort"}, description = "The REST server port")
    private int restApiPort = 8080;

    @Parameter(names = {"-up", "--udpPort"}, description = "The UDP server port")
    private int udpPort = 12345;

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
                createApp().start(restApiPort, udpPort);
            }
        }
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

    private JCommander buildJCommander() {
        return JCommander.newBuilder().addObject(this).build();
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
