package org.krayne.kantan.stream.collector;

public final class Collectors2 {
    private Collectors2() {}

    private static final ImmutableCollectors IMMUTABLE_COLLECTORS = new ImmutableCollectors();

    public static ImmutableCollectors immutable() {
        return IMMUTABLE_COLLECTORS;
    }
}
