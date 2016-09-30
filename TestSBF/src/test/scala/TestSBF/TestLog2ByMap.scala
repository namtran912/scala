package TestSBF

import java.io.{BufferedReader, BufferedWriter, FileReader, FileWriter}
import java.util.Calendar

import org.junit.Test

/**
  * Created by CPU11179-local on 9/29/2016.
  */
class TestLog2ByMap {
    @Test
    def test(): Unit = {
        var map: Map[String, Int] = Map()
        val reader = new BufferedReader(new FileReader("C:\\Users\\CPU11179-local\\Desktop\\server_58728080_2016_08_27_09.log"))
        val writer = new BufferedWriter(new FileWriter("C:\\Users\\CPU11179-local\\Desktop\\result2.log"))

        var line: String = ""
        var i: Long = 0
        var result: Map[(String, Int), Int] = Map()

        while (line != null) {
            try {
                line = reader.readLine
                val info = line split (",")

                info(12) match {
                    case "impression" => {
                        val key = info(9) + info(11)
                        if (map.contains(key)) {
                            map += key -> (map(key) + 1)
                            if (map(key) > 20)
                                map -= key
                        }
                        else
                            map += (key -> 1)
                    }
                    case "click" => {
                        val key = info(9) + info(11)
                        if (map.contains(key)) {
                            val count = map(key)
                            if (count > 0) {
                                if (result.contains(info(9), count))
                                    result += (info(9), count) -> (result((info(9), count)) + 1)
                                else
                                    result += (info(9), count) -> 1
                                map -= key
                            }
                        }
                    }
                    case _ => {}
                }
                i += 1
                if (i % 1000000 == 0)
                    println(i + " " + map.size)

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
