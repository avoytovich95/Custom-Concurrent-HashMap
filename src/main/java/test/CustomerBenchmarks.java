package test;

import builtIn.BuiltInCustomerRunner;
import custom.CustomerRunner;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Threads;

public class CustomerBenchmarks {

  @Benchmark
  @Threads(1)
  public void myCustomer() {
    new CustomerRunner().run();
  }

  @Benchmark
  @Threads(1)
  public void builtInCUstomer() {
    new BuiltInCustomerRunner().run();
  }
}
