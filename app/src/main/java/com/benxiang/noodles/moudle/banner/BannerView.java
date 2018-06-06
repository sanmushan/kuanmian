package com.benxiang.noodles.moudle.banner;

import com.benxiang.noodles.base.BaseView;
import com.benxiang.noodles.model.remote.CommonModel;

/**
 * Created by LIN on 2018/5/11.
 */

public interface BannerView extends BaseView {
    void getBannerSuccess(CommonModel<BannerModel> bannerModel);
}