package org.krayne.kantan.future;

import org.krayne.kantan.timing.TimeInterval;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
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

    public static DelayedFutureBuilder delayed(long delayMillis, Executor executor) {
        return new DelayedFutureBuilder(delayMillis, executor);
    }

    public static RetryingFutureBuilder retrying() {
        return new RetryingFutureBuilder();
    }

    public static RetryingFutureBuilder retrying(Executor executor) {
        return new RetryingFutureBuilder(executor);
    }

    public static TimingFutureBuilder timing(Consumer<TimeInterval> consumer) {
        return new TimingFutureBuilder(consumer);
    }
}
