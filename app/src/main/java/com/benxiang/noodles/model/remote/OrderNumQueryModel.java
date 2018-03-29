package com.benxiang.noodles.model.remote;

import com.benxiang.noodles.model.TestModle;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by 刘圣如 on 2017/9/26.
 */

public class OrderNumQueryModel {
    /**
     * "Result": "1",
     * "ResultMsg": "查询成功"
     * "BillHis": {
     * "BillItems": [{   ,
     * "ItemName": "老卤鸡爪","ItemPrice": 8.00,  "ItemCount": 1},{
     * "ItemName": "老卤鸡爪","ItemPrice": 8.00,  "ItemCount": 1,}]
     * ,"BillNo": null, "BillItemCount": 1,   "BillDateTime": "2016-09-21T15:43:29","OrderTotal": 8.00,}
     */


    @SerializedName("Result")
    public String result;

    @SerializedName("ResultMsg")
    public String resultMsg;

    @SerializedName("BillHis")
    public BillHis billHis;

    public class BillHis {
        @SerializedName("BillItems")
        public List<BillItems> billItemsList;
        @SerializedName("SettlementItems")
        public List<TestModle.Data> SettlementItems;
        @SerializedName("billPayNotice")
        public List<TestModle.Data> billPayNotice;
        @SerializedName("billPayNoticeDetail")
        public List<TestModle.Data> billPayNoticeDetail;
        @SerializedName("GUID")
        public String GUID;
        @SerializedName("BillNo")
        public String BillNo;
        @SerializedName("BillType")
        public String BillType;
        @SerializedName("BillStatus")
        public String BillStatus;
        @SerializedName("BillItemCount")
        public int BillItemCount;
        @SerializedName("BillItemReCount")
        public int BillItemReCount;
        @SerializedName("BillDate")
        public String BillDate;
        @SerializedName("BillDateTime")
        public String BillDateTime;
        @SerializedName("TableNo")
        public String TableNo;
        @SerializedName("OrderTotal")
        public double OrderTotal;
        @SerializedName("OrderDcTotal")
        public double OrderDcTotal;
        @SerializedName("OrderReTotal")
        public int OrderReTotal;
        @SerializedName("OrderReDcTotal")
        public int OrderReDcTotal;
        @SerializedName("Discount")
        public int Discount;
        @SerializedName("IsPay")
        public int IsPay;
        @SerializedName("PayTime")
        public String PayTime;
        @SerializedName("PayType")
        public String PayType;
        @SerializedName("PayCardCode")
        public String PayCardCode;
        @SerializedName("WXCouponID")
        public String WXCouponID;
        @SerializedName("WXCouponCode")
        public String WXCouponCode;
        @SerializedName("OutTradeNo")
        public String OutTradeNo;
        @SerializedName("TotalFee")
        public int TotalFee;
        @SerializedName("TransactionID")
        public String TransactionID;
        @SerializedName("PrepayId")
        public String PrepayId;
        @SerializedName("PrepayTime")
        public String PrepayTime;
        @SerializedName("MemberID")
        public String MemberID;
        @SerializedName("CustomerName")
        public String CustomerName;
        @SerializedName("CustomerPhone")
        public String CustomerPhone;
        @SerializedName("CustomerAddress")
        public String CustomerAddress;
        @SerializedName("CustomerCount")
        public int CustomerCount;
        @SerializedName("CustomerMealTime")
        public String CustomerMealTime;
        @SerializedName("CustomerRemark")
        public String CustomerRemark;
        @SerializedName("LID")
        public String LID;
        @SerializedName("IsDelete")
        public int IsDelete;
        @SerializedName("SyncTime")
        public String SyncTime;
        @SerializedName("IsSync")
        public int IsSync;
        @SerializedName("SMPos_BillGUID")
        public String SMPos_BillGUID;
        @SerializedName("SMPos_BillNo")
        public String SMPos_BillNo;
        @SerializedName("SMPos_BillDate")
        public String SMPos_BillDate;
        @SerializedName("PrintStatus")
        public int PrintStatus;
        @SerializedName("WaiterName")
        public String WaiterName;
        @SerializedName("Waiter")
        public String Waiter;
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
        /*   @SerializedName("BillNo")
           public String billNo;
           @SerializedName("BillItemCount")
           public int billItemCount;

           @SerializedName("BillDateTime")
           public String billDateTime;
           @SerializedName("OrderTotal")
           public int orderTotal;
           @SerializedName("PayType")
           public int payType;*/
        public class BillItems {

            @SerializedName("GUID")
            public String GUID;
            @SerializedName("SID")
            public String SID;
            @SerializedName("BillGUID")
            public String BillGUID;
            @SerializedName("BillDate")
            public String BillDate;
            @SerializedName("BillDateTime")
            public String BillDateTime;
            @SerializedName("ItemCode")
            public String ItemCode;
            @SerializedName("ItemName")
            public String ItemName;
            @SerializedName("ItemPrice")
            public double ItemPrice;
            @SerializedName("ItemDcPrice")
            public double ItemDcPrice;
            @SerializedName("ItemStandard")
            public String ItemStandard;
            @SerializedName("ItemStandardNo")
            public String ItemStandardNo;
            @SerializedName("ItemCount")
            public int ItemCount;
            @SerializedName("ItemReCount")
            public int ItemReCount;
            @SerializedName("ItemAdditions")
            public int ItemAdditions;
            @SerializedName("LockSoldOut")
            public int LockSoldOut;
            @SerializedName("LockSoldOutTime")
            public String LockSoldOutTime;
            @SerializedName("Discount")
            public int Discount;
            @SerializedName("Remark")
            public String Remark;
            @SerializedName("MealID")
            public String MealID;
            @SerializedName("MealName")
            public String MealName;
            @SerializedName("IsPay")
            public int IsPay;
            @SerializedName("SyncTime")
            public String SyncTime;
            @SerializedName("IsSync")
            public int IsSync;
            @SerializedName("BackReason")
            public String BackReason;
            @SerializedName("BackTime")
            public String BackTime;
            @SerializedName("Backer")
            public String Backer;
            @SerializedName("BackerName")
            public String BackerName;
            @SerializedName("CostPrice")
            public int CostPrice;
            @SerializedName("MenuTypeCode")
            public String MenuTypeCode;
            @SerializedName("MenuTypeName")
            public String MenuTypeName;
        }
           /* @SerializedName("ItemName")
            public String goodsName;

            @SerializedName("ItemPrice")
            public float goodsPrice;
            @SerializedName("ItemCount")
            public int goodsNum;
        }*/
    }

}
