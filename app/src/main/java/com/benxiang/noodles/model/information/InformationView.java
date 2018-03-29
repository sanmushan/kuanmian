package com.benxiang.noodles.model.information;

import com.benxiang.noodles.base.BaseView;
import com.benxiang.noodles.model.remote.CommonModel;

/**
 * Created by 刘圣如 on 2017/9/19.
 */

public interface InformationView extends BaseView {
    void getInformationSuccess(CommonModel<InformationModle> commonModel);

}
