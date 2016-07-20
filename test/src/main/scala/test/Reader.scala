package test

import java.io._
import java.net.URL

import scala.util.control.Breaks._

/**
  * Created by CPU11179-local on 7/13/2016.
  */
class Reader(strFile : String) {

    val bufferedSource = io.Source.fromFile(strFile)
    var data : Array[String] = null
    var nDatas : Int = 0

    def readFile() = {
        val lnr : LineNumberReader  = new LineNumberReader(new FileReader(new File(strFile)));
        lnr.skip(Long.MaxValue);
        nDatas = lnr.getLineNumber()

        data = new Array[String](nDatas)
        var info : String = ""

        var i : Int = 0
        for (line <- bufferedSource.getLines) {
            data(i) = line
            i = i + 1
        }
    }

    def getLocation(ip : Long) : String = {

        var left : Int = 1
        var right : Int = nDatas - 1
        var location = ""

        breakable {while (left <= right) {
            val mid : Int = (left + right) / 2
            val info0 : Array[String]  = data(mid - 1).split(",")
            val info : Array[String]  = data(mid).split(",")
            val ipL = info0(0).substring(1, info0(0).length - 1).toLong
            val ipR = info(0).substring(1, info(0).length - 1).toLong

            if (ipL < ip && ipR >= ip) {
                location = info(1)
                if (info(1).equals("\"Viet Nam\""))
                  location = info(2) + " " + info(3) + " " + location
                break
            }
            else if (ipL >= ip)
                right = mid - 1
            else
                left = mid + 1

        }}

      location
    }

    def ip2Long(ip: String): Long = {
        val atoms: Array[Long] = ip.split("\\.").map(java.lang.Long.parseLong(_))
        val result: Long = (3 to 0 by -1).foldLeft(0L)(
            (result, position) => result | (atoms(3 - position) << position * 8))

        result & 0xFFFFFFFF
    }

    def getIP() : String  = {
        val urlIP : URL = new URL("http://checkip.amazonaws.com");
        val reader : BufferedReader = new BufferedReader(new InputStreamReader(urlIP.openStream()));
        reader.readLine
    }
}
