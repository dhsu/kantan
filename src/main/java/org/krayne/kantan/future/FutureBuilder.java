package org.krayne.kantan.future;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Supplier;

interface FutureBuilder {
    default CompletableFuture<Void> runAsync(Runnable runnable) {
        return supplyFuture(() -> CompletableFuture.runAsync(runnable));
    }

    default CompletableFuture<Void> runAsync(Runnable runnable, Executor executor) {
        return supplyFuture(() -> CompletableFuture.runAsync(runnable, executor));
    }

    default <T> CompletableFuture<T> supplyAsync(Supplier<T> supplier) {
        return supplyFuture(() -> CompletableFuture.supplyAsync(supplier));
    }

    default <T> CompletableFuture<T> supplyAsync(Supplier<T> supplier, Executor executor) {
        return supplyFuture(() -> CompletableFuture.supplyAsync(supplier, executor));
    }

    <T> CompletableFuture<T> supplyFuture(Supplier<CompletableFuture<T>> supplier);
}
