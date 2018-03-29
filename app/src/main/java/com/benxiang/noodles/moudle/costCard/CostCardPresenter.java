package com.benxiang.noodles.moudle.costCard;

import com.benxiang.noodles.base.RxBasePresenter;
import com.benxiang.noodles.model.CostCardDataModel;
import com.benxiang.noodles.model.remote.CommonModel;
import com.benxiang.noodles.network.NoodlesParams;
import com.benxiang.noodles.rx.RxUtil;
import com.benxiang.noodles.utils.JsonHelper;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import timber.log.Timber;

import static com.benxiang.noodles.network.ApiFactory.getInitchargingPieApi;

/**
 * Created by admin on 2018/1/5.
 */

public class CostCardPresenter extends RxBasePresenter<CostCardView>{

    //TODO 请求成本卡信息 LINBIN
    public void getCostCard(String method, final String json){
        getInitchargingPieApi()
                .getCostCardData(NoodlesParams.commonMethod(method, json))
                .compose(RxUtil.<CommonModel<CostCardDataModel>>toMain())
                .subscribe(new Consumer<CommonModel<CostCardDataModel>>() {
                    @Override
                    public void accept(@NonNull CommonModel<CostCardDataModel> cardDataModel) throws Exception {
                        Timber.e("成本卡信息："+ JsonHelper.getGson().toJson(cardDataModel));
                        if (cardDataModel.status == 1 && cardDataModel.strMsg.Result.equals("1")) {
                            view.getCostCardSuccess(cardDataModel.strMsg);
                        } else {
                            view.showNetError(cardDataModel.strMsg.ResultMsg);

                        }
                    }

                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        view.showNetError("网络解析异常");
                    }
                });
    }


}
