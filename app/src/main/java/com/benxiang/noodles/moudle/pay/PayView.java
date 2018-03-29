package com.benxiang.noodles.moudle.pay;

import com.benxiang.noodles.base.BaseView;

/**
 * Created by 刘圣如 on 2017/9/6.
 */

public interface PayView extends BaseView {
    void getQrCode(String data);
    void paySuccess();
}
