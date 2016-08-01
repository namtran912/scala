package test

import java.io._
import java.net.URL
import scala.collection.mutable.ArrayBuffer
import scala.util.control.Breaks._


/**
  * Created by CPU11179-local on 7/13/2016.
  */
class Reader {
    var data: Array[String] = null
    var nDatas: Int = 0

  def modify(): Unit = {
      val bufferedSource = io.Source.fromFile(Process.StrFile)
      val writer: FileWriter = new FileWriter(Process.StrFileModified)
      val logWriter: FileWriter = new FileWriter(Process.StrFileLog)

      val logMapping = new ArrayBuffer[String]()
      var cols0 = Array("*", "*", "*", "*", "*", "*", "*")
      var cols = Array("*", "*", "*", "*", "*", "*", "*")

      for (line <- bufferedSource.getLines) {
          cols = line.split("\",\"").map(_.trim).map((s: String) => "\"" + s + "\"")

          if (cols(2).equals("\"VN\"")) {
              val location = cols(4).substring(1, cols(4).length - 1)
              var id = Process.mapping(true, location)
              if (id == -1 && Process.MappingLocationModified.exists((p: (String, String)) => p._1.equals(location)))
                 id = Process.mapping(true, Process.MappingLocationModified.get(location).get)
              if (id == -1 && logMapping.find((s: String) => s.equals(cols(4))).isEmpty)
                 logMapping.+=(cols(4))

              writer.write(s"${cols(1)}," + "\"" + id + "\"" + s",${cols(3)},${cols(4)},${cols(5)}" + "\n")
              cols0 = cols.clone
          }
          else if (!cols0(2).equals(cols(2))) {
              val location = cols(3).substring(1, cols(3).length - 1)
              var id = Process.mapping(false, location)
              if (id == -1 && Process.MappingLocationModified.exists((p: (String, String)) => p._1.equals(location)))
                 id = Process.mapping(false, Process.MappingLocationModified.get(location).get)
              if (id == -1 && logMapping.find((s: String) => s.equals(cols(3))).isEmpty)
                  logMapping.+=(cols(3))

              writer.write(s"${cols(1)}," + "\"" + id + "\"" + s",${cols(3)}" + "\n")
              cols0 = cols.clone
          }
      }
      if (cols(2).equals("\"VN\"")) {
          val location = cols(4).substring(1, cols(4).length - 1)
          var id = Process.mapping(true, location)
          if (id == -1 && Process.MappingLocationModified.exists((p: (String, String)) => p._1.equals(location)))
              id = Process.mapping(true, Process.MappingLocationModified.get(location).get)
          if (id == -1 && logMapping.find((s: String) => s.equals(cols(4))).isEmpty)
              logMapping.+=(cols(4))

          writer.write(s"${cols(1)}," + "\"" + id + "\"" + s",${cols(3)},${cols(4)},${cols(5)}" + "\n")
      }
      else {
          val location = cols(3).substring(1, cols(3).length - 1)
          var id = Process.mapping(false, location)
          if (id == -1 && Process.MappingLocationModified.exists((p: (String, String)) => p._1.equals(location)))
              id = Process.mapping(false, Process.MappingLocationModified.get(location).get)
          if (id == -1 && logMapping.find((s: String) => s.equals(cols(3))).isEmpty)
              logMapping.+=(cols(3))

          writer.write(s"${cols(1)}," + "\"" + id + "\"" + s",${cols(3)}" + "\n")
      }
  }

  def readFile() = {
      val lnr: LineNumberReader = new LineNumberReader(new FileReader(new File(Process.StrFileModified)))
      lnr.skip(Long.MaxValue);
      nDatas = lnr.getLineNumber
      lnr.close

      val bufferedSource = io.Source.fromFile(Process.StrFileModified)

      data = new Array[String](nDatas)
      var info: String = ""
      var i: Int = 0
      for (line <- bufferedSource.getLines) {
          data(i) = line
          i = i + 1
      }
      bufferedSource.close
  }
}
