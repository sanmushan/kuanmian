package com.benxiang.noodles.model.remote;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by 刘圣如 on 2017/9/6.
 */

public class MerchantModel {

    /**
     * "Result": "1",
     "ResultMsg": "查询成功"
     "data": [
     {
     "id": 1,
     "merchant_name": "系统"
     },
     {
     "id": 3,
     "merchant_name": "广东省分部"
     }
     ]
     */

    @SerializedName("ResultMsg")
    public String ResultMsg;
    @SerializedName("Result")
    public String Result;
    @SerializedName("data")
    public List<MerchantData> merchantDatas;

    public class MerchantData{

        @SerializedName("id")
        public int id;
        @SerializedName("merchant_name")
        public String merchant_name;
    }

}
