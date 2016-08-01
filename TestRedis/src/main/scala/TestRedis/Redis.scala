package TestRedis

import redis.clients.jedis.JedisPool
import redis.clients.jedis.exceptions.JedisException
/**
  * Created by CPU111Process.BlockSize9-local on Process.BlockSize/22/2016.
  */
class Redis(val redisHost: String, val redisPort: Int) {
    val jedisPool = new JedisPool(redisHost, redisPort)
    val jedis = jedisPool.getResource

    //Lưu theo khoảng cách
    def get(uId: String, itemId: String, kindOfLimit: String): Array[Long] = {
        val b = jedis.hget(s"$uId, $itemId".toCharArray.map(_.toByte), Array[Byte](Process.LimitType2Byte(kindOfLimit)._1))
        val result = new Array[Long]((b.size - 8) / 3 + 1)

        result(0) = Process.bytes2long(b.slice(0, 8))
        for (i <- 1 until result.size)
            result(i) = result(0) + Process.bytes2int(b.slice(8 + (i - 1) * 3, 8 + i * 3))
        result
    }

    def update(uId: String, itemId: String, kindOfLimit: String): Unit = {
        val map = jedis.hgetAll(s"$uId, $itemId".toCharArray.map(_.toByte))
        val info = map.get(Array[Byte](Process.LimitType2Byte(kindOfLimit)._1))

        map.put(Array[Byte](Process.LimitType2Byte(kindOfLimit)._1), Process.update(info, Process.LimitType2Byte(kindOfLimit)._2))
        jedis.hmset(s"$uId, $itemId".toCharArray.map(_.toByte), map)
    }

    //Lưu bình thường không dùng protobuf
    def get2(uId: String, itemId: String, kindOfLimit: String): Array[Long] = {
        val b = jedis.hget(s"$uId, $itemId".toCharArray.map(_.toByte), Array[Byte](Process.LimitType2Byte(kindOfLimit)._1))
        val result = new Array[Long](b.size / 8)

        for (i <- 0 until result.size)
            result(i) = Process.bytes2long(b.slice(i * 8, (i + 1) * 8))
        result
    }

    def update2(uId: String, itemId: String, kindOfLimit: String): Unit = {
        val map = jedis.hgetAll(s"$uId, $itemId".toCharArray.map(_.toByte))
        val info = map.get(Array[Byte](Process.LimitType2Byte(kindOfLimit)._1))

        map.put(Array[Byte](Process.LimitType2Byte(kindOfLimit)._1), Process.update2(info, Process.LimitType2Byte(kindOfLimit)._2))
        jedis.hmset(s"$uId, $itemId".toCharArray.map(_.toByte), map)
    }

    //Dùng Protobuf
    def get3(uId: String, itemId: String, kindOfLimit: String): Array[Long] = {
      val b = jedis.hget(s"$uId, $itemId".toCharArray.map(_.toByte), Array[Byte](Process.LimitType2Byte(kindOfLimit)._1))
      Process.bytes2longs(b)
    }

    def update3(uId: String, itemId: String, kindOfLimit: String): Unit = {
        val map = jedis.hgetAll(s"$uId, $itemId".toCharArray.map(_.toByte))
        val info = map.get(Array[Byte](Process.LimitType2Byte(kindOfLimit)._1))

        map.put(Array[Byte](Process.LimitType2Byte(kindOfLimit)._1), Process.update3(Process.bytes2longs(info), Process.LimitType2Byte(kindOfLimit)._2))
        jedis.hmset(s"$uId, $itemId".toCharArray.map(_.toByte), map)
    }
}
