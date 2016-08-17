package TestRedis

import java.net.SocketTimeoutException
import java.util.Calendar

import org.apache.commons.pool2.impl.GenericObjectPoolConfig
import redis.clients.jedis.{Jedis, JedisPool}
import redis.clients.jedis.exceptions.{JedisConnectionException, JedisException}
/**
  * Created by CPU111Process.BlockSize9-local on Process.BlockSize/22/2016.
  */
class Redis(val redisHost: String, val redisPort_1: Int, val redisPort_2: Int) {

    var jedisPool_1: JedisPool = null
    var jedisPool_2: JedisPool = null
    var jedis_1: Jedis = null
    var jedis_2: Jedis = null
    try {
        jedisPool_1 = new JedisPool(new GenericObjectPoolConfig, redisHost, redisPort_1, 10)
        jedis_1 = jedisPool_1.getResource

        jedisPool_2 = new JedisPool(new GenericObjectPoolConfig, redisHost, redisPort_2, 10)
        jedis_2 = jedisPool_2.getResource
    }
    catch {
        case _ => println("Error: connect")
    }


    //Lưu theo khoảng cách
    def get(uId: String, itemId: String, kindOfLimit: String): Array[Long] = {
        var b: Array[Byte] = null
        var check = true
        try {
            b = jedis_1.hget(s"$uId, $itemId".toCharArray.map(_.toByte), Array[Byte](Process.LimitType2Byte(kindOfLimit)._1))
        }
        catch {
            case ex: SocketTimeoutException => {
                println("Timeout 1")
                check = false
            }

            case ex: JedisConnectionException => {
                println("Timeout 1")
                check = false
            }

            case _ => {
                println("Error: get 1")
                check = false
            }
        }

        if (check == false)
            try {
                check = true
                b = jedis_2.hget(s"$uId, $itemId".toCharArray.map(_.toByte), Array[Byte](Process.LimitType2Byte(kindOfLimit)._1))
            }
            catch {
                case ex: SocketTimeoutException => {
                    println("Timeout 2")
                    check = false
                }

                case ex: JedisConnectionException => {
                    println("Timeout 2")
                    check = false
                }

                case _ => {
                    println("Error: get 2")
                    check = false
                }
            }

        if (check == false)
            return null

        val result = new Array[Long]((b.size - 8) / 3 + 1)
        result(0) = Process.bytes2long(b.slice(0, 8))

        for (i <- 1 until result.size)
            result(i) = result(0) + Process.bytes2int(b.slice(8 + (i - 1) * 3, 8 + i * 3))
        result
    }

    def update(uId: String, itemId: String, kindOfLimit: String): Unit = {
        var map = jedis_1.hgetAll(s"$uId, $itemId".toCharArray.map(_.toByte))
        val info = map.get(Array[Byte](Process.LimitType2Byte(kindOfLimit)._1))
        map.put(Array[Byte](Process.LimitType2Byte(kindOfLimit)._1), Process.update(info, Process.LimitType2Byte(kindOfLimit)._2))

        try {
            jedis_1.hmset(s"$uId, $itemId".toCharArray.map(_.toByte), map)
            jedis_2.hmset(s"$uId, $itemId".toCharArray.map(_.toByte), map)
        }
        catch {
            case  _ => {
                println("Error: set")
                return
            }
        }
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
