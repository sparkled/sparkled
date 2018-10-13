package io.sparkled;

import com.google.inject.persist.PersistService;
import io.sparkled.schema.SchemaUpdater;
import io.sparkled.udpserver.UdpServer;

import javax.inject.Inject;

public class App {

    private final PersistService persistService;
    private final SchemaUpdater schemaUpdater;
    private final RestApiServer restApiServer;
    private final UdpServer udpServer;

    @Inject
    public App(PersistService persistService,
               SchemaUpdater schemaUpdater,
               RestApiServer restApiServer,
               UdpServer udpServer) throws Exception {

        this.persistService = persistService;
        this.restApiServer = restApiServer;
        this.udpServer = udpServer;
        this.schemaUpdater = schemaUpdater;
    }

    void start(AppSettings settings) throws Exception {
        persistService.start();
        schemaUpdater.update();
        restApiServer.start(settings.getRestApiPort());
        udpServer.start(settings.getUdpPort());
    }
}
