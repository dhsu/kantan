package org.krayne.kantan.future;

import org.krayne.kantan.timing.TimeInterval;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public final class Futures {
    private Futures() {}

    public static <T> CompletableFuture<T> exceptional(Throwable t) {
        CompletableFuture<T> f = new CompletableFuture<>();
        f.completeExceptionally(t);
        return f;
    }

    public static DelayedFutureBuilder delayed(long delayMillis) {
        return new DelayedFutureBuilder(delayMillis);
    }

    public static RetryingFutureBuilder retrying() {
        return new RetryingFutureBuilder();
    }

    public static TimingFutureBuilder timing(Consumer<TimeInterval> consumer) {
        return new TimingFutureBuilder(consumer);
    }
}
