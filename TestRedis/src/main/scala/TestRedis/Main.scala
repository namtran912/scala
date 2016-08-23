package TestRedis

import java.io.{BufferedWriter, FileWriter}
import java.util.{Calendar, Date}

import com.google.protobuf.ByteString
import org.apache.log4j.BasicConfigurator
import redis.clients.jedis.JedisPool

import scala.collection.mutable.ArrayBuffer

/**
  * Created by CPU11179-local on 7/22/2016.
  */

object Main {
    def main(agrs: Array[String]): Unit = {
        BasicConfigurator.configure()
        val redis = new Redis
        val begin = Calendar.getInstance.getTimeInMillis
        for (i <- 1 to 1000)
            //redis.update("u1", "i1", "click/day")
            redis.get("u1", "i1", "click/day")//.foreach((time: Long) => println(time))

        val end = Calendar.getInstance.getTimeInMillis
        println(end - begin)
    }
}
