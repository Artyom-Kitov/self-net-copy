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

    public UdpMulticastClient(InetAddress groupAddress, NetworkInterface netIf, int port) throws IOException {
        this.groupAddress = groupAddress;
        this.port = port;
        socket = new MulticastSocket(port);
        socket.joinGroup(new InetSocketAddress(groupAddress, port), netIf);
        Signal.handle(new Signal("INT"), signal -> goodbye());
    }

    private void hello() throws IOException {
        byte[] buf = Message.ENTER.toByteArray();
        DatagramPacket greeting = new DatagramPacket(buf, buf.length, groupAddress, port);
        socket.send(greeting);
    }

    private void goodbye() {
        byte[] buf = Message.EXIT.toByteArray();
        DatagramPacket parting = new DatagramPacket(buf, buf.length, groupAddress, port);
        try {
            socket.send(parting);
        } catch (IOException ignore) {
        }
    }

    @Override
    public void run() {
        try {
            hello();
        } catch (IOException e) {
            System.out.println("ERROR: " + e.getMessage());
            System.exit(-1);
        }

        while (true) {
            try {
                byte[] buf = new byte[1024];
                DatagramPacket echo = new DatagramPacket(buf, buf.length);
                socket.receive(echo);
                Message message = Message.fromByteArray(buf, 0, echo.getLength());
                switch (message) {
                    case ENTER -> hosts.add(echo.getAddress().getHostAddress());
                    case EXIT -> hosts.remove(echo.getAddress().getHostAddress());
                }
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
