package io.sparkled.udpserver;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public interface RequestHandler {

    void handle(DatagramSocket serverSocket, DatagramPacket receivePacket) throws IOException;
}
