package com.benxiang.noodles.model.placeorder;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by 刘圣如 on 2017/9/30.
 */

public class PlaceOrderParam {

    public String shopCode;
    public OrderData Order;

    public static class OrderData{

        public String GUID;
        public String  BillNo ;
        public String GuestQty ;
        public int OrderType;
        public String TableNo;
        public double SumPrice;
        public int IsPay;
        public int PayType;
        public String PayTime;
        public String PayCardCode;
        public String LID;
        public String Waiter;
        public String WaiterName;
        public String Cashier;
        public String CashierName;
        public String Checker;
        public String CheckerName;
        public String Operator;
        public String OperatorName;
        public String OrderDate;
        public String Remark;
//        public List<OrderDetailDatas> orderDetail;

        @SerializedName("orderDetail")
        public List<OrderDetailData> orderDetails;
    }

    public static class OrderDetailData{

        public String productId;
        public String productName;
        public float price;
        public float CostPrice;
        public float SourcePrice;
        public float DishSumReal;
        public int count;
        public int isSuite;
        public String Remark;
    }
}
