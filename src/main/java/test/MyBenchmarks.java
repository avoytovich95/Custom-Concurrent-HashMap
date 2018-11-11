package test;

import enums.Items;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import custom.hash.CustomHash;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;


public class MyBenchmarks {

  @State(Scope.Thread)
  public static class MyHash {
    CustomHash<Items> myHash = new CustomHash<Items>();
    Items[] items = Items.values();
    int elements = items.length;
  }

  @Benchmark
  @BenchmarkMode({Mode.Throughput, Mode.AverageTime})
  @OutputTimeUnit(TimeUnit.MILLISECONDS)
  @Threads(16)
  public void testPut(MyHash hash) {
    hash.myHash.put(
        hash.items[ThreadLocalRandom.current().nextInt(0, hash.elements)],
        ThreadLocalRandom.current().nextInt(0, 20)
    );
  }

  @Benchmark
  @BenchmarkMode({Mode.Throughput, Mode.AverageTime})
  @OutputTimeUnit(TimeUnit.MILLISECONDS)
  @Threads(16)
  public void testGet(MyHash hash, Blackhole bh) {
    Integer x = hash.myHash.get(
        hash.items[ThreadLocalRandom.current().nextInt(0, hash.elements)]
    );
    bh.consume(x);
  }

  @Benchmark
  @BenchmarkMode({Mode.Throughput, Mode.AverageTime})
  @OutputTimeUnit(TimeUnit.MILLISECONDS)
  @Threads(16)
  public void testContains(MyHash hash, Blackhole bh) {
    Boolean x = hash.myHash.contains(
        hash.items[ThreadLocalRandom.current().nextInt(0, hash.elements)]
    );
    bh.consume(x);
  }

  @Benchmark
  @BenchmarkMode({Mode.Throughput, Mode.AverageTime})
  @OutputTimeUnit(TimeUnit.MILLISECONDS)
  @Threads(16)
  public void testRemove(MyHash hash, Blackhole bh) {
    Integer x = hash.myHash.remove(
        hash.items[ThreadLocalRandom.current().nextInt(0, hash.elements)]
    );
    bh.consume(x);
  }

  @Benchmark
  @BenchmarkMode({Mode.Throughput, Mode.AverageTime})
  @OutputTimeUnit(TimeUnit.MILLISECONDS)
  @Threads(16)
  public void testIncrement(MyHash hash) {
    hash.myHash.increment(
        hash.items[ThreadLocalRandom.current().nextInt(0, hash.elements)],
        ThreadLocalRandom.current().nextInt(0, 20)
    );
  }

  @Benchmark
  @BenchmarkMode({Mode.Throughput, Mode.AverageTime})
  @OutputTimeUnit(TimeUnit.MILLISECONDS)
  @Threads(16)
  public void testDecrement(MyHash hash, Blackhole bh) {
    Integer x = hash.myHash.decrement(
        hash.items[ThreadLocalRandom.current().nextInt(0, hash.elements)],
        ThreadLocalRandom.current().nextInt(0, 20)
    );
    bh.consume(x);
  }

}
