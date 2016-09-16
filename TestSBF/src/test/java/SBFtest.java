import pintergreg.bloomfilter.ScalableBloomFilter;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Created by CPU11179-local on 9/16/2016.
 */
public class SBFtest {
    public static void main(String[] args) throws IOException {
        ScalableBloomFilter sbf = new ScalableBloomFilter(1000000, 0.001);
        BufferedReader reader = new BufferedReader(new FileReader("Log.txt"));
        String user = null;

        int res = 0;
        String userTest = "2000.d6aa4ce7ae25477b1e34.1470338179746.d3678afc";
        int i = 0;
        try {
            while ((user = reader.readLine()) != null) {
                sbf.add(user.getBytes());
                if (user.equals(userTest))
                    res++;
                i++;
                if (i % 10000000 == 0)
                    System.out.println(i);
            }
        }
        catch (Exception ex){
            System.out.println(i + " " + user);
        }
        reader.close();


        System.out.println(sbf.count(userTest.getBytes()) + " " + res);
    }
}
