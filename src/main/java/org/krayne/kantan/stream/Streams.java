package org.krayne.kantan.stream;

import java.util.Iterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public final class Streams {
    private Streams() {}

    public static <T> Stream<T> from(Iterator<T> iterator) {
        return from(iterator, false);
    }

    public static <T> Stream<T> from(Iterator<T> iterator, boolean parallel) {
        Iterable<T> iterable = () -> iterator;
        return from(iterable, parallel);
    }

    public static <T> Stream<T> from(Iterable<T> iterable) {
        return from(iterable, false);
    }

    public static <T> Stream<T> from(Iterable<T> iterable, boolean parallel) {
        return StreamSupport.stream(iterable.spliterator(), parallel);
    }
}
