package com.benxiang.noodles.model.placeorder;

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
 * Created by 刘圣如 on 2017/9/6.
 */

public class PlaceOrderPresenter extends RxBasePresenter<PlaceOrderView> {
    private static final String TAG = "PayPresenter";

    public void placeOrder(String method, String json) {
        getInitchargingPieApi()
                .placeOrder(NoodlesParams.commonMethod(method, json))
                .compose(RxUtil.<CommonModel<PlaceOrderModel>>toMain())
                .subscribe(new Consumer<CommonModel<PlaceOrderModel>>() {
                    @Override
                    public void accept(@NonNull CommonModel<PlaceOrderModel> commonModel) throws Exception {
//                        Timber.e("下单后获取的数据:"+ JsonHelper.getGson().toJson(commonModel));
                        if (commonModel.status == 1 && commonModel.strMsg.Result.equals("1")) {
                            view.placeOrderSuccess(commonModel.strMsg);
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

    public void retreatFood(String method, String json) {
        getInitchargingPieApi()
                .retreatFood(NoodlesParams.commonMethod(method, json))
                .compose(RxUtil.<CommonModel<StrMsgCommonModel>>toMain())
                .subscribe(new Consumer<CommonModel<StrMsgCommonModel>>() {
                    @Override
                    public void accept(@NonNull CommonModel<StrMsgCommonModel> commonModel) throws Exception {
                        if (commonModel.status == 1 && commonModel.strMsg.Result.equals("1")) {

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
    @Override
    public void attachView(PlaceOrderView view) {
//        this.view=view;
        super.attachView(view);
    }

    @Override
    public void detavh() {

        super.detavh();
    }
}
