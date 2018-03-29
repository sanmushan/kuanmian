package com.benxiang.noodles.model.selloff;

import com.benxiang.noodles.base.RxBasePresenter;
import com.benxiang.noodles.model.remote.CommonModel;
import com.benxiang.noodles.model.remote.StrMsgCommonModel;
import com.benxiang.noodles.network.NoodlesParams;
import com.benxiang.noodles.rx.RxUtil;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

import static com.benxiang.noodles.network.ApiFactory.getInitchargingPieApi;

/**
 * Created by 刘圣如 on 2017/9/6.
 */

public class SellOffPresenter extends RxBasePresenter<SellOffView> {
    private static final String TAG = "PayPresenter";

    public void sellOff(String method, String json) {
        getInitchargingPieApi()
                .sellOff(NoodlesParams.commonMethod(method, json))
                .compose(RxUtil.<CommonModel<StrMsgCommonModel>>toMain())
                .subscribe(new Consumer<CommonModel<StrMsgCommonModel>>() {
                    @Override
                    public void accept(@NonNull CommonModel<StrMsgCommonModel> commonModel) throws Exception {
                        if (commonModel.status == 1 && commonModel.strMsg.Result.equals("1")) {
                        }
                        else {
                            view.showNetError(commonModel.strMsg.ResultMsg);
                        }
                    }

                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                    }
                });
    }
    public void getSellOffList(String method, String json) {
        getInitchargingPieApi()
                .getSellOffList(NoodlesParams.commonMethod(method, json))
                .compose(RxUtil.<CommonModel<SellOffListInfoModel>>toMain())
                .subscribe(new Consumer<CommonModel<SellOffListInfoModel>>() {
                    @Override
                    public void accept(@NonNull CommonModel<SellOffListInfoModel> commonModel) throws Exception {
                        if (commonModel.status == 1 && commonModel.strMsg.Result.equals("1")) {
                        }
                        else {
                            view.showNetError(commonModel.strMsg.ResultMsg);
                        }
                    }

                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                    }
                });
    }



    @Override
    public void attachView(SellOffView view) {
//        this.view=view;
        super.attachView(view);
    }

    @Override
    public void detavh() {

        super.detavh();
    }
}
