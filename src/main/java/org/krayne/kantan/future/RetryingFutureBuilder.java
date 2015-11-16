package org.krayne.kantan.future;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Function;
import java.util.function.Supplier;

public class RetryingFutureBuilder implements FutureBuilder {
    private static final int DEFAULT_MAX_RETRY_COUNT = 3;
    private static final long DEFAULT_MIN_RETRY_DELAY_MILLIS = 0L;
    private static final long DEFAULT_MAX_RETRY_DELAY_MILLIS = 0L;
    private static final Random RANDOM = new Random();

    private final Optional<Executor> executor;
    private int maxRetryCount;
    private long minRetryDelayMillis;
    private long maxRetryDelayMillis;

    RetryingFutureBuilder() {
        this(Optional.empty());
    }

    RetryingFutureBuilder(Executor executor) {
        this(Optional.of(executor));
    }

    private RetryingFutureBuilder(Optional<Executor> executor) {
        this.executor = executor;
        this.maxRetryCount = DEFAULT_MAX_RETRY_COUNT;
        this.minRetryDelayMillis = DEFAULT_MIN_RETRY_DELAY_MILLIS;
        this.maxRetryDelayMillis = DEFAULT_MAX_RETRY_DELAY_MILLIS;
    }

    public RetryingFutureBuilder withRetryCount(int maxRetryCount) {
        this.maxRetryCount = maxRetryCount;
        return this;
    }

    public RetryingFutureBuilder withDelay(long retryDelayMillis) {
        if (retryDelayMillis < 0) {
            throw new IllegalArgumentException("Retry delay cannot be negative");
        }
        this.minRetryDelayMillis = retryDelayMillis;
        this.maxRetryDelayMillis = retryDelayMillis;
        return this;
    }

    public RetryingFutureBuilder withDelay(long minRetryDelayMillis, long maxRetryDelayMillis) {
        if (minRetryDelayMillis < 0 || maxRetryDelayMillis < 0) {
            throw new IllegalArgumentException("Retry delay cannot be negative");
        } else if (minRetryDelayMillis > maxRetryDelayMillis) {
            throw new IllegalArgumentException("Min retry delay cannot be greater than max");
        }
        this.minRetryDelayMillis = minRetryDelayMillis;
        this.maxRetryDelayMillis = maxRetryDelayMillis;
        return this;
    }

    @Override
    public <T> CompletableFuture<T> supplyFuture(Supplier<CompletableFuture<T>> supplier) {
        return this.supplyFuture(supplier, this.maxRetryCount, this.minRetryDelayMillis, this.maxRetryDelayMillis);
    }

    //------------------------------------------------------------------------------------------------------------------

    private <T> CompletableFuture<T> supplyFuture(Supplier<CompletableFuture<T>> supplier, int retriesRemaining, long minRetryDelayMillis, long maxRetryDelayMillis) {
        return supplier.get().handle((r, t) -> {
            if (t != null) {
                return this.getRetryFuture(supplier, t, retriesRemaining, minRetryDelayMillis, maxRetryDelayMillis);
            } else {
                return CompletableFuture.completedFuture(r);
            }
        }).thenCompose(Function.identity());
    }

    private <T> CompletableFuture<T> getRetryFuture(Supplier<CompletableFuture<T>> supplier, Throwable t, int retriesRemaining, long minRetryDelayMillis, long maxRetryDelayMillis) {
        if (retriesRemaining > 0) {
            long delayMillis = this.nextDelayMillis(minRetryDelayMillis, maxRetryDelayMillis);
            if (delayMillis > 0) {
                if (this.executor.isPresent()) {
                    return Futures.delayed(delayMillis, executor.get())
                            .supplyFuture(() -> this.supplyFuture(supplier, retriesRemaining - 1, minRetryDelayMillis, maxRetryDelayMillis));
                } else {
                    return Futures.delayed(delayMillis)
                            .supplyFuture(() -> this.supplyFuture(supplier, retriesRemaining - 1, minRetryDelayMillis, maxRetryDelayMillis));
                }
            } else {
                return supplyFuture(supplier, retriesRemaining - 1, minRetryDelayMillis, maxRetryDelayMillis);
            }
        } else {
            CompletableFuture<T> exceptional = Futures.exceptional(t);
            return exceptional;
        }
    }

    private long nextDelayMillis(long min, long max) {
        return max - min <= 0 ? min : (RANDOM.nextLong() % (max - min)) + min;
    }
}
