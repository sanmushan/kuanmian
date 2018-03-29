package com.benxiang.noodles.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by 刘圣如 on 2017/9/21.
 */

public class TestModle {

    @SerializedName("ResultMsg")
    public String ResultMsg;

    @SerializedName("Result")
    public String Result;
    @SerializedName("data")
    public Data data;

    public static class Data {
        @SerializedName("Status")
        public String Status;
        @SerializedName("Message")
        public String Message;
    }
}
