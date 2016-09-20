package TestSBF

/**
  * Created by CPU11179-local on 9/20/2016.
  */
object CountingBloomFilterUtils {

    def multiHash(key: Array[Byte], k: Int, m: Int): Array[Int] = {
        val result = new Array[Int](k)
        val h = redis.clients.util.MurmurHash.hash64A(key, 42)
        val a = ((h & 0xFFFFFFFF00000000L) >> 32).toInt
        val b = (h & 0xFFFFFFFF).toInt

        for(i <- 0 until k)
            result(i) = Math.abs((a + b * (i + 1)) % m)
        result
    }

    def determineMaxSize(m: Int, k: Double): Int = Math.floor(m * 0.6931471805599453D / k).toInt

    def determineProbabilistic(n: Int, m: Int): Double = Math.pow(Math.E, (m * -0.4804530139182014D) / n)

    def determineSize(n: Int, p: Double): Int = Math.ceil((n * Math.log(p)) / -0.4804530139182014D).toInt

    def determineHashNumber(m: Int, n: Int): Int = Math.ceil((m / n) * 0.6931471805599453D).toInt
}
