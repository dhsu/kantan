package org.krayne.kantan.future;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class DelayedFutureBuilder implements FutureBuilder {
    private final long delayMillis;

    DelayedFutureBuilder(long delayMillis) {
        this.delayMillis = delayMillis;
    }

    @Override
    public <T> CompletableFuture<T> supplyFuture(Supplier<CompletableFuture<T>> supplier) {
        CompletableFuture<T> future = new CompletableFuture<>();
        Timer timer = new Timer();
        timer.schedule(new FutureTimerTask<>(supplier, future), this.delayMillis);
        return future;
    }

    //------------------------------------------------------------------------------------------------------------------

    private static class FutureTimerTask<T> extends TimerTask {
        private final Supplier<CompletableFuture<T>> supplier;
        private final CompletableFuture<T> future;

        public FutureTimerTask(Supplier<CompletableFuture<T>> supplier, CompletableFuture<T> future) {
            this.supplier = supplier;
            this.future = future;
        }

        @Override
        public void run() {
            this.supplier.get().whenComplete((r, t) -> {
                if (t != null) {
                    this.future.completeExceptionally(t);
                } else {
                    this.future.complete(r);
                }
            });
        }
    }
}
