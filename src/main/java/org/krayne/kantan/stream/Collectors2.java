package org.krayne.kantan.stream;

import com.google.common.collect.ImmutableList;

import java.util.Optional;
import java.util.stream.Collector;

public final class Collectors2 {
    private Collectors2() {}

    public static <T> Collector<T, ImmutableList.Builder<T>, ImmutableList<T>> toImmutableList() {
        return Collector.of(
                ImmutableList::builder,
                ImmutableList.Builder::add,
                (l, r) -> l.addAll(r.build()),
                ImmutableList.Builder::build);
    }

    public static <T> Collector<Optional<T>, ImmutableList.Builder<T>, ImmutableList<T>> flatteningOptionals() {
        return Collector.of(
                ImmutableList::builder,
                (acc, opt) -> opt.ifPresent(acc::add),
                (l, r) -> l.addAll(r.build()),
                ImmutableList.Builder::build);
    }
}
