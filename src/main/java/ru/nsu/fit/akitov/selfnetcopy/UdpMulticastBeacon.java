package ru.nsu.fit.akitov.selfnetcopy;

import java.io.IOException;
import java.net.*;

public class UdpMulticastBeacon implements Runnable {

    private final InetAddress groupAddress;
    private final int port;
    private final MulticastSocket socket;

    private static final int CAST_INTERVAL = 1000;
    public static final String MESSAGE = "Hi!";

    public UdpMulticastBeacon(InetAddress groupAddress, NetworkInterface netIf, int port) throws IOException {
        this.groupAddress = groupAddress;
        this.port = port;
        socket = new MulticastSocket(port);
        socket.joinGroup(new InetSocketAddress(groupAddress, port), netIf);
    }

    private void cast() throws IOException {
        byte[] buf = MESSAGE.getBytes();
        DatagramPacket greeting = new DatagramPacket(buf, buf.length, groupAddress, port);
        socket.send(greeting);
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(CAST_INTERVAL);
                cast();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
