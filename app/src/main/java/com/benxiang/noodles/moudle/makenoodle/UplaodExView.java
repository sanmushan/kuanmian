package com.benxiang.noodles.moudle.makenoodle;

import com.benxiang.noodles.base.BaseView;

/**
 * Created by 刘圣如 on 2017/9/19.
 */

public interface UplaodExView extends BaseView{
    void UplaodExSuccess();
    void refundmentExSuccess();
    void refundmentFaile();

    void retreaFoodSuccess();
    void retreaFoodFaile();

    void setOrderStatusSuccess();
    void setOrderStatusFail();

    void setOrderStatusByIDSuccess();
    void setOrderStatusByIDFail();
}
