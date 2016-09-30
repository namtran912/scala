package TestSBF

import java.io._
import java.util.Calendar

import org.junit.Test

import scala.collection.mutable.ArrayBuffer

/**
  * Created by CPU11179-local on 9/27/2016.
  */
class TestLog2 {
    @Test
    def test(): Unit = {
        val sbf = new ScalableCountingBloomFilter(10000000, 0.001)
        val reader = new BufferedReader(new FileReader("C:\\Users\\CPU11179-local\\Desktop\\server_58728080_2016_08_27_09.log"))
        val writer = new BufferedWriter(new FileWriter("C:\\Users\\CPU11179-local\\Desktop\\result.log"))

        var line: String = ""
        var i: Long = 0
        var result: Map[(String, Int), Int] = Map()

        /*val lnr: LineNumberReader = new LineNumberReader(new FileReader(new File("C:\\Users\\CPU11179-local\\Desktop\\server_58728080_2016_08_27_09.log")))
        lnr.skip(Long.MaxValue)
        println(lnr.getLineNumber)*/

        while (line != null) {
            try {
                line = reader.readLine
                val info = line split (",")

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
            catch {
                case ex: Exception => {
                    println(i + " " + ex + " " + line)
                }
            }
        }
        reader.close()

        for(key <- result.keySet)
            writer.write(s"${key._1}\t${key._2} \t${result(key)}" + "\n")

        writer.close()
    }
}
