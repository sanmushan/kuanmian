package com.benxiang.noodles.moudle.config;

import android.util.Log;

import com.benxiang.noodles.base.RxBasePresenter;
import com.benxiang.noodles.model.remote.CommonModel;
import com.benxiang.noodles.model.remote.MechanicalModel;
import com.benxiang.noodles.model.remote.MerchantModel;
import com.benxiang.noodles.network.ApiFactory;
import com.benxiang.noodles.network.NoodlesParams;
import com.benxiang.noodles.rx.RxUtil;

import io.reactivex.observers.DefaultObserver;

/**
 * Created by 刘圣如 on 2017/9/6.
 */

public class SettingPresenter extends RxBasePresenter<SettingView> {
    private static final String TAG = "SettingPresenter";

    public void loadSpanner(String method, String json) {
        Log.e(TAG, "loadSpanner: ");
        ApiFactory.getInitchargingPieApi()
                .QuerySupplier(NoodlesParams.commonMethod(method, json))
                .compose(RxUtil.<CommonModel<MerchantModel>>toMain())
                .subscribe(new DefaultObserver<CommonModel<MerchantModel>>() {
                    @Override
                    public void onNext(CommonModel<MerchantModel> merchantModel) {
                        if (merchantModel.status == 1 && merchantModel.strMsg.Result.equals("1")) {
                            view.showSpannerInfo(merchantModel.strMsg);
                        } else {
                            view.showNetError("获取失败");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showNetError("网络异常");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void redigterMchNo(String method, String json) {
        ApiFactory.getInitchargingPieApi().
                addMacAddress(NoodlesParams.commonMethod(method, json))
                .compose(RxUtil.<CommonModel<MechanicalModel>>toMain())
                .subscribe(new DefaultObserver<CommonModel<MechanicalModel>>() {
                    @Override
                    protected void onStart() {
                        super.onStart();
                        view.showLoading();
                    }

                    @Override
                    public void onNext(CommonModel<MechanicalModel> commonModel) {
                        if (commonModel.status == 1 && commonModel.strMsg.Result.equals("1")) {
                            view.regiterSuccess();
                        } else {
                            view.showNetError("注册失败");
                        }
//                        Log.e("接受的数据", "onNext: " + commonModel.status);
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.hideLoading();
                        Log.e("接受的数据", "onError: " + e);
                    }

                    @Override
                    public void onComplete() {
//                        view.hideLoading();
                        Log.e("接受的数据", "onComplete:");
                    }
                });
    }

    @Override
    public void attachView(SettingView view) {
//        this.view=view;
        super.attachView(view);
    }

    @Override
    public void detavh() {

        super.detavh();
    }
}
