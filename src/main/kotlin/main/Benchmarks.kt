package main

import org.openjdk.jmh.annotations.*
import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.infra.Blackhole

import org.openjdk.jmh.annotations.*

@State(Scope.Benchmark)
@Fork(1)
@Warmup(iterations = 0)
@Measurement(iterations = 1)
open class Benchmarks {
  var value: Double = 0.0

  @Setup
  fun setUp(): Unit {
    value = 3.0
  }

  @Benchmark
  fun sqrtBenchmark(): Double = Math.sqrt(value)

}