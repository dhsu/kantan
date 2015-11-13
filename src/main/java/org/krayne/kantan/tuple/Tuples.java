package org.krayne.kantan.tuple;

public final class Tuples {
    private Tuples() {}

    public static <A, B> Tuple2<A, B> of(A t1, B t2) {
        return new Tuple2<>(t1, t2);
    }
}
