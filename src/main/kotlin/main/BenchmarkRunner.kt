package main

import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.BenchmarkMode
import org.openjdk.jmh.annotations.Fork
import org.openjdk.jmh.annotations.Mode

class BenchmarkRunner {

  @Benchmark
  @Fork(value = 1, warmups = 2)
  @BenchmarkMode(Mode.Throughput)
  fun init() {
    // stuff
  }

}