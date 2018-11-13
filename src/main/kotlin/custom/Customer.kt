package custom

import custom.hash.CustomHash
import enums.Items
import enums.PlayerState
import org.openjdk.jmh.annotations.Benchmark

import java.util.concurrent.ThreadLocalRandom
import java.util.concurrent.TimeUnit

import org.openjdk.jmh.annotations.BenchmarkMode
import org.openjdk.jmh.annotations.Mode
import org.openjdk.jmh.annotations.OutputTimeUnit

class Customer(private val exchange: CustomHash<Items>, val name: String): Runnable {

  private val items = HashMap<Items, Int>()
  private val runs = 1000
  private var state = PlayerState.SELLING

  private val itemTotal = Items.values().size

  private var buying: Items
  var quantity = 0
  var tries = 0

  init {
    for (i in Items.values()) {
      val num = ThreadLocalRandom.current().nextInt(0, 11)
      if (num != 0)
        items[i] = num
    }

    buying = Items.values()[ThreadLocalRandom.current().nextInt(0, itemTotal)]
    quantity = ThreadLocalRandom.current().nextInt(1, 11)
  }

  override fun run() {
    for (i in 0..runs) {

      when (state) {
        PlayerState.BUYING -> buy()
        PlayerState.SELLING -> sell()
      }

    }
  }

  private fun buy() {
    if (tries < 4) {

      val r = exchange.decrement(buying, quantity)

      if (r != null) {
        if (r == 0) {
          items.merge(buying, quantity, Integer::sum)
          resetBuying()
        } else {
          val q = quantity - r
          quantity = r
          items.merge(buying, q, Integer::sum)
          tries++
        }
      } else
        tries++
    } else
      resetBuying()

    state = PlayerState.SELLING
  }

  private fun resetBuying() {
    tries = 0
    buying = Items.values()[ThreadLocalRandom.current().nextInt(0, itemTotal)]
    quantity = ThreadLocalRandom.current().nextInt(1, 11)
  }

  private fun sell() {
    val item = getRandomItem()
    val quantity = getRandomQuantity(item)

    exchange.increment(item, quantity)

    items.compute(item) { _, v ->
      if (v == quantity)
        null
      else
        v!! - quantity
    }
    state = PlayerState.BUYING
  }

  private fun getRandomItem(): Items {
    var item = Items.values()[ThreadLocalRandom.current().nextInt(0, itemTotal)]
    while (!items.containsKey(item))
      item = Items.values()[ThreadLocalRandom.current().nextInt(0, itemTotal)]

    return item
  }

  private fun getRandomQuantity(item: Items): Int {
    var quantity = items[item]!!
    if (quantity != 1)
      quantity = ThreadLocalRandom.current().nextInt(1, quantity)

    return quantity
  }

}

fun main(args: Array<String>) {
  val exchange = CustomHash<Items>()
  Thread(Customer(exchange, "Jon")).start()
  Thread(Customer(exchange, "Alex")).start()
  Thread(Customer(exchange, "Anthony")).start()
  Thread(Customer(exchange, "Mike")).start()
}
