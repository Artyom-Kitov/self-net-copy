package ru.nsu.fit.akitov.selfnetcopy;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        Arguments arguments;
        try {
            arguments = Arguments.buildFromArray(args);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage() + "\n" + Utils.usage() + "\n"  + Utils.interfaces());
            return;
        }
        try {
            UdpMulticastClient client = new UdpMulticastClient(arguments.groupAddress(),
                    arguments.networkInterface(), arguments.port());
            UdpMulticastBeacon beacon = new UdpMulticastBeacon(arguments.groupAddress(),
                    arguments.networkInterface(), arguments.port());
            new Thread(beacon).start();
            client.run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}