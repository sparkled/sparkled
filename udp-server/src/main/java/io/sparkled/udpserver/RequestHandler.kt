package io.sparkled.udpserver;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * Returns appropriate responses to UDP requests.
 */
public interface RequestHandler {

    void handle(DatagramSocket serverSocket, DatagramPacket receivePacket);
}
