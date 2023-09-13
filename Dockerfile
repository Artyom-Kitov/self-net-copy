FROM openjdk:17-jdk-slim
WORKDIR /app
COPY . /app/.
RUN chmod +x gradlew
RUN ./gradlew jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "build/libs/self-net-copy-1.0-SNAPSHOT.jar", "230.0.0.0", "eth0", "8080"]
