# self-net-copy
Networking course lab 1

To build and run the application, you need Java 17+.

# Building:
```bash
./gradlew jar
```

# Running:

List available network interfaces:
```bash
java -jar build/libs/<jar name>.jar
```

The first argument is group address, the second one is a network interface and the third one is port.
Example:
```bash
java -jar build/libs/<jar name>.jar 230.0.0.0 eth0 8080
```
This command starts the application with group 230.0.0.0 on interface eth0 and at port 8080.
