package com.benxiang.noodles.model.selloff;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by 刘圣如 on 2017/9/30.
 */

public class SellOffListInfoModel {

    @SerializedName("Result")
    public String Result;
    @SerializedName("ResultMsg")
    public String ResultMsg;
    public List<SellOffData> data;

    public class SellOffData{

        @SerializedName("MenuItemCode")
        public String MenuItemCode;
        @SerializedName("MenuItemCName")
        public String MenuItemCName;
        @SerializedName("Standard")
        public String Standard;
        @SerializedName("MenuItemPrice")
        public double MenuItemPrice;
    }

}
