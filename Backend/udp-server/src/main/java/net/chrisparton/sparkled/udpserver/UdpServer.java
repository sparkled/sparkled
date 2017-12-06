package net.chrisparton.sparkled.udpserver;

import java.net.SocketException;

/**
 * A UDP Server that listens on the given port.
 */
public interface UdpServer {

    /**
     * Start the UDP server on the provided port. This method is idempotent.
     *
     * @param port The port to listen on.
     */
    void start(int port) throws SocketException;
}
