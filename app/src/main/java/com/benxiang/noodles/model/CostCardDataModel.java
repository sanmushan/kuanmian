package com.benxiang.noodles.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author LINBIN
 *         Created by LIN on 2018/1/3.
 *         获取成本卡实体类
 */

public class CostCardDataModel {
    /**
     * {
     * "Result": "1",
     * "ResultMsg": "查询成本卡信息成功",
     * "data": [
     * {
     * "MenuItemCode": "06057",
     * "MenuItemCName": "8寸淡奶油蛋糕",
     * "MenuItemPrice": 218.0,
     * "Standard": "个",
     * "OrderNo": "1",
     * "Products": [
     * {
     * "ProductCode": "06056",
     * "ProductName": "8寸淡奶油蛋糕",
     * "StoreUnit": "个",
     * "StoreStandard": "个"
     * }
     * ]
     * }
     * ]
     * }
     */

    @SerializedName("Result")
    public String Result;
    @SerializedName("ResultMsg")
    public String ResultMsg;

    @SerializedName("data")
    public List<CardData> carDatas;

    public class CardData {
        @SerializedName("MenuItemCode")
        public String MenuItemCode;
        @SerializedName("MenuItemCName")
        public String MenuItemCName;
        @SerializedName("MenuItemPrice")
        public String MenuItemPrice;
        @SerializedName("Standard")
        public String Standard;
        @SerializedName("OrderNo")
        public String OrderNo;

        @SerializedName("Products")
        public List<Product> Products;

    }

    public class Product {
        @SerializedName("ProductCode")
        public String ProductCode;
        @SerializedName("ProductName")
        public String ProductName;
        @SerializedName("StoreUnit")
        public String StoreUnit;
        @SerializedName("StoreStandard")
        public String StoreStandard;

    }


}
