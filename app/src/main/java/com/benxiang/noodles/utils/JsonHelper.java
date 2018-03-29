package com.benxiang.noodles.utils;

import com.google.gson.Gson;

/**
 * Created by admin on 2016/11/2.
 */

public class JsonHelper {

    private JsonHelper() {

    }

    public static Gson getGson() {
        return GsonHolder.sInstance;
    }

    public static  class GsonHolder {
        public static final Gson sInstance = new Gson();
    }
}
