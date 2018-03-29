package com.benxiang.noodles.utils;

import java.text.DecimalFormat;

/**
 * Created by 刘圣如 on 2017/11/7.
 */

public class FormatUtil {
    public static double flaotToDouble(float data) {

        DecimalFormat df = new DecimalFormat("######0.00");
        double doubleData = Double.parseDouble(String.valueOf(data));
        return doubleData;
    }
}

