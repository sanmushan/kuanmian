package com.benxiang.noodles.data.pay;

/**
 * Created by 刘圣如 on 2017/11/14.
 */

public class PayType {
    //0 = 会员卡支付, 2 = 微信支付, 5 = 多币种支付（启用多币种支付，才可使用支付列表PayList）,6 = 支付宝支付

    public static final int ClUD_CARD_PAY_TYPE = 0;
    public static final int WEIXIN_PAY_TYPE = 2;
    public static final int ANY_PAY_TYPE = 5;
    public static final int ALI_PAY_TYPE = 6;
}
