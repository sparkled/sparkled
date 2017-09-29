package net.chrisparton.sparkled.udpserver;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UdpServer {

    private static final Logger logger = Logger.getLogger(UdpServer.class.getName());
    private static final int SERVER_PORT = 12345;
    private static final int RECEIVE_BUFFER_SIZE = 16;
    private static final ExecutorService threadPool = Executors.newFixedThreadPool(1);
    private static final RequestHandler requestHandler = new RequestHandler();

    public static void start() throws SocketException {
        DatagramSocket serverSocket = new DatagramSocket(SERVER_PORT);
        logger.info("Created socket at port " + SERVER_PORT);

        threadPool.submit(() -> {
            byte[] receiveData = new byte[RECEIVE_BUFFER_SIZE];

            while (true) {
                try {
                    handleRequest(serverSocket, receiveData);
                } catch (IOException e) {
                    logger.log(Level.SEVERE, "Failed to handle UDP request.", e);
                }
            }
        });
    }

    private static void handleRequest(final DatagramSocket serverSocket, final byte[] receiveData) throws IOException {
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        serverSocket.receive(receivePacket);
        requestHandler.handle(serverSocket, receivePacket);
    }
}
