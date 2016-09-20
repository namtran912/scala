package TestSBF

import java.util.LinkedList

/**
  * Created by CPU11179-local on 9/20/2016.
  */
class ScalableCountingBloomFilter {

    private val bloomFilters: LinkedList[ExtendedCountingBloomFilter] = new LinkedList[ExtendedCountingBloomFilter]()
    private var m: Int = 0
    private var k: Int = 0

    def this(m: Int, k: Int) {
        this()
        this.m = m
        this.k = k
        bloomFilters.add(new ExtendedCountingBloomFilter(m, k))
    }

    def this(n: Int, p: Double) {
        this()
        this.m = CountingBloomFilterUtils.determineSize(n, p)
        this.k = CountingBloomFilterUtils.determineHashNumber(m, n)
        bloomFilters.add(new ExtendedCountingBloomFilter(m, k))
    }

    def getSize: Int = bloomFilters.size

    def add(key: Array[Byte]) {
        bloomFilters.getLast.add(key)
        if (bloomFilters.getLast.isFull)
            bloomFilters.add(new ExtendedCountingBloomFilter(m, k))
    }

    def count(key: Array[Byte]): Int = {
        var result: Int = 0
        if (bloomFilters.size == 1)
            result = bloomFilters.getFirst.count(key)
        else {
            val indexes: Array[Int] = CountingBloomFilterUtils.multiHash(key, k, m)
            for(i <- 0 until bloomFilters.size)
                result += bloomFilters.get(i).count(indexes)
        }
        if (result == Integer.MAX_VALUE)
            return 0
        result
    }

    def remove(key: Array[Byte]): Unit = {
        if (bloomFilters.size == 1)
            bloomFilters.getFirst.remove(key)
        else {
            val indexes: Array[Int] = CountingBloomFilterUtils.multiHash(key, k, m)
            for(i <- 0 until bloomFilters.size)
                bloomFilters.get(i).remove(indexes)
        }
    }

    def clear() {
        while (bloomFilters.size > 1) bloomFilters.removeLast
        bloomFilters.getFirst.clear()
    }
}
