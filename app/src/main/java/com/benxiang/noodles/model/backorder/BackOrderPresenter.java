package com.benxiang.noodles.model.backorder;

import com.benxiang.noodles.base.RxBasePresenter;
import com.benxiang.noodles.model.remote.CommonModel;
import com.benxiang.noodles.model.remote.StrMsgCommonModel;
import com.benxiang.noodles.network.NoodlesParams;
import com.benxiang.noodles.rx.RxUtil;
import com.benxiang.noodles.utils.JsonHelper;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import timber.log.Timber;

import static com.benxiang.noodles.network.ApiFactory.getInitchargingPieApi;

/**
 * Created by 刘圣如 on 2017/11/17.
 * 补货接口的网络请求
 */

public class BackOrderPresenter extends RxBasePresenter<BackOrderView> {
    public void getBackOrderInfo(String method, String json) {
        getInitchargingPieApi()
                .toBackOrder(NoodlesParams.commonMethod(method, json))
                .compose(RxUtil.<CommonModel<StrMsgCommonModel>>toMain())
                .subscribe(new Consumer<CommonModel<StrMsgCommonModel>>() {
                    @Override
                    public void accept(@NonNull CommonModel<StrMsgCommonModel> commonModel) throws Exception {
                        Timber.e("补货获取的信息："+ JsonHelper.getGson().toJson(commonModel));
                        if (commonModel.status == 1 && commonModel.strMsg.Result.equals("1")) {
                            view.BackOrderSuccess();
                        } else {
                            view.showNetError("网络异常");
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        view.showNetError("网络解析异常");
                    }
                });

    }
    @Override
    public void attachView(BackOrderView view) {
        super.attachView(view);
    }

    @Override
    public void detavh() {
        super.detavh();
    }
}
