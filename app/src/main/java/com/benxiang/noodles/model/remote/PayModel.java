package com.benxiang.noodles.model.remote;

import com.google.gson.annotations.SerializedName;

/**
 * Created by 刘圣如 on 2017/9/16.
 */

public class PayModel {
    /**
     "Result":"1","ResultMsg":"支付成功","data":null
     */

    @SerializedName("Result")
    public String result;

    @SerializedName("ResultMsg")
    public String resultMsg;
    @SerializedName("data")
    public DataModel data;
     public class DataModel{
         @SerializedName("Status")
        public String Status;
         @SerializedName("Message")
        public String Message;
    }

}
