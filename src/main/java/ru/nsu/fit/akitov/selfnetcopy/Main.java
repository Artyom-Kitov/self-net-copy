package ru.nsu.fit.akitov.selfnetcopy;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Main {

    private static String usage() {
        StringBuilder builder = new StringBuilder()
                .append("""
                usage: ./self-net-copy <ipv4/ipv6 addr> <netIf> <port>
                
                Available interfaces:
                """);
        try {
            NetworkInterface.getNetworkInterfaces()
                    .asIterator()
                    .forEachRemaining(e -> builder.append(e).append("\n"));
        } catch (SocketException e) {
            return "Error: couldn't get available network interfaces";
        }
        return builder.toString();
    }

    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println(usage());
            return;
        }
        try {
            InetAddress groupAddress = InetAddress.getByName(args[0]);
            NetworkInterface networkInterface = NetworkInterface.getByName(args[1]);
            if (networkInterface == null) {
                System.out.println(usage());
                return;
            }
            int port = Integer.parseInt(args[2]);

            UdpMulticastClient client = new UdpMulticastClient(groupAddress,
                    networkInterface, port);
            client.run();
        } catch (UnknownHostException | NumberFormatException e) {
            System.out.println(usage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}