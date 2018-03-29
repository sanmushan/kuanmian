package com.benxiang.noodles.network;

import com.benxiang.noodles.contants.Constants;
import com.benxiang.noodles.utils.DataEncrypt;

import java.util.HashMap;

/**
 * Created by 刘圣如 on 2017/9/5.
 */

public class NoodlesParams {
    public static HashMap<String, String> commonParams() {
        HashMap<String, String> baseParams = new HashMap<>();
        baseParams.put("account", Constants.ACCOUNT);
        baseParams.put("TokenType", Constants.TOKENTYPE);
        baseParams.put("Token", DataEncrypt.dataEncrpt());
        return baseParams;
    }
    public static HashMap <String, String> addMacAddress(String is_unusual,String location,String longitude,String latitude) {
        HashMap<String, String> map = commonParams();
        map.put("is_unusual", is_unusual);
        map.put("location", location);
        map.put("longitude", longitude);
        map.put("latitude", latitude);
        return map;
    }
    public static HashMap<String ,String> commonMethod(String method,String json){
        HashMap<String, String> map = commonParams();
        map.put("Method", method);
        map.put("ReParameter", json);
        return map;
    }

    public static HashMap<String ,String> sliderMethod(String method,String json){
        HashMap<String, String> map = new HashMap<>();
        map.put("Method", method);
        map.put("ReParameter", json);
        return map;
    }
}
