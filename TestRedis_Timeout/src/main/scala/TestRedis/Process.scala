package TestRedis

import java.io.ByteArrayOutputStream
import java.util.Calendar

import com.google.protobuf.Message

import scala.util.control.Breaks._

/**
  * Created by CPU11179-local on 7/22/2016.
  */
object Process {
    val LimitType2Byte: Map[String, (Byte, Long)] = Map("imp/hour" -> (0.toByte, 3600000L), "imp/day" -> (1.toByte, 86400000L), "click/hour" -> (2.toByte, 3600000L), "click/day" -> (3.toByte, 86400000L))

    //Lưu theo khoảng cách
    def update(info: Array[Byte], limit: Long): Array[Byte] = {
        def find(left: Int, right: Int, root: Long, now: Long, limit: Long): Int = {
            var l = left
            var r = right
            var result = -1
            while (l <= r) {
                val mid = (l + r) / 2
                var time = root + bytes2int(info.slice(8 + (mid - 1)*3, 8 + mid*3))

                if (mid == 0)
                    time = root
                if (now - time <= limit)
                    r = mid - 1
                else {
                    l = mid + 1
                    result = mid
              }
            }
            result
        }
        val now = Calendar.getInstance.getTime.getTime

        if (info == null)
            return long2bytes(now)

        val root = bytes2long(info.slice(0, 8))
        val idx = find(0, (info.size - 8) / 3, root, now, limit)
        if (idx == -1) {
            val result = new Array[Byte](info.size + 3)
            info.slice(0, info.size).copyToArray(result, 0)
            int2bytes((now - root).toInt).copyToArray(result, result.size - 3)
            result
        }
        else {
            val result = new Array[Byte](((info.size - 8) / 3 - idx)*3 + 8)
            val newRoot = root + bytes2int(info.slice(8 + idx * 3, 8 + (idx + 1) * 3))
            long2bytes(newRoot).copyToArray(result, 0)

            for (i <- 8 + (idx + 1)*3 to info.size - 3)
                int2bytes((bytes2int(info.slice(i, i + 3)) + root - newRoot).toInt).copyToArray(result, 8)
            int2bytes((now - newRoot).toInt).copyToArray(result, result.size - 3)
            result
        }
    }

    //Lưu bình thường không dùng protobuf
    def update2(info: Array[Byte], limit: Long): Array[Byte] = {
        def find(left: Int, right: Int, now: Long, limit: Long): Int = {
            var l = left
            var r = right
            var result = -1
            while (l <= r) {
                val mid = (l + r) / 2
                if (now - bytes2long(info.slice(mid * 8, (mid + 1) * 8)) <= limit)
                    r = mid - 1
                else {
                    l = mid + 1
                    result = mid
                }
            }
            result
        }
        val now = Calendar.getInstance.getTime.getTime

        if (info == null)
            return long2bytes(now)

        val idx = find(0, info.size / 8 - 1, now, limit)
        val result = new Array[Byte](info.size - idx * 8)
        info.slice(idx*8 + 1, info.size).copyToArray(result, 0)
        long2bytes(now).copyToArray(result, result.size - 8)

        result
    }

    //Dùng ProtoBuf
    def update3(info: Array[Long], limit: Long): Array[Byte] = {
        def find(left: Int, right: Int, now: Long, limit: Long): Int = {
            var l = left
            var r = right
            var result = -1
            while (l <= r) {
                val mid = (l + r) / 2
                if (now - info(mid) <= limit)
                    r = mid - 1
                else {
                    l = mid + 1
                    result = mid
                }
            }
            result
        }
        val now = Calendar.getInstance.getTime.getTime

        if (info == null)
            return longs2bytes(Array[Long](now))

        val idx = find(0, info.size - 1, now, limit)
        val result = new Array[Long](info.size - idx)
        info.slice(idx + 1, info.size).copyToArray(result, 0)
        result(result.size - 1) = now

        longs2bytes(result)
    }

    def int2bytes(l: Int): Array[Byte] = {
        val result = new Array[Byte](3)
        var ll = l
        for (i <- 2 to 0 by -1) {
            result(i) = ((ll & 0xFF) - 0x80).toByte
            ll >>= 3
        }
        result
    }

    def bytes2int(b: Array[Byte]): Int  = {
        var result: Int = 0
        for (i <- 0 to 2) {
            result <<= 3
            result |= ((0x80 + b(i))& 0xFF)
        }
        result
    }

    def long2bytes(l: Long): Array[Byte] = {
        val result = new Array[Byte](8)
        var ll = l
        for (i <- 7 to 0 by -1) {
            result(i) = ((ll & 0xFF) - 0x80).toByte
            ll >>= 8
        }
        result
    }

    def bytes2long(b: Array[Byte]): Long  = {
        var result: Long = 0
        for (i <- 0 to 7) {
            result <<= 8
            result |= ((0x80 + b(i))& 0xFF)
        }
        result
    }

    def longs2bytes(times: Array[Long]): Array[Byte] = {
        val timeBuilder = TimeProto.Time.newBuilder
        times.foreach((t: Long) => timeBuilder.addTime(t))
        timeBuilder.build.toByteArray
    }

    def bytes2longs(b: Array[Byte]): Array[Long]  = {
        if (b == null)
            return null
        val list = TimeProto.Time.parseFrom(b).getTimeList
        val result = new Array[Long](list.size)
        for (i <- 0 until list.size)
            result(i) = list.get(i)
        result
    }
}
