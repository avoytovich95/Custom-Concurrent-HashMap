package main

import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.results.format.ResultFormatType
import org.openjdk.jmh.runner.Runner
import org.openjdk.jmh.runner.options.OptionsBuilder
import org.openjdk.jmh.runner.options.VerboseMode

object BenchmarkRun {

  @JvmStatic
  fun main(args: Array<String>) {
    val opts = OptionsBuilder()
        .include(".*")
        .verbosity(VerboseMode.EXTRA)
        .resultFormat(ResultFormatType.CSV)
        .build()


    Runner(opts).run()

//    org.openjdk.jmh.Main.main(args)
  }

}