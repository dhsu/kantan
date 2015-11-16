package org.krayne.kantan.timing;

import org.junit.Assert;
import org.junit.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

public class TimeIntervalTest {

    @Test
    public void durationGivenStartAndEnd() {
        TimeInterval timeInterval = new TimeInterval(Instant.ofEpochSecond(1000L), Instant.ofEpochSecond(2000L, 123L));
        long expectedDurationNanos = TimeUnit.SECONDS.toNanos(1000L) + 123L;
        Assert.assertEquals(Duration.ofNanos(expectedDurationNanos), timeInterval.getDuration());
    }

    @Test
    public void endGivenStartAndDuration() {
        TimeInterval timeInterval = new TimeInterval(Instant.ofEpochSecond(1000L), Duration.ofNanos(369));
        Assert.assertEquals(Instant.ofEpochSecond(1000L, 369L), timeInterval.getEnd());
    }

    @Test
    public void endBeforeStart() {
        TimeInterval timeInterval = new TimeInterval(Instant.ofEpochSecond(2000L), Instant.ofEpochSecond(1000L));
        Assert.assertEquals(-1_000_000L, timeInterval.getDuration().toMillis());
    }

    @Test
    public void startEqualsEnd() {
        Instant now = Instant.now();
        TimeInterval timeInterval = new TimeInterval(now, now);
        Assert.assertEquals(Duration.ZERO, timeInterval.getDuration());
    }
}
