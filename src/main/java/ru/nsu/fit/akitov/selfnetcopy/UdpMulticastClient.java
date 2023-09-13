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
    private final Set<String> hosts = new HashSet<>();

    private static final int CAST_INTERVAL = 1000;

    public UdpMulticastClient(InetAddress groupAddress, NetworkInterface netIf, int port) throws IOException {
        this.groupAddress = groupAddress;
        this.port = port;
        socket = new MulticastSocket(port);
        socket.joinGroup(new InetSocketAddress(groupAddress, port), netIf);
        Signal.handle(new Signal("INT"), signal -> leave());
        Signal.handle(new Signal("TERM"), signal -> leave());
    }

    private void cast() throws IOException {
        byte[] buf = Message.CAST.toByteArray();
        DatagramPacket greeting = new DatagramPacket(buf, buf.length, groupAddress, port);
        socket.send(greeting);
    }

    private void leave() {
        byte[] buf = Message.LEAVE.toByteArray();
        DatagramPacket parting = new DatagramPacket(buf, buf.length, groupAddress, port);
        try {
            socket.send(parting);
        } catch (IOException ignore) {
        }
        System.exit(0);
    }

    void sender() {
        try {
            while (true) {
                Thread.sleep(CAST_INTERVAL);
                cast();
            }
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    @Override
    public void run() {
        new Thread(this::sender).start();
        while (true) {
            try {
                byte[] buf = new byte[1024];
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);
                Message message = Message.fromByteArray(buf, 0, packet.getLength());
                switch (message) {
                    case CAST -> hosts.add(packet.getAddress().getHostAddress());
                    case LEAVE -> hosts.remove(packet.getAddress().getHostAddress());
                }
                Runtime.getRuntime().exec("clear");
                System.out.println("\nHosts available:");
                hosts.forEach(System.out::println);
            } catch (IOException e) {
                System.out.println("ERROR: " + e.getMessage());
            } catch (IllegalArgumentException e) {
                System.out.println("ERROR: got an unexpected message");
            }
        }
    }

}
