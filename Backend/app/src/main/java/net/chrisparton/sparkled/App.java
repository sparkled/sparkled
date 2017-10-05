package net.chrisparton.sparkled;

import com.google.inject.persist.PersistService;
import net.chrisparton.sparkled.music.SongSchedulerService;
import net.chrisparton.sparkled.persistence.PersistenceService;
import net.chrisparton.sparkled.udpserver.UdpServer;

import javax.inject.Inject;

public class App {

    private static final int REST_API_PORT = 8080;
    private static final int UDP_PORT = 12345;

    private RestApiServer restApiServer;
    private UdpServer udpServer;
    private SongSchedulerService songSchedulerService;

    @Inject
    public App(RestApiServer restApiServer,
               UdpServer udpServer,
               SongSchedulerService songSchedulerService,
               PersistService persistService) throws Exception {

        this.restApiServer = restApiServer;
        this.udpServer = udpServer;
        this.songSchedulerService = songSchedulerService;
        persistService.start();
    }

    public void start() throws Exception {
        PersistenceService.start();
        restApiServer.start(REST_API_PORT);
        udpServer.start(UDP_PORT);
        songSchedulerService.start();
    }
}
