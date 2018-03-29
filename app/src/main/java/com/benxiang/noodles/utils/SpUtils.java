package com.benxiang.noodles.utils;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

/**
 *
 * @author LIN
 * @date 2018/1/24
 */

public class SpUtils {
    /**
     * 用于保存应用内部数据
      */
    private static SharedPreferences appShared;
    /**
     *  编辑appShared中的数据
     */
    private static SharedPreferences.Editor appEditor;

    public static void init(Context context, String name) {
        appShared = context.getSharedPreferences(name, MODE_PRIVATE);
        appEditor = appShared.edit();
    }

    public static void saveValue(String key, String value) {
        appEditor.putString(key, value);
        appEditor.commit();
    }

    public static String loadValue(String key) {
        return appShared.getString(key, "");
    }

    public static void clear() {
        appEditor = appShared.edit();
        appEditor.clear();
        appEditor.commit();
    }
}
