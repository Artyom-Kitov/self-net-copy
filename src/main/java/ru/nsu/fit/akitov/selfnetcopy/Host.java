package ru.nsu.fit.akitov.selfnetcopy;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.Instant;

@Getter
public class Host {

    private final String name;

    @Setter
    Instant lastSeen;

    public Host(String name) {
        this.name = name;
        lastSeen = Instant.now();
    }

    public void updateLastSeen() {
        lastSeen = Instant.now();
    }

    public long lastSeenAgoMillis() {
        return Duration.between(lastSeen, Instant.now()).toMillis();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Host other)) {
            return false;
        }
        return name.equals(other.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

}
