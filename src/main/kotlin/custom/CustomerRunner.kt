package custom

import custom.hash.CustomHash
import enums.Items

class CustomerRunner {
  val hash = CustomHash<Items>()

  var name = 0
  val customers = Array(16) { Customer(hash, name++.toString()) }

  fun run() {
    for (customer in customers) {
      Thread(customer).start()
    }
  }
}

fun main(args: Array<String>) {
  CustomerRunner().run()
}