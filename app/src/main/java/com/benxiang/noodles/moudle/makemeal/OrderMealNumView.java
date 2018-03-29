package com.benxiang.noodles.moudle.makemeal;

import com.benxiang.noodles.base.BaseView;
import com.benxiang.noodles.model.remote.OrderNumQueryModel;

/**
 * Created by 刘圣如 on 2017/9/19.
 */

public interface OrderMealNumView extends BaseView{

    void queryMealNoSuccess(OrderNumQueryModel strMsg);

    void queryMealNoFaile();
}
