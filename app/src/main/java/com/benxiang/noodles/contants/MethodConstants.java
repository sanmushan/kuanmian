package com.benxiang.noodles.contants;

import com.benxiang.noodles.utils.PreferenceUtil;

/**
 * Created by 刘圣如 on 2017/9/26.
 */

public class MethodConstants {
    //接口方法名
    public static final String ORDER_NUM_IF = "GetBillHisByTakeNumber";//根据取餐号获取订单信息
    public static final String GET_RECIPE_IF = "GetRecipe";//获取配方数据
    public static final String RELIEF_EXCEPTION_IF = "Relieve";//解除异常
    public static final String GETMERCHANT = "GetMerchant";
    public static final String MECHANICAL = "mechanical";
    public static final String RUNWXREFUND = "RunWxRefund";//微信退款
    public static final String RUNalipayREFUND = "RunAlipayRefund";//阿里退款
    public static final String BACKORDERITEM = "BackOrderItem";//退菜
    public static final String KOUKU = "KouKu";//扣库==============================================》没完善
    public static final String GUQING = "GuQing";
    public static final String GETGUQING = "GetGuQing";
    public static final String UPDATEMCHNO = "Updatemchno";//上传异常信息
    public static final String GETMENU = "GetMenu";//获取物品信息
    public static final String GREATEORDER = "CreateOrder";//下单
    public static final String ORDER_STATUS = "SetBillStatusByTakeNumber";//根据取餐号修改订单状态
    public static final String BACK_ORDER = "QTRK";//补货==============================================》没完善
    public static final String SHOPCODE = PreferenceUtil.config().getMacNo(Constants.MAC_NO);//门店编号，和LID的值相同
    public static final String TAKENUMBER = "123456";//取餐号
    public static final String CLEARSTOCK = "ClearStock";//物品库存清零
    public static final String ADDORDERITEM = "AddOrderItem";//物品库存清零
    public static final String ORDER_STATUS_ID = "SetBillStatusByID";
    //成本卡    LINBIN
    public static final String COST_CARD_DATA = "GetCostCard";
}
