package org.krayne.kantan.tuple;

public class Tuple2<A, B> {
    private final A v1;
    private final B v2;

    public static <A, B> Tuple2<A, B> of(A v1, B v2) {
        return new Tuple2<A, B>(v1, v2);
    }

    private Tuple2(A v1, B v2) {
        this.v1 = v1;
        this.v2 = v2;
    }

    public A v1() {
        return this.v1;
    }

    public B v2() {
        return this.v2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tuple2<?, ?> tuple2 = (Tuple2<?, ?>) o;

        if (v1 != null ? !v1.equals(tuple2.v1) : tuple2.v1 != null) return false;
        return !(v2 != null ? !v2.equals(tuple2.v2) : tuple2.v2 != null);
    }

    @Override
    public int hashCode() {
        int result = v1 != null ? v1.hashCode() : 0;
        result = 31 * result + (v2 != null ? v2.hashCode() : 0);
        return result;
    }
}
