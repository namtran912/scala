package TestSBF

import java.io.{BufferedReader, FileReader}

import org.junit.Test

/**
  * Created by CPU11179-local on 9/20/2016.
  */
class TestLog {
    @Test
    def test(): Unit = {
        val sbf = new ScalableCountingBloomFilter(1000000, 0.05)
        val reader = new BufferedReader(new FileReader("Log.txt"))
        var user: String = null

        var res = 0
        val userTest = "2000.d6aa4ce7ae25477b1e34.1470338179746.d3678afc"
        //var map: Map[String, Int] = Map()
        var i = 0
        try {
            while ((user = reader.readLine) != null) {

                /*if (map.contains(user))
                    map += (user -> (map(user) + 1))
                else
                    map += (user -> 1)*/
                sbf.add(user.getBytes)
                if (user.equals(userTest))
                    res += 1
                i += 1
                if (i % 10000000 == 0)
                    System.out.println(i)

                if (sbf.count(user.getBytes()) == 10)
                    sbf.remove(user.getBytes())

              //  if (map.contains(user) && map(user) == 10)
               //     map -= user

            }
        }
        catch {
            case ex: Exception => {
                System.out.println(i + " " + user)
            }
        }
        reader.close()

        Thread.sleep(5000)
       // System.out.println(map(userTest))
        System.out.println(sbf.count(userTest.getBytes) + " " + res)
    }
}
