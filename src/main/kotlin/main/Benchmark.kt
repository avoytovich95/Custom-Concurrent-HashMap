package main

import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.results.format.ResultFormatType
import org.openjdk.jmh.runner.Runner
import org.openjdk.jmh.runner.options.OptionsBuilder
import org.openjdk.jmh.runner.options.VerboseMode


object Benchmark {

  @JvmStatic
  fun main(args: Array<String>) {
    val opts = OptionsBuilder()
        .include(".*")
        .verbosity(VerboseMode.EXTRA)
        .warmupIterations(20)
        .measurementIterations(20)
        .forks(10)
        .resultFormat(ResultFormatType.CSV)
        .build()

    Runner(opts).run()

//    org.openjdk.jmh.Main.main(args)
  }

  @Benchmark
  fun thing() {

  }

}