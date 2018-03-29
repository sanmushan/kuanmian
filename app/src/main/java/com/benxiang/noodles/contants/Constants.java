package com.benxiang.noodles.contants;

import com.benxiang.noodles.utils.PreferenceUtil;

/**
 * Created by 刘圣如 on 2017/8/29.
 */

public class Constants {
    public static final String BaseURL="http://120.76.52.14:8088";
    public static final String URL= PreferenceUtil.config().getHttpAddress(Constants.HTTP_ADDRESS)+"/STTXPTAPI/";
//    public static final String URL= "http://120.76.52.14:8088/";

    //抽奖网页
//    public static final String LOCKY_DRAW_URL = "http://120.76.52.14:8088/MMB/Games/TurntableGame_Android.aspx?debug=true";
    public static final String LOCKY_DRAW_URL = "http://120.76.52.14:8088/MMB/Games/TurntableGame_Android.aspx";
    //抽奖开始
    public static final String BEGIN_LOTTERY = "beginLottery";
    public static final String COLSE_LOTTERY = "closeLottery";


    public static final String HTTP_ADDRESS="http_address";
    public static final String TOKENKEY="1234567812345678";
    public static final String TOKENTYPE="xor";
    public static final String ACCOUNT="emian";

    public static final int COUNT_DOWN_TIME=120;
    public static final int COUNT_DOWN_WAIT_TIME=60;
    public static final int Wait_COUNT_DOWN_TIME = 1000 * 20;
    public static final int pay_COUNT_DOWN_TIME = 1000 * 20;
    //启动设备
    public static final String SRARTDEVICE="locationlat";
    public static final String STOPDEVICE="locationlat";
    //sp存储中的key
    public static final String LOCATIONLAT="locationlat";
    public static final String LOCATIONLONG = "locationlong";
    public static final String MAC_NO = "mac_no";
    public static final String SPINNER_ID="spinner_id";


    //支付
    public static final int ALIPAY=6;
    public static final int WEIXIN=2;


    //设置

    public static final String NOODLE_NO="noodleNo";
    public static final String NOODLE_PRICE="noodlePrice";
    public static final String RICE_NO="rice_No";
    public static final String RICE_PRICE="rice_Price";

    //新鲜面
    public static final String FRESH_NO = "fresh_No";
    public static final String FRESH_PRICE = "fresh_Price";


    //机器异常

    public static final String MACHINE_EXCEPTION = "mac_exception";

    //米粉是否做完:0:还没开始做，1：正在做，2：已做完
    public static final int SIGN_NOT_DO = 0;
    public static final int SIGN_DOING = 1;
    public static final int SIGN_HAS_DONE = 2;

}
