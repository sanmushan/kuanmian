package com.benxiang.noodles.model.retreatfood;

import java.util.List;

/**
 * Created by 刘圣如 on 2017/9/30.
 */

public class RetreatFoodParam {

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
        public List<OrderDetailDatas> orderDetail;
    }

    public static class OrderDetailDatas{


        public String productId;
        public String productName;
        public double price;
        public int count;
        public int isSuite;
        public String BackReason ;
        public String BackTime;
        public String Backer ;
        public String BackerName;
        public double CostPrice;
        public double DishSumReal;
        public double SourcePrice;
        public String OrderDate;
        public String CheckTime;
        public String Remark;
    }
}
