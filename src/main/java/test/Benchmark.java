package test;

import custom.hash.CustomHash;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Mode;

public class Benchmark {

  @org.openjdk.jmh.annotations.Benchmark
  @Fork(value = 1, warmups = 1)
  @BenchmarkMode(Mode.Throughput)
  public void init() {
    // Do nothing
  }

}
