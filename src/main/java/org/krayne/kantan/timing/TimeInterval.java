package org.krayne.kantan.timing;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class TimeInterval {
    private final Instant start;
    private final Instant end;
    private final Duration duration;

    public TimeInterval(Instant start, Instant end) {
        this.start = start;
        this.end = end;
        long durationNanos = this.start.until(this.end, ChronoUnit.NANOS);
        this.duration = Duration.ofNanos(durationNanos);
    }

    public TimeInterval(Instant start, Duration duration) {
        this.start = start;
        this.duration = duration;
        this.end = this.start.plusNanos(duration.toNanos());
    }

    public Instant getStart() {
        return this.start;
    }

    public Instant getEnd() {
        return this.end;
    }

    public Duration getDuration() {
        return this.duration;
    }
}
