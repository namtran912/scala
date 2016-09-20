package TestSBF

/**
  * Created by CPU11179-local on 9/20/2016.
  */
class CountingBloomFilter {

    protected var bitSet: Array[Int] = null
    protected var m: Int = 0
    protected var k: Int = 0

    def this(m: Int, k: Int) = {
        this()
        this.m = m
        this.k = k
        bitSet = new Array[Int](m)
    }

    def this(n: Int, p: Double) = {
        this()
        m = CountingBloomFilterUtils.determineSize(n, p)
        k = CountingBloomFilterUtils.determineHashNumber(m, n)
        bitSet = new Array[Int](m)
    }

    def add(key: Array[Byte]) {
        for (i <- CountingBloomFilterUtils.multiHash(key, k, m))
            bitSet(i) += 1
    }

    def remove(key: Array[Byte]): Int = {
        val num = count(key)
        for (i <- CountingBloomFilterUtils.multiHash(key, k, m))
            bitSet(i) -= num
        num
    }

    def count(key: Array[Byte]): Int = {
        var result: Int = Integer.MAX_VALUE

        for (i <- CountingBloomFilterUtils.multiHash(key, k, m))
            result = Math.min(result, bitSet(i))
        result
    }

    protected def clear() {
        for(i <- 0 until m) bitSet(i) = 0
    }
}
