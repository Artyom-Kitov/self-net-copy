package ru.nsu.fit.akitov.selfnetcopy;

import lombok.Builder;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

@Builder
public record Arguments(InetAddress groupAddress,
                        NetworkInterface networkInterface,
                        int port) {
    public static Arguments buildFromArray(String[] args) {
        if (args.length != 3) {
            throw new IllegalArgumentException("wrong number of parameters");
        }
        try {
            InetAddress groupAddress = InetAddress.getByName(args[0]);
            NetworkInterface netIf = NetworkInterface.getByName(args[1]);
            if (netIf == null) {
                throw new IllegalArgumentException("wrong network interface");
            }
            int port = Integer.parseInt(args[2]);
            return Arguments.builder()
                    .groupAddress(groupAddress)
                    .networkInterface(netIf)
                    .port(port)
                    .build();
        } catch (UnknownHostException e) {
            throw new IllegalArgumentException("wrong group address");
        } catch (SocketException e) {
            throw new IllegalArgumentException("wrong network interface");
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("wrong port");
        }
    }

}
