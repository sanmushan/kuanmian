package com.benxiang.noodles.model.remote;

import com.google.gson.annotations.SerializedName;

/**
 * Created by 刘圣如 on 2017/9/16.
 */

public class QrModel {
    /**
     Result": "1",
     "data": "sdfsdfsdfsdfsdf",
     "ResultMsg": "获取成功"
     */

    @SerializedName("Result")
    public String result;
    @SerializedName("data")
    public String data;
    @SerializedName("ResultMsg")
    public String resultMsg;


}
