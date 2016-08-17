package TestRedis

import java.io.{BufferedWriter, FileWriter}
import java.util.{Calendar, Date}

import com.google.protobuf.ByteString
import redis.clients.jedis.JedisPool

import scala.collection.mutable.ArrayBuffer

/**
  * Created by CPU11179-local on 7/22/2016.
  */

object Main {
    def main(agrs: Array[String]): Unit = {
        val redis = new Redis("localhost", 6379, 6380)
        //val begin = Calendar.getInstance.getTimeInMillis
        //for (i <- 1 to 1000)
           // redis.update("u1", "i1", "click/day")
        for (i <- 1 to 1000) {
            /* new Thread(new Runnable {
                  override def run(): Unit = {*/
            // val begin = Calendar.getInstance.getTimeInMillis
            redis.get("u1", "i1", "click/day")//.foreach((time: Long) => println(time))
            //  val end = Calendar.getInstance.getTimeInMillis
            // println(end - begin)
            /*}
              }).start*/
        }
         //val end = Calendar.getInstance.getTimeInMillis
        //println(end - begin)
    }
}
