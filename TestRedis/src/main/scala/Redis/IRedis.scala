package Redis

import java.util.Map
import java.util.List

/**
  * Created by CPU11179-local on 8/18/2016.
  */
trait IRedis {
    def hGet(key: Array[Byte], field: Array[Byte]): Array[Byte]
    def hSet(key: Array[Byte], field: Array[Byte], value: Array[Byte]): Unit
    def hmSet(key: Array[Byte], value: Map[Array[Byte], Array[Byte]]): Unit
    def hmGet(key: Array[Byte]): Map[Array[Byte], Array[Byte]]

    def lPop(key: Array[Byte]): Array[Byte]
    def rPop(key: Array[Byte]): Array[Byte]
    def lSet(key: Array[Byte], index: Long, value: Array[Byte]): Unit
    def lPush(key: Array[Byte], value: Array[Byte]*): Unit
    def rPush(key: Array[Byte], value: Array[Byte]*): Unit
    def lRange(key: Array[Byte], start: Long, end: Long): List[Array[Byte]]
    def lLen(key: Array[Byte]): Long

    def sPop(key: Array[Byte]): Array[Byte]
    def sAdd(key: Array[Byte], value: Array[Byte]*): Unit

    def Get(key: Array[Byte]): Array[Byte]
    def Set(key: Array[Byte], value: Array[Byte]): Unit
}