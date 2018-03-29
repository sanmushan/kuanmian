package com.benxiang.noodles.network;

import com.benxiang.noodles.contants.Constants;

/**
 * Created by 刘圣如 on 2017/9/5.
 */

public class ApiFactory {
    private static NoodlesApi noodlesApi;
    public static NoodlesApi getInitchargingPieApi(){
        if (noodlesApi == null) {
            synchronized (ApiFactory.class) {
                if (noodlesApi == null) {
                    noodlesApi = new RetrofitFactory().configRetrofit(NoodlesApi.class, Constants.URL, false);
                }
            }
        }
        return noodlesApi;
    }
}
