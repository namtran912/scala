package TestRedis

import java.net.SocketTimeoutException
import java.util.Calendar

import Redis.{BaseRedis, IRedis}
import org.apache.commons.pool2.impl.GenericObjectPoolConfig
import redis.clients.jedis.{Jedis, JedisPool}
import redis.clients.jedis.exceptions.{JedisConnectionException, JedisException}
/**
  * Created by CPU111Process.BlockSize9-local on Process.BlockSize/22/2016.
  */
class Redis {
    val jedis: IRedis = new BaseRedis(20, "127.0.0.1:6379", "127.0.0.1:6380")

    //Lưu theo khoảng cách
    def get(uId: String, itemId: String, kindOfLimit: String): Array[Long] = {
        val key = s"$uId, $itemId".toCharArray.map(_.toByte)
        val field = Array[Byte](Process.LimitType2Byte(kindOfLimit)._1)
        var b = jedis.hGet(key, field)
        if (b == null)
            return null
        val result = new Array[Long]((b.size - 8) / 3 + 1)
        result(0) = Process.bytes2long(b.slice(0, 8))

        for (i <- 1 until result.size)
            result(i) = result(0) + Process.bytes2int(b.slice(8 + (i - 1) * 3, 8 + i * 3))
        result
    }

    def update(uId: String, itemId: String, kindOfLimit: String): Unit = {
        val key = s"$uId, $itemId".toCharArray.map(_.toByte)
        val field = Array[Byte](Process.LimitType2Byte(kindOfLimit)._1)

        var map = jedis.hmGet(key)
        if (map == null)
            return
        val info = map.get(field)
        map.put(field, Process.update(info, Process.LimitType2Byte(kindOfLimit)._2))

        jedis.hmSet(key, map)
    }

    /*//Lưu bình thường không dùng protobuf
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
    }*/
}
