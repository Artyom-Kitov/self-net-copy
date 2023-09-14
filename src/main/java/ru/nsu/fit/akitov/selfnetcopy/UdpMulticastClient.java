package ru.nsu.fit.akitov.selfnetcopy;

import sun.misc.Signal;

import java.io.*;
import java.net.*;
import java.util.HashSet;
import java.util.Set;

public class UdpMulticastClient implements Runnable {

    private final InetAddress groupAddress;
    private final int port;
    private final MulticastSocket socket;
    private final Set<Host> hosts = new HashSet<>();

    private static final int CLIENT_TTL_MILLIS = 2000;

    public UdpMulticastClient(InetAddress groupAddress, NetworkInterface netIf, int port) throws IOException {
        this.groupAddress = groupAddress;
        this.port = port;
        socket = new MulticastSocket(port);
        socket.joinGroup(new InetSocketAddress(groupAddress, port), netIf);
    }

    private void updateHosts() {
        Set<Host> copy = new HashSet<>(hosts);
        hosts.clear();
        copy.stream()
                .filter(host -> host.lastSeenAgoMillis() < CLIENT_TTL_MILLIS)
                .forEach(hosts::add);
    }

    @Override
    public void run() {
        while (true) {
            try {
                byte[] buf = new byte[1024];
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);

                String message = new String(buf, 0, packet.getLength());
                if (!message.equals(UdpMulticastBeacon.MESSAGE)) {
                    continue;
                }

                updateHosts();
                hosts.add(new Host(packet.getAddress().getHostAddress()));
                System.out.println("\nHosts available:");
                hosts.forEach(host -> System.out.println(host.getName()));
            } catch (IOException e) {
                System.out.println("ERROR: " + e.getMessage());
            }
        }
    }

}
