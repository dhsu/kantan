package org.krayne.kantan.future;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.Supplier;

public class RetryingFutureBuilder implements FutureBuilder {
    private static final int DEFAULT_MAX_RETRY_COUNT = 3;
    private static final long DEFAULT_MIN_RETRY_DELAY_MILLIS = 0L;
    private static final long DEFAULT_MAX_RETRY_DELAY_MILLIS = 0L;

    private int maxRetryCount;
    private long minRetryDelayMillis;
    private long maxRetryDelayMillis;

    RetryingFutureBuilder() {
        this.maxRetryCount = DEFAULT_MAX_RETRY_COUNT;
        this.minRetryDelayMillis = DEFAULT_MIN_RETRY_DELAY_MILLIS;
        this.maxRetryDelayMillis = DEFAULT_MAX_RETRY_DELAY_MILLIS;
    }

    public RetryingFutureBuilder withCount(int maxRetryCount) {
        this.maxRetryCount = maxRetryCount;
        return this;
    }

    public RetryingFutureBuilder withDelay(long retryDelayMillis) {
        this.minRetryDelayMillis = retryDelayMillis;
        this.maxRetryDelayMillis = retryDelayMillis;
        return this;
    }

    public RetryingFutureBuilder withDelay(long minRetryDelayMillis, long maxRetryDelayMillis) {
        this.minRetryDelayMillis = minRetryDelayMillis;
        this.maxRetryDelayMillis = maxRetryDelayMillis;
        return this;
    }

    @Override
    public <T> CompletableFuture<T> supplyFuture(Supplier<CompletableFuture<T>> supplier) {
        return supplyFuture(supplier, this.maxRetryCount, this.minRetryDelayMillis, this.maxRetryDelayMillis);
    }

    //------------------------------------------------------------------------------------------------------------------

    private static <T> CompletableFuture<T> supplyFuture(Supplier<CompletableFuture<T>> supplier, int retriesRemaining, long minRetryDelayMillis, long maxRetryDelayMillis) {
        return supplier.get().handle((r, t) -> {
            if (t != null) {
                if (retriesRemaining > 0) {
                    return supplyFuture(supplier, retriesRemaining - 1, minRetryDelayMillis, maxRetryDelayMillis);
                } else {
                    CompletableFuture<T> exceptional = Futures.exceptional(t);
                    return exceptional;
                }
            } else {
                return CompletableFuture.completedFuture(r);
            }
        }).thenCompose(Function.identity());
    }
}
