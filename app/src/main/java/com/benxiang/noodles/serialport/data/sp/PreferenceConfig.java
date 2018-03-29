package com.benxiang.noodles.serialport.data.sp;

import com.benxiang.noodles.utils.PreferenceNoodlesUtil;

/**
 * Created by admin on 2017/9/13.
 */

public class PreferenceConfig {

    private static String TYPE_A_MIN = "type_a_min";
    private static String TYPE_A_MAX = "type_a_max";
    private static String TYPE_B_MIN = "type_b_min";
    private static String TYPE_B_MAX = "type_b_max";
    private static String TYPE_C_MIN = "type_c_min";
    private static String TYPE_C_MAX = "type_c_max";

    public static int getTypeAMin() {
        return PreferenceNoodlesUtil.config().getInt(TYPE_A_MIN,4);
    }

    public static int getTypeAMax() {
        return PreferenceNoodlesUtil.config().getInt(TYPE_A_MAX,18);
    }

    public static int getTypeBMin() {
        return PreferenceNoodlesUtil.config().getInt(TYPE_B_MIN,19);
    }

    public static int getTypeBMax() {
        return PreferenceNoodlesUtil.config().getInt(TYPE_B_MAX,36);
    }

    public static int getTypeCMin() {
        return PreferenceNoodlesUtil.config().getInt(TYPE_C_MIN,25);
    }

    public static int getTypeCMax() {
        return PreferenceNoodlesUtil.config().getInt(TYPE_C_MAX,36);
    }

    public static void setTypeAMin(int typeAMin) {
        PreferenceNoodlesUtil.config().put(TYPE_A_MIN,typeAMin);
    }

    public static void setTypeAMax(int typeAMin) {
        PreferenceNoodlesUtil.config().put(TYPE_A_MAX,typeAMin);
    }

    public static void setTypeBMin(int typeBMin) {
        PreferenceNoodlesUtil.config().put(TYPE_B_MIN,typeBMin);
    }

    public static void setTypeBMax(int typeBMax) {
        PreferenceNoodlesUtil.config().put(TYPE_B_MAX,typeBMax);
    }

    public static void setTypeCMin(int typeCMin) {
        PreferenceNoodlesUtil.config().put(TYPE_C_MIN,typeCMin);
    }

    public static void setTypeCMax(int typeCMax) {
        PreferenceNoodlesUtil.config().put(TYPE_C_MAX,typeCMax);
    }

}
