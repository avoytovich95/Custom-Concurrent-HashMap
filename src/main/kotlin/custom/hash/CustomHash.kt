package custom.hash

import enums.Items
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.locks.ReentrantLock

class CustomHash<K>{

  @Volatile var table = arrayOfNulls<Entry<K>>(16)
  private val locks = Array(16) { ReentrantLock() }
  @Volatile var size = 0

  private fun acquire(k: K) {
    val i = (k.hashCode() shr 4) % 16
    locks[i].lock()
  }

  private fun release(k: K) {
    val i = (k.hashCode() shr 4) % 16
    locks[i].unlock()
  }

  private fun lockMap() {
    for (lock in locks)
      lock.lock()
  }

  private fun releaseMap() {
    for (lock in locks)
      lock.unlock()
  }

  private fun getHashIndex(k: K) = (k.hashCode() shr 4) % table.size

  private fun policy(): Boolean {
    return size / table.size > 2
  }

  fun contains(k: K): Boolean {
    acquire(k)
    try {
      val i = getHashIndex(k)

      if (table[i] == null)
        return false
      else {
        var e = table[i]

        while (true) {
          when {
            e!!.hash == k.hashCode() -> return true
            e.next == null -> return false
            else -> e = e.next
          }
        }
      }
    }finally {
      release(k)
    }
  }

  fun put (k: K, v: Int) {
    acquire(k)
    try {
      val i = k!!.hashCode() % table.size

      if (table[i] == null) {
        size++
        table[i] = Entry(k, v, k.hashCode())
      } else {
        var e = table[i]

        while (true) {
          if (e!!.hash == k.hashCode()) {
            e.v = v
            break
          } else if (e.next == null) {
            size++
            e.next = Entry(k, v, k.hashCode())
            break
          } else
            e = e.next
        }
      }
    } finally {
      release(k)
    }
  }

  fun get (k: K): Int? {
    acquire(k)
    try {
      val i = k!!.hashCode() % table.size

      if (table[i] == null)
        return null
      else {
        var e = table[i]

        while (true) {
          when {
            e!!.hash == k.hashCode() -> return e.v
            e.next == null -> return null
            else -> e = e.next
          }
        }
      }
    }finally {
      release(k)
    }
  }

  fun remove(k: K): Int? {
    acquire(k)
    try {
      val i = k!!.hashCode() % table.size

      if (table[i] == null)
        return null
      else {
        var e = table[i]
        var prev: Entry<K>? = null

        while (true) {
          when {
            e!!.hash == k.hashCode() -> {
              size--
              val v = e.v
              if (prev == null)
                table[i] = e.next
              else
                prev.next = e.next
              return v
            }
            e.next == null -> return null
            else -> {
              size--
              prev = e
              e = e.next
            }
          }
        }
      }
    }finally {
      release(k)
    }
  }

  fun increment(k: K, v: Int) {
    acquire(k)
    try {
      val i = k!!.hashCode() % table.size

      if (table[i] == null) {
        table[i] = Entry(k, v, k.hashCode())
        size++
      } else {
        var e = table[i]

        while (true) {

          if (e!!.hash == k.hashCode()) {
            e.increase(v)
            break
          } else if (e.next != null)
            e = e.next
          else {
            e.next = Entry(k, v, k.hashCode())
            size++
            break
          }
        }
      }
    } finally {
      release(k)
    }
    if (policy())
      resize()
  }

  fun decrement(k: K, v: Int): Int? {
    acquire(k)
    try {
      val i = k!!.hashCode() % table.size
      if (table[i] == null)
        return null

      var e = table[i]
      var prev: Entry<K>? = null

      while (true) {

        when {
          e!!.hash == k.hashCode() -> return if (v >= e.v) {
            val r = v - e.v
            if (prev == null)
              table[i] = e.next
            else
              prev.next = e.next
            r
          } else {
            e.v -= v
            0
          }
          e.next == null -> return null
          e.next != null -> {
            prev = e
            e = e.next
          }
        }
      }

    } finally {
      release(k)
    }
  }

  private fun resize() {
    val oldSize = table.size
    lockMap()
    try {
      if (oldSize != table.size)
        return
      val newSize = oldSize * 2
      val oldTable = table
      table = arrayOfNulls(newSize)
      size = 0

      for (i in oldTable) {
        if (i != null) {
          var e = i
          do {
            insert(e!!.k, e.v)
            e = e.next
          } while (e != null)

        }
      }

    } finally {
      releaseMap()
    }
  }

  private fun insert(k: K, v: Int) {
    val e = Entry(k, v, k!!.hashCode())
    val i = e.hash % table.size
    if (table[i] == null)
      table[i] = e
    else
      table[i]!!.add(e)
    size++
  }

  override fun toString(): String {
    val str = StringBuilder("{")
    var count = 0
    for (bucket in table) {
      if (bucket != null) {
        var e = bucket
        do {
          str.append("${e!!.k}=${e.v}")
          count++
          if (count < size)
            str.append(", ")
          e = e.next
        } while (e != null)
      }
    }
    return str.append("}").toString()
  }
}

fun main(args: Array<String>) {
  val myMap = CustomHash<Items>()
  val map = ConcurrentHashMap<Items, Int>()

  for (i in Items.values()) {
//    myMap.increment(i, 4)
//    map.merge(i, 4, Integer::sum)
    myMap.put(i, 4)
    map[i] = 4
  }

  println("My Map:\n $myMap")
  println("Built in:\n $map")

  println("My Map size: ${myMap.size}")
  println("Built in size: ${map.size}")

  for (i in Items.values()) {
    myMap.remove(i)
    map.remove(i)
  }

  println("My empty Map:\n $myMap")
  println("Built in empty:\n $map")

  println("My Map size: ${myMap.size}")
  println("Built in size: ${map.size}")

}
