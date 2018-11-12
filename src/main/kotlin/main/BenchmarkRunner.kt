package main

import org.openjdk.jmh.annotations.Mode
import org.openjdk.jmh.results.format.ResultFormatType
import org.openjdk.jmh.runner.Runner
import org.openjdk.jmh.runner.options.OptionsBuilder
import java.util.concurrent.TimeUnit

object BenchmarkRunner {

  @JvmStatic
  fun main(args: Array<String>) {
    val opts = OptionsBuilder()
      .include(".*")
      .warmupIterations(5)
      .measurementIterations(15)
      .forks(1)
      .resultFormat(ResultFormatType.CSV)
      .mode(Mode.Throughput)
      .mode(Mode.AverageTime)
      .threads(32)
      .timeUnit(TimeUnit.MILLISECONDS)
      .build()


    Runner(opts).run()
  }

}