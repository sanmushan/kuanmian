package com.benxiang.noodles.moudle.banner;

import com.benxiang.noodles.contants.Constants;
import com.benxiang.noodles.network.ApiFactory;
import com.benxiang.noodles.network.NoodlesApi;
import com.benxiang.noodles.network.RetrofitFactory;

/**
 * Created by LIN on 2018/5/11.
 */

public class ApiBannerFactory {
    private static NoodlesApi noodlesApi;
    public static NoodlesApi getInitchargingPieApi(){
        if (noodlesApi == null) {
            synchronized (ApiFactory.class) {
                if (noodlesApi == null) {
                    noodlesApi = new RetrofitFactory().configRetrofit(NoodlesApi.class, Constants.BaseURL, false);
                }
            }
        }
        return noodlesApi;
    }
}
