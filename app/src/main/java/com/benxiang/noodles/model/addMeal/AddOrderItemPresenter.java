package com.benxiang.noodles.model.addMeal;

import com.benxiang.noodles.base.RxBasePresenter;
import com.benxiang.noodles.model.remote.CommonModel;
import com.benxiang.noodles.network.NoodlesParams;
import com.benxiang.noodles.rx.RxUtil;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import timber.log.Timber;

import static com.benxiang.noodles.network.ApiFactory.getInitchargingPieApi;

/**
 * Created by Administrator on 2017/12/19.
 */

public class AddOrderItemPresenter extends RxBasePresenter<AddOrderItemView> {

    public void addOrderItem(String method, String json) {
        getInitchargingPieApi()
                .addOrderItem(NoodlesParams.commonMethod(method, json))
                .compose(RxUtil.<CommonModel<AddOrderItemModel>>toMain())
                .subscribe(new Consumer<CommonModel<AddOrderItemModel>>() {
                    @Override
                    public void accept(@NonNull CommonModel<AddOrderItemModel> commonModel) throws Exception {
//                        Timber.e("加菜接口获得的值："+ JsonHelper.getGson().toJson(commonModel));
                        if (commonModel.status == 1 && commonModel.strMsg.Result.equals("1")) {
                            Timber.e("加菜成功");
                        } else {
                            view.showNetError(commonModel.strMsg.ResultMsg);
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
