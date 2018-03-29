package com.benxiang.noodles;

import com.benxiang.noodles.utils.FormatUtil;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        System.out.print(FormatUtil.flaotToDouble((float) 5.90)+"价格");

//        assertEquals(4, 2 + 2);
//        jiabmi();
//        encrypt("2017-01-01 12:00:00","1234567812345678");
    }

    public String jiabmi() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");
        Date currentTime = new Date();
        String dateString = formatter.format(currentTime);
        System.out.println(dateString.replace(":", "").replace("-", ""));
        System.out.println(dateString);
        return dateString.replace(":", "").replace("-", "");
    }
    public byte[] encrypt(String data,String key) {
        byte[] bytes=data.getBytes();
        byte[] bytekey=key.getBytes();
        if (bytes == null) {
            return null;
        }

        int len = bytes.length;
        String result = "";
        int j=0;
        for (int i=0;i<len;i++){
//            result+=Integer.toHexString(bytes[i]^bytekey[j]);
            result+=String.format("%02x", bytes[i]^bytekey[j]).toUpperCase();
            j=(j+1)%8;
        }

        System.out.println(result);

        return bytes;
    }
}