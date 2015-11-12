package org.krayne.kantan.future;

import org.krayne.kantan.timing.TimeInterval;
import org.krayne.kantan.timing.Timer;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class TimingFutureBuilder implements FutureBuilder {
    private final Consumer<TimeInterval> timingConsumer;

    TimingFutureBuilder(Consumer<TimeInterval> timingConsumer) {
        this.timingConsumer = timingConsumer;
    }

    @Override
    public <T> CompletableFuture<T> supplyFuture(Supplier<CompletableFuture<T>> supplier) {
        return CompletableFuture.supplyAsync(Timer::start)
                .thenCompose(timer -> supplier.get().whenComplete((r, t) -> timingConsumer.accept(timer.read())));
    }
}
