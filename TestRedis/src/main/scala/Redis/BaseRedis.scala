package Redis

import java.util
import java.util.Map

import org.apache.commons.pool2.impl.GenericObjectPoolConfig
import org.apache.log4j._
import redis.clients.jedis.{Jedis, JedisPool}

import scala.collection.mutable.ArrayBuffer
import scala.util.Random

/**
  * Created by CPU11179-local on 8/18/2016.
  */
class BaseRedis(timeout: Int, ip: String*) extends IRedis{
    final val LOGGER = Logger.getLogger(classOf[BaseRedis])
    val jedis = new ArrayBuffer[Jedis]()

    ip.foreach((ip: String) => {
        val host = ip.substring(0, ip.indexOf(":"))
        val port = ip.substring(ip.indexOf(":") + 1, ip.length).toInt
        try {
            jedis += new JedisPool(new GenericObjectPoolConfig, host, port, timeout).getResource
        }
        catch {
            case _ => LOGGER.error("Connect to " + ip + " failed")
        }
    })

    var constRand = ArrayBuffer[Int]()
    for (i <- 0 until jedis.size) constRand += i

    override def hGet(key: Array[Byte], field: Array[Byte]): Array[Byte] = {
        var srand = constRand.clone
        while(srand.size > 0) {
            val idx = srand(Random.nextInt(srand.size))
            val rJedis = jedis(idx)
            try {
                 return rJedis.hget(key, field)
            }
            catch {
                case _ => LOGGER.info("Timeout get: " + idx)
            }
            srand -= idx
        }
        LOGGER.info("Timeout get: all")
        null
    }

    override def hSet(key: Array[Byte], field: Array[Byte], value: Array[Byte]): Unit = {
        jedis.foreach((jedis: Jedis) => jedis.hset(key, field, value))
    }

    override def hmGet(key: Array[Byte]): util.Map[Array[Byte], Array[Byte]] = {
        var srand = constRand.clone

        while(srand.size > 0) {
            val idx = srand(Random.nextInt(srand.size))
            val rJedis = jedis(idx)
            try {
                return rJedis.hgetAll(key)
            }
            catch {
                case _ => LOGGER.info("Timeout get: " + idx)
            }
            srand -= idx
        }
        LOGGER.info("Timeout get: all")
        null
    }

    override def hmSet(key: Array[Byte], value: Map[Array[Byte], Array[Byte]]): Unit = {
        jedis.foreach((jedis: Jedis) => jedis.hmset(key, value))
    }

    override def lPop(key: Array[Byte]): Array[Byte] = {
        var result: Array[Byte] = null
        jedis.foreach((jedis: Jedis) => result = jedis.lpop(key))
        result
    }

    override def rPop(key: Array[Byte]): Array[Byte] = {
        var result: Array[Byte] = null
        jedis.foreach((jedis: Jedis) => result = jedis.rpop(key))
        result
    }

    override def lSet(key: Array[Byte], index: Long, value: Array[Byte]): Unit = {
        jedis.foreach((jedis: Jedis) => jedis.lset(key, index, value))
    }

    override def lPush(key: Array[Byte], value: Array[Byte]*): Unit = {
        jedis.foreach((jedis: Jedis) => jedis.lpush(key, value:_*))
    }

    override def rPush(key: Array[Byte], value: Array[Byte]*): Unit = {
        jedis.foreach((jedis: Jedis) => jedis.rpush(key, value:_*))
    }

    override def lRange(key: Array[Byte], start: Long, end: Long): util.List[Array[Byte]] = {
        var srand = constRand.clone

        while(srand.size > 0) {
            val idx = srand(Random.nextInt(srand.size))
            val rJedis = jedis(idx)
            try {
                return rJedis.lrange(key, start, end)
            }
            catch {
                case _ => LOGGER.info("Timeout get: " + idx)
            }
            srand -= idx
        }
        LOGGER.info("Timeout get: all")
        null
    }

    override def lLen(key: Array[Byte]): Long = {
        var srand = constRand.clone

        while(srand.size > 0) {
            val idx = srand(Random.nextInt(srand.size))
            val rJedis = jedis(idx)
            try {
                return rJedis.llen(key)
            }
            catch {
                case _ => LOGGER.info("Timeout get: " + idx)
            }
            srand -= idx
        }
        LOGGER.info("Timeout get: all")
        -1
    }

    override def sAdd(key: Array[Byte], value: Array[Byte]*): Unit = {
        jedis.foreach((jedis: Jedis) => jedis.sadd(key,  value:_*))
    }

    override def sPop(key: Array[Byte]): Array[Byte] = {
        var srand = constRand.clone

        while(srand.size > 0) {
            val idx = srand(Random.nextInt(srand.size))
            val rJedis = jedis(idx)
            try {
                return rJedis.spop(key)
            }
            catch {
                case _ => LOGGER.info("Timeout get: " + idx)
            }
            srand -= idx
        }
        LOGGER.info("Timeout get: all")
        null
    }

    override def Get(key: Array[Byte]): Array[Byte] = {
        var srand = constRand.clone

        while(srand.size > 0) {
            val idx = srand(Random.nextInt(srand.size))
            val rJedis = jedis(idx)
            try {
                return rJedis.get(key)
            }
            catch {
                case _ => LOGGER.info("Timeout get: " + idx)
            }
            srand -= idx
        }
        LOGGER.info("Timeout get: all")
        null
    }

    override def Set(key: Array[Byte], value: Array[Byte]): Unit = {
        jedis.foreach((jedis: Jedis) => jedis.set(key, value))
    }
}
