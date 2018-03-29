package com.benxiang.noodles.model.lottery;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by 刘圣如 on 2017/11/25.
 */

public class BeginLotteryModel {

    public int lotteryNum;
    public List<SellOutIyem> sellOutIyem;
    public class SellOutIyem{

        @SerializedName("MenuTypeCode")
        public String MenuTypeCode;
        @SerializedName("MenuItemCode")
        public String MenuItemCode;
        @SerializedName("MenuItemCName")
        public String MenuItemCName;
        @SerializedName("stockNum")
        public int stockNum;
    }

}
