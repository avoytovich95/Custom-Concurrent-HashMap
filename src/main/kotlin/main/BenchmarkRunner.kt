package main

import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.results.format.ResultFormatType
import org.openjdk.jmh.runner.Runner
import org.openjdk.jmh.runner.options.OptionsBuilder
import org.openjdk.jmh.runner.options.VerboseMode

object BenchmarkRunner {

  @JvmStatic
  fun main(args: Array<String>) {
    val opts = OptionsBuilder()
      .include(".*")
      .warmupIterations(1)
      .measurementIterations(5)
      .forks(1)
      .verbosity(VerboseMode.EXTRA)
      .resultFormat(ResultFormatType.CSV)
      .build()


    Runner(opts).run()
  }

}