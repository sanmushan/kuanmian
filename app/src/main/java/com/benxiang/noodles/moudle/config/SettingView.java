package com.benxiang.noodles.moudle.config;

import com.benxiang.noodles.base.BaseView;
import com.benxiang.noodles.model.remote.MerchantModel;

/**
 * Created by 刘圣如 on 2017/9/6.
 */

public interface SettingView extends BaseView {
    void showSpannerInfo(MerchantModel merchantModel);
    void regiterSuccess();
}
