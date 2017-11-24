package net.chrisparton.sparkled;

import com.google.inject.persist.PersistService;
import net.chrisparton.sparkled.music.SongSchedulerService;
import net.chrisparton.sparkled.schema.SchemaUpdater;
import net.chrisparton.sparkled.udpserver.UdpServer;

import javax.inject.Inject;

public class App {

    private final PersistService persistService;
    private final SchemaUpdater schemaUpdater;
    private final RestApiServer restApiServer;
    private final UdpServer udpServer;
    private final SongSchedulerService songSchedulerService;

    @Inject
    public App(PersistService persistService,
               SchemaUpdater schemaUpdater,
               RestApiServer restApiServer,
               UdpServer udpServer,
               SongSchedulerService songSchedulerService) throws Exception {

        this.persistService = persistService;
        this.restApiServer = restApiServer;
        this.udpServer = udpServer;
        this.songSchedulerService = songSchedulerService;
        this.schemaUpdater = schemaUpdater;
    }

    void start(int restApiPort, int udpPort) throws Exception {
        persistService.start();
        schemaUpdater.update();
        restApiServer.start(restApiPort);
        udpServer.start(udpPort);
        songSchedulerService.start();
    }
}
