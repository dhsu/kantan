package org.krayne.kantan.stream;

import com.google.common.collect.ImmutableList;
import org.junit.Assert;
import org.junit.Test;
import org.krayne.kantan.stream.collector.Collectors2;

import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class ImmutableCollectorsTest {

    @Test
    public void toListBasic() {
        ImmutableList<Integer> list = IntStream.range(0, 3).mapToObj(i -> i).collect(Collectors2.immutable().toList());
        Assert.assertEquals(ImmutableList.of(0, 1, 2), list);
    }

    @Test
    public void toListEmpty() {
        ImmutableList<Object> list = Stream.empty().collect(Collectors2.immutable().toList());
        Assert.assertEquals(ImmutableList.of(), list);
    }

    @Test
    public void flatteningOptionalsBasic() {
        Stream<Optional<String>> stream = Stream.of(Optional.of("horus"), Optional.of("set"), Optional.empty(), Optional.of("bastet"), Optional.empty());
        ImmutableList<String> list = stream.collect(Collectors2.immutable().flatteningOptionals());
        Assert.assertEquals(ImmutableList.of("horus", "set", "bastet"), list);
    }

    @Test
    public void flatteningOptionalsAllEmpty() {
        Stream<Optional<String>> stream = Stream.of(Optional.empty(), Optional.empty());
        ImmutableList<String> list = stream.collect(Collectors2.immutable().flatteningOptionals());
        Assert.assertEquals(0, list.size());
    }
}
