package net.chrisparton.sparkled;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.google.inject.Guice;
import com.google.inject.Injector;
import net.chrisparton.sparkled.inject.RestApiServerModule;
import net.chrisparton.sparkled.music.inject.MusicPlayerModule;
import net.chrisparton.sparkled.persistence.inject.PersistenceModule;
import net.chrisparton.sparkled.schema.inject.SchemaModule;
import net.chrisparton.sparkled.udpserver.inject.UdpServerModule;

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
