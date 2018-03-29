package com.benxiang.noodles.moudle.config;

import com.benxiang.noodles.base.RxBasePresenter;
import com.benxiang.noodles.model.remote.CommonModel;
import com.benxiang.noodles.model.remote.ReliefModle;
import com.benxiang.noodles.model.remote.StrMsgCommonModel;
import com.benxiang.noodles.network.NoodlesParams;
import com.benxiang.noodles.rx.RxUtil;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DefaultObserver;

import static com.benxiang.noodles.network.ApiFactory.getInitchargingPieApi;

/**
 * Created by 刘圣如 on 2017/9/6.
 */

public class MenagePresenter extends RxBasePresenter<MenageView> {
//    private SettingView view;


    public void ExceptionResult(String method, String json) {
        getInitchargingPieApi()
                .ToexceptionRelief(NoodlesParams.commonMethod(method, json))
                .compose(RxUtil.<CommonModel<ReliefModle>>toMain())
                .subscribe(new DefaultObserver<CommonModel<ReliefModle>>() {
                    @Override
                    public void onNext(CommonModel<ReliefModle> commonModel) {
                        if (commonModel.status == 1 && commonModel.strMsg.Result.equals("1")) {
                            view.ReliefSuccess(commonModel.strMsg.ResultMsg);
                        } else {
                            view.showNetError("解除异常失败");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.ReliefFiale();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void refundment(String method, String json) {
        getInitchargingPieApi()
                .refundment(NoodlesParams.commonMethod(method, json))
                .compose(RxUtil.<CommonModel<StrMsgCommonModel>>toMain())
                .subscribe(new Consumer<CommonModel<StrMsgCommonModel>>() {
                    @Override
                    public void accept(@NonNull CommonModel<StrMsgCommonModel> commonModel) throws Exception {
                        if (commonModel.status == 1 && commonModel.strMsg.Result.equals("1")) {
                            view.refundmentSuccess(commonModel.strMsg.ResultMsg);
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

    public void upadeAPP(String method, String json) {
        getInitchargingPieApi()
                .udapeAPP(NoodlesParams.commonMethod(method, json))
                .compose(RxUtil.<CommonModel<StrMsgCommonModel>>toMain())
                .subscribe(new Observer<CommonModel<StrMsgCommonModel>>() {


                    @Override
                    public void onSubscribe(Disposable d) {
                        view.showLoading();
                    }

                    @Override
                    public void onNext(CommonModel<StrMsgCommonModel> strMsgCommonModelCommonModel) {
                        if (strMsgCommonModelCommonModel.status == 1 &&
                                strMsgCommonModelCommonModel.strMsg.Result.equals("1")) {
                            view.upedaAppSuccess(strMsgCommonModelCommonModel.strMsg.ResultMsg);
                        }
                        else {
                            view.showNetError(strMsgCommonModelCommonModel.strMsg.ResultMsg);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                            view.showNetError("网络异常，请检查网络连接");
                    }

                    @Override
                    public void onComplete() {
                        view.hideLoading();
                    }
                });
    }
    @Override
    public void attachView(MenageView view) {
        super.attachView(view);
    }

    @Override
    public void detavh() {
        super.detavh();
    }
}
