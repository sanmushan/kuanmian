package com.benxiang.noodles.moudle.makemeal;

import com.benxiang.noodles.base.BaseView;

/**
 * Created by 刘圣如 on 2017/9/19.
 */

public interface QrderQueryView extends BaseView{
    //退菜
    void RetreatFoodSuccess();

    //设置订单状态为成功
    void setOrderNoStatusSuccess();
    //退款
    void refundmentSuccess();
    void refundmentFaile();
}
