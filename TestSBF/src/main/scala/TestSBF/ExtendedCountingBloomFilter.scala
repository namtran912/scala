package TestSBF

/**
  * Created by CPU11179-local on 9/20/2016.
  */
class ExtendedCountingBloomFilter extends CountingBloomFilter {

    private var size: Int = 0
    private var n: Int = 0

    def this(n: Int, p: Double) = {
        this()
        m = CountingBloomFilterUtils.determineSize(n, p)
        k = CountingBloomFilterUtils.determineHashNumber(m, n)
        bitSet = new Array[Int](m)
        size = 0
        this.n = n
    }

    def this(m: Int, k: Int) = {
        this()
        this.m = m
        this.k = k
        bitSet = new Array[Int](m)
        size = 0
        this.n = CountingBloomFilterUtils.determineMaxSize(m, k)
    }

    def getSize: Int = size

    def isFull: Boolean = size == n

    def count(indexes: Array[Int]): Int = {
        var result: Int = Integer.MAX_VALUE

        for (i <- indexes)
            result = Math.min(result, bitSet(i))
        result
    }

    override def add(key: Array[Byte]) {
        size += 1
        super.add(key)
    }

    override def remove(key: Array[Byte]): Int = {
        val num = super.remove(key)
        size -= num
        num
    }

    def remove(indexes: Array[Int]) {
        val num = count(indexes)
        for (i <- indexes)
            bitSet(i) -= num
        size -= num
    }

    override def clear() {
        this.size = 0
        super.clear()
    }
}

