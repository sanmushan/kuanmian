package com.benxiang.noodles.moudle.costCard;

import com.benxiang.noodles.base.BaseView;
import com.benxiang.noodles.model.CostCardDataModel;

/**
 * Created by Zeqiang Fang on 2018/1/5.
 */

public interface CostCardView extends BaseView{
    //获取成本卡信息成功 LINBIN
    void getCostCardSuccess(CostCardDataModel cardDataModel);
}
