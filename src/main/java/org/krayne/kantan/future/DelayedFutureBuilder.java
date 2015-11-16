package org.krayne.kantan.future;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class DelayedFutureBuilder implements FutureBuilder {
    private final Optional<Executor> executor;
    private final long delayMillis;

    DelayedFutureBuilder(long delayMillis) {
        this(delayMillis, Optional.empty());
    }

    DelayedFutureBuilder(long delayMillis, Executor executor) {
        this(delayMillis, Optional.of(executor));
    }

    private DelayedFutureBuilder(long delayMillis, Optional<Executor> executor) {
        if (delayMillis < 0) {
            throw new IllegalArgumentException("Delay cannot be negative");
        }
        this.delayMillis = delayMillis;
        this.executor = executor;
    }

    @Override
    public <T> CompletableFuture<T> supplyFuture(Supplier<CompletableFuture<T>> supplier) {
        Runnable delayFunc = () -> delay(this.delayMillis);
        CompletableFuture<Void> delayFuture = this.executor
                .map(e -> CompletableFuture.runAsync(delayFunc, e))
                .orElse(CompletableFuture.runAsync(delayFunc));
        return delayFuture.thenCompose(r -> supplier.get());
    }

    //------------------------------------------------------------------------------------------------------------------

    private static void delay(long millis) {
        if (millis > 0) {
            try {
                TimeUnit.MILLISECONDS.sleep(millis);
            } catch (InterruptedException e) {
            }
        }
    }
}
