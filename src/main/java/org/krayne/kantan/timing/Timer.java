package org.krayne.kantan.timing;

import java.time.Instant;

public class Timer {
    private final Instant start;

    public static Timer start() {
        return new Timer(Instant.now());
    }

    private Timer(Instant start) {
        this.start = start;
    }

    public TimeInterval read() {
        return new TimeInterval(this.start, Instant.now());
    }
}
