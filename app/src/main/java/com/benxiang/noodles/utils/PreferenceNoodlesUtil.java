package com.benxiang.noodles.utils;

import com.blankj.utilcode.util.SPUtils;

/**
 * Created by ZeQiang Fang on 2017/9/13.
 */

public class PreferenceNoodlesUtil {

    private static final String CONFIG = "noodles";
    private static SPUtils configPreference;
    private static final Object monitor = new Object();

    public static SPUtils config() {
        if (configPreference == null) {
            synchronized (monitor) {
                configPreference = SPUtils.getInstance(CONFIG);
            }
        }
        return configPreference;
    }
}
