package com.benxiang.noodles.moudle.pay;

/**
 * Created by 刘圣如 on 2017/9/16.
 */

public class PayParam {
/**
 * " shopCode ": "0001",
 "orderNo": "1000",
 “money”:”100”
 */

public String shopCode;
public String orderNo;
public String money;

public String orderPrice;

    @Override
    public String toString() {
        return "shopCode="+shopCode
                +"\norderNo="+orderNo
                +"\nmoney="+money
                +"\norderPrice="+orderPrice;
    }
}
