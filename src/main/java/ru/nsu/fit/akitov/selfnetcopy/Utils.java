package ru.nsu.fit.akitov.selfnetcopy;

import lombok.experimental.UtilityClass;

import java.net.NetworkInterface;
import java.net.SocketException;

@UtilityClass
public class Utils {

    public static String usage() {
        return """
                usage: self-net-copy <ipv4/ipv6 addr> <netIf> <port>
                """;
    }

    public static String interfaces() {
        StringBuilder builder = new StringBuilder("Available interfaces:\n");
        try {
            NetworkInterface.getNetworkInterfaces()
                    .asIterator()
                    .forEachRemaining(e -> builder.append(e).append("\n"));
        } catch (SocketException ignore) {
        }
        return builder.toString();
    }

}
