package com.benxiang.noodles.utils;

import android.util.Log;

import com.benxiang.noodles.contants.Constants;
import com.blankj.utilcode.util.SPUtils;


/**
 * Created by 刘圣如 on 2017/9/5.
 */

public class PreferenceUtil {
    private static final String CONFIG = "config";
    private SPUtils configPreference;
    private static PreferenceUtil preferenceUtil;
    private static final Object monitor = new Object();

    private PreferenceUtil() {
        configPreference = SPUtils.getInstance(CONFIG);
    }

    public static PreferenceUtil config() {
        if (preferenceUtil == null) {
            synchronized (monitor) {
                preferenceUtil = new PreferenceUtil();
            }
        }
        return preferenceUtil;
    }

    public void setStringValue(String key, String value) {
        if (configPreference != null) {
            configPreference.put(key, value);
        }
    }

    public void setIntValue(String key, int value) {
        if (configPreference != null) {
            configPreference.put(key, value);
        }
    }
    public void setFloatValue(String key, float value) {
        if (configPreference != null) {
            configPreference.put(key, value);
        }
    }

    public void setBooleanValue(String key, boolean value) {
        if (configPreference != null) {
            configPreference.put(key, value);
        }
    }

    public String getStringValue(String key) {
        if (configPreference != null) {
            return configPreference.getString(key);
        }
        return "";
    }

    public int getIntValue(String key) {
        if (configPreference != null) {
            return configPreference.getInt(key);
        }
        return 0;
    }

    public boolean getBooleanValue(String key) {
        if (configPreference != null) {
            return configPreference.getBoolean(key, false);
        }
        return false;
    }

    public float getFloatValue(String key) {
        if (configPreference != null) {
            Log.e("啦啦", "getFloatValue: " +configPreference.getFloat(key, 0));
            return configPreference.getFloat(key, 0);
        }
        return 0;
    }
    public String getMacNo(String key) {
        if (configPreference != null) {
            return configPreference.getString(key);
        }
        return "";
    }

    public String getHttpAddress(String key) {
        if (configPreference != null) {
            return configPreference.getString(key, Constants.BaseURL);
        }
        return "";
    }
}
