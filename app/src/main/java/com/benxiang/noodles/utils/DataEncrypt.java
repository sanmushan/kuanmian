package com.benxiang.noodles.utils;

import com.benxiang.noodles.contants.Constants;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by 刘圣如 on 2017/9/16.
 */

public class DataEncrypt {
    public static String dataFormat(){
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String nowTime = simpleDateFormat.format(date);
        return nowTime;
    }
    public static String dataFormatString(){
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");
        Date date = new Date();
        String nowTime = simpleDateFormat.format(date).replace("-","").replace(":","");
        return nowTime;
    }
    public static String dataEncrpt(){
        byte[] dates = dataFormat().getBytes();
        byte[] keys = Constants.TOKENKEY.getBytes();
        if (dates == null) {
            return "";
        }
        int j=0;
        String result="";
        for (int i=0;i<dates.length;i++) {
            result+=String.format("%02x", dates[i]^keys[j]).toUpperCase();
            j=(j+1)%8;
        }
        return result;
    }
}
