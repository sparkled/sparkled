package net.chrisparton.sparkled;

import com.google.inject.persist.PersistService;
import net.chrisparton.sparkled.music.SongSchedulerService;
import net.chrisparton.sparkled.udpserver.UdpServer;

import javax.inject.Inject;

public class App {

    private static final int REST_API_PORT = 8080;
    private static final int UDP_PORT = 12345;

    private final PersistService persistService;
    private final RestApiServer restApiServer;
    private final UdpServer udpServer;
    private final SongSchedulerService songSchedulerService;

    @Inject
    public App(PersistService persistService,
               RestApiServer restApiServer,
               UdpServer udpServer,
               SongSchedulerService songSchedulerService) throws Exception {

        this.persistService = persistService;
        this.restApiServer = restApiServer;
        this.udpServer = udpServer;
        this.songSchedulerService = songSchedulerService;
    }

    public void start() throws Exception {
        persistService.start();
        restApiServer.start(REST_API_PORT);
        udpServer.start(UDP_PORT);
        songSchedulerService.start();
    }
}
