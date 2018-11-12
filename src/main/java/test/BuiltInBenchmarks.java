package test;

import enums.Items;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

public class BuiltInBenchmarks {

  @State(Scope.Benchmark)
  public static class Hash {
    ConcurrentHashMap<Items, Integer> hash = new ConcurrentHashMap<Items, Integer>();
    Items[] items = Items.values();
    int elements = items.length;
  }

  @Benchmark
  public void testPut(Hash hash) {
    hash.hash.put(
        hash.items[ThreadLocalRandom.current().nextInt(0, hash.elements)],
        ThreadLocalRandom.current().nextInt(0, 20)
    );
  }

  @Benchmark
  public void testGet(Hash hash, Blackhole bh) {
    Object x = hash.hash.get(
        hash.items[ThreadLocalRandom.current().nextInt(0, hash.elements)]
    );
    bh.consume(x);
  }

  @Benchmark
  public void testContains(Hash hash, Blackhole bh) {
    Boolean x = hash.hash.contains(
        hash.items[ThreadLocalRandom.current().nextInt(0, hash.elements)]
    );
    bh.consume(x);
  }

  @Benchmark
  public void testRemove(Hash hash, Blackhole bh) {
    Object x = hash.hash.remove(
        hash.items[ThreadLocalRandom.current().nextInt(0, hash.elements)]
    );
    bh.consume(x);
  }

  @Benchmark
  public void testIncrement(Hash hash) {
    hash.hash.merge(
        hash.items[ThreadLocalRandom.current().nextInt(0, hash.elements)],
        ThreadLocalRandom.current().nextInt(0, 20),
        Integer::sum
    );
  }

  @Benchmark
  public void testDecrement(Hash hash) {
    hash.hash.computeIfPresent(
        hash.items[ThreadLocalRandom.current().nextInt(0, hash.elements)],
        (k, v) -> {
          int newV = ThreadLocalRandom.current().nextInt(0, 20);
          if (newV >= v)
            return null;
          else
            return v - newV;
        });
  }

}
