package TestSBF

import java.io.{BufferedReader, BufferedWriter, FileReader, FileWriter}
import java.util.Calendar

import org.junit.Test

import scala.collection.mutable.ArrayBuffer

/**
  * Created by CPU11179-local on 9/27/2016.
  */
class TestLog2 {
    @Test
    def test(): Unit = {
        val begin = Calendar.getInstance.getTimeInMillis
        val sbf = new ScalableCountingBloomFilter(1000000, 0.05)
        val reader = new BufferedReader(new FileReader("C:\\Users\\CPU11179-local\\Desktop\\server_58728080_2016_08_27_09.log"))
        val writer = new BufferedWriter(new FileWriter("C:\\Users\\CPU11179-local\\Desktop\\result.log"))

        var line: String = null
        var i = 0
        var result: Map[(String, Int), Int] = Map()

        try {
            while ((line = reader.readLine) != null) {
                val info = line split(",")

                info(12) match {
                    case "impression" => {
                        val key = (info(9) + info(11)).getBytes
                        sbf.add(key)
                        if (sbf.count(key) > 20)
                          sbf.remove(key)
                    }
                    case "click" => {
                        val key = (info(9) + info(11)).getBytes
                        val count = sbf.count(key)
                        if (count > 0) {
                            if (result.contains(info(9), count))
                                result += (info(9), count) -> (result((info(9), count)) + 1)
                            else
                                result += (info(9), count) -> 1
                            sbf.remove(key)
                        }
                    }
                    case _ => {}
                }
                i += 1
                if (i % 1000000 == 0)
                    println(i + " " + sbf.getSize)

            }
        }
        catch {
            case ex: Exception => {
                println(line)
            }
        }
        reader.close()

        for(key <- result.keySet)
            writer.write(s"${key._1}\t${key._2} \t${result(key)}" + "\n")

        writer.close()

        val end = Calendar.getInstance.getTimeInMillis
        println(end - begin)
    }
}
