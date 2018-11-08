package custom.hash

class Entry<K>(val k: K, var v: Int, val hash: Int) {

  var next: Entry<K>? = null


  fun increase(v: Int) {
    this.v += v
  }

  fun decrease(v: Int) {
    this.v -= v
  }

  fun add (n: Entry<K>) {
    if (next == null)
      next = n
    else
      next!!.add(n)
  }

  fun exists(k: K): Boolean {
    val h = k!!.hashCode()
    return when {
      h == hash -> true
      next != null -> next!!.exists(k)
      else -> false
    }
  }

  override fun toString(): String {
    var s = "{$k, $v, ->"
    if (next != null)
      s += "${next!!.k}}"
    else
      s += "*}"
    return s
  }
}