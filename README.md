# Kantan [![Build Status](https://travis-ci.org/dhsu/kantan.svg?branch=master)](https://travis-ci.org/dhsu/kantan)

More core libraries for Java 8+

## Table of Contents

* [Collectors](#collectors)
* [Streams](#streams)
* [Futures](#futures)




## Collectors

So you love [immutability](http://www.javapractices.com/topic/TopicAction.do?Id=29) and you're a
fan of [Guava's immutable collections](https://github.com/google/guava/wiki/ImmutableCollectionsExplained).
You're annoyed that immutable collections are not in the JDK, and frown any time you have to
call [`collect()`](http://docs.oracle.com/javase/8/docs/api/java/util/stream/Stream.html#collect-java.util.stream.Collector-) on a stream...

### Immutable List Collector

This is your basic [`ImmutableList`](http://docs.guava-libraries.googlecode.com/git/javadoc/com/google/common/collect/ImmutableList.html) collector.

#### Example

```java
import org.krayne.kantan.stream.collector.Collectors2

ImmutableList<Integer> collect = Stream.of(1, 2, 3).collect(Collectors2.immutable().toList());
```

### Optional Flattening Immutable List Collector

Use [`Optional`](http://docs.oracle.com/javase/8/docs/api/java/util/Optional.html) much? Sometimes
you have a stream of these and you only want the present values (immutably, of course). Instead of doing:

```java
Stream<Optional<String>> stream = getStream();
List<String> list = stream
    .filter(Optional::isPresent)
    .map(Optional::get)
    .collect(Collectors.toList());
ImmutableList<String> immutableList = ImmutableList.copyOf(list);
```

you can simply do:

```java
Stream<Optional<String>> stream = getStream();
ImmutableList<String> immutableList = stream.collect(Collectors2.immutable().flatteningOptionals());
```




## Streams

Do you constantly find your stream-loving self in possession of `java.lang.Iterable`s and
`java.util.Iterator`s but just want a `java.util.stream.Stream`? Do you go to
[Stack Overflow](http://stackoverflow.com/questions/24511052/java8-iterator-to-stream) every time
you need to do this? How about some simple conversion functions?

```java
Stream.from(someIteratorOrIterable);
```

If you need a [parallel stream](http://zeroturnaround.com/rebellabs/java-parallel-streams-are-bad-for-your-health/),
overloaded methods give you that option.

```java
boolean isParallel = true;
Stream.from(someIteratorOrIterable, isParallel);
```




## Futures

Kantan provides a number of [`CompletableFuture`](http://docs.oracle.com/javase/8/docs/api/java/util/concurrent/CompletableFuture.html)
utility methods.

### Simple Exceptional Future

If you've ever needed a completed, exceptional `CompletableFuture`, you've probably done:

```java
CompletableFuture<?> f = new CompletableFuture<>();
f.completeExceptionally(t);
return f;
```

With Kantan, just return ```Futures.exceptional(t)```

### Delayed Future

Sometimes you just need a `CompletableFuture` that runs at a later time. Maybe you're simulating lag.
Whatever the reason, you can do so easily with `Futures.delayed()`.

```java
// print "ok" after 3 seconds
Futures.delayed(3000L).runAsync(() -> System.out.println("ok"));

// a future that completes after 3 seconds with the string result, "ok"
Futures.delayed(3000L).supplyAsync(() -> "ok");

// a future that delays the future returned by getSomeFuture() by 3 seconds
Futures.delayed(3000L).supplyFuture(() -> getSomeFuture());
```

For more control, you can provide a `java.util.concurrent.Executor` to use for the delaying thread, as in:

```java
Futures.delayed(3000L, someExecutor).supplyAsync(() -> "ok");
```

### Retrying Future

We often want to retry futures a number of times. Perhaps the network was temporarily down, or a
cache write check/set operation failed. With `Futures.retrying()`, we can control simple retry behavior.

```java
// future that will retry the supplied operation up to 3 times.
Futures.retrying()
    .withRetryCount(3)
    .supplyAsync(() -> operationThatMightFail());

// future that will retry the supplied operation up to 3 times, waiting 3 seconds
// between each attempt.
Futures.retrying()
    .withRetryCount(5)
    .withDelay(3000L)
    .supplyAsync(() -> operationThatMightFail());

// future that will retry the supplied operation up to 3 times, waiting a random
// amount of time (from 3 and 10 seconds) between each attempt.
Futures.retrying()
    .withRetryCount(5)
    .withDelay(3000L, 10000L)
    .supplyAsync(() -> operationThatMightFail());
```

As with delayed futures, you can also pass in a `java.util.concurrent.Executor` to use for the retrying
and delaying thread. The retrying future decorator also provides methods to `runAsync()` and
`supplyFuture()`.

### Future with Timing

Sometimes you'll want to time your `CompletableFuture`s. Maybe you want to log a warning if some operation
takes too long. Enter `Futures.timing()`.

```java
Futures.timing(timeInterval -> LOGGER.log("Operation took {} ms", timeInterval.getDuration().toMillis()))
    .runAsync(() -> performSomeOperation());
```