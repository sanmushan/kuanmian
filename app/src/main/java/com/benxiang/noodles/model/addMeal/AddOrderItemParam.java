package com.benxiang.noodles.model.addMeal;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2017/12/14.
 */

public class AddOrderItemParam {

    @SerializedName("shopCode")
    public String shopCode;
    @SerializedName("Order")
    public OrderData Order;

    public static class OrderData {
        @SerializedName("GUID")
        public String GUID;
        @SerializedName("BillNo")
        public String BillNo;
        @SerializedName("GuestQty")
        public String GuestQty;
        @SerializedName("OrderType")
        public int OrderType;
        @SerializedName("TableNo")
        public String TableNo;
        @SerializedName("SumPrice")
        public double SumPrice;
        @SerializedName("IsPay")
        public int IsPay;
        @SerializedName("PayTime")
        public String PayTime;
        @SerializedName("PayType")
        public int PayType;
        @SerializedName("PayCardCode")
        public String PayCardCode;
        @SerializedName("LID")
        public String LID;
        @SerializedName("Waiter")
        public String Waiter;
        @SerializedName("WaiterName")
        public String WaiterName;
        @SerializedName("Cashier")
        public String Cashier;
        @SerializedName("CashierName")
        public String CashierName;
        @SerializedName("Checker")
        public String Checker;
        @SerializedName("CheckerName")
        public String CheckerName;
        @SerializedName("Operator")
        public String Operator;
        @SerializedName("OperatorName")
        public String OperatorName;
        @SerializedName("OrderDate")
        public String OrderDate;
        @SerializedName("Remark")
        public String Remark;
        @SerializedName("orderDetail")
        public List<OrderDetail> orderDetails;
    }

    public static class OrderDetail {
        @SerializedName("productId")
        public String productId;
        @SerializedName("productName")
        public String productName;
        @SerializedName("price")
        public float price;
        @SerializedName("count")
        public int count;
        @SerializedName("isSuite")
        public int isSuite;
        @SerializedName("CostPrice")
        public float CostPrice;
        @SerializedName("SourcePrice")
        public float SourcePrice;
        @SerializedName("OrderDate")
        public String OrderDate;
        @SerializedName("CheckTime")
        public String CheckTime;
        @SerializedName("Remark")
        public String Remark;
    }
}
