package org.krayne.kantan.stream;

import com.google.common.collect.ImmutableList;
import org.junit.Assert;
import org.junit.Test;

import java.util.stream.IntStream;

public class Collectors2Test {

    @Test
    public void basicImmutableListCollector() {
        ImmutableList<Integer> list = IntStream.range(0, 3).mapToObj(i -> i).collect(Collectors2.toImmutableList());
        Assert.assertEquals(ImmutableList.of(0, 1, 2), list);
    }
}
