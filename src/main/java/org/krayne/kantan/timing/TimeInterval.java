package org.krayne.kantan.timing;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class TimeInterval {
    private final Instant start;
    private final Duration duration;

    public TimeInterval(Instant start, Instant end) {
        this.start = start;
        long durationNanos = this.start.until(end, ChronoUnit.NANOS);
        this.duration = Duration.ofNanos(durationNanos);
    }

    public TimeInterval(Instant start, Duration duration) {
        this.start = start;
        this.duration = duration;
    }

    public Instant getStart() {
        return this.start;
    }

    public Instant getEnd() {
        return this.start.plus(this.duration);
    }

    public Duration getDuration() {
        return this.duration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TimeInterval that = (TimeInterval) o;

        if (!start.equals(that.start)) return false;
        return duration.equals(that.duration);
    }

    @Override
    public int hashCode() {
        int result = start.hashCode();
        result = 31 * result + duration.hashCode();
        return result;
    }
}
