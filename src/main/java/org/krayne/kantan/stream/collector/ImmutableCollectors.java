package org.krayne.kantan.stream.collector;

import com.google.common.collect.ImmutableList;

import java.util.Optional;
import java.util.stream.Collector;

public final class ImmutableCollectors {
    ImmutableCollectors() {}

    public <T> Collector<T, ?, ImmutableList<T>> toList() {
        return Collector.of(
                () -> {
                    ImmutableList.Builder<T> builder = ImmutableList.builder();
                    return builder;
                },
                ImmutableList.Builder::add,
                (l, r) -> l.addAll(r.build()),
                ImmutableList.Builder::build);
    }

    public <T> Collector<Optional<? extends T>, ?, ImmutableList<T>> flatteningOptionals() {
        return Collector.of(
                () -> {
                    ImmutableList.Builder<T> builder = ImmutableList.builder();
                    return builder;
                },
                (acc, opt) -> opt.ifPresent(acc::add),
                (l, r) -> l.addAll(r.build()),
                ImmutableList.Builder::build);
    }
}
