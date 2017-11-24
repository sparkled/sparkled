package net.chrisparton.sparkled;

import com.google.inject.Guice;
import com.google.inject.Injector;
import net.chrisparton.sparkled.inject.RestApiServerModule;
import net.chrisparton.sparkled.music.inject.MusicPlayerModule;
import net.chrisparton.sparkled.persistence.inject.PersistenceModule;
import net.chrisparton.sparkled.schema.inject.SchemaModule;
import net.chrisparton.sparkled.udpserver.inject.UdpServerModule;

public class Main {
    public static void main(String[] args) throws Exception {
        Injector injector = Guice.createInjector(
                new PersistenceModule(),
                new SchemaModule(),
                new RestApiServerModule(),
                new UdpServerModule(),
                new MusicPlayerModule()
        );

        App app = injector.getInstance(App.class);
        app.start();
    }
}
