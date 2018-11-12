package builtIn

import custom.Customer
import enums.Items
import java.util.concurrent.ConcurrentHashMap

class BuiltInCustomerRunner {

  val hash = ConcurrentHashMap<Items, Int>()

  var name = 0
  val customers = Array(16) { BuiltInCustomer(hash, name++.toString()) }

  fun run() {
    for (customer in customers) {
      Thread(customer).start()
    }
  }

}

fun main(args: Array<String>) {
  BuiltInCustomerRunner().run()
}