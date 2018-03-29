package com.benxiang.noodles.moudle.makemeal;

import com.benxiang.noodles.base.RxBasePresenter;
import com.benxiang.noodles.model.remote.CommonModel;
import com.benxiang.noodles.model.remote.StrMsgCommonModel;
import com.benxiang.noodles.network.NoodlesParams;
import com.benxiang.noodles.rx.RxUtil;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

import static com.benxiang.noodles.network.ApiFactory.getInitchargingPieApi;

/**
 * Created by 刘圣如 on 2017/9/26.
 */

public class OrderQueryPresenter extends RxBasePresenter<QrderQueryView> {
    public void RetreatFood(String method, String json) {
        getInitchargingPieApi()
                .retreatFood(NoodlesParams.commonMethod(method, json))
                .compose(RxUtil.<CommonModel<StrMsgCommonModel>>toMain())
                .subscribe(new Observer<CommonModel<StrMsgCommonModel>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(CommonModel<StrMsgCommonModel> commonModel) {
                        if (commonModel.status == 1 && commonModel.strMsg.Result.equals("1")) {
                            view.RetreatFoodSuccess();
                        } else {
                            view.showNetError(commonModel.strRes);
                        }

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
    public  void setOrderNoStatus(String method, String json) {
        getInitchargingPieApi()
                .setOrderNoStatus(NoodlesParams.commonMethod(method, json))
                .compose(RxUtil.<CommonModel<StrMsgCommonModel>>toMain())
                .subscribe(new Observer<CommonModel<StrMsgCommonModel>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(CommonModel<StrMsgCommonModel> commonModel) {
                        if (commonModel.status==1&&commonModel.strMsg.Result.equals("1")){
                            view.setOrderNoStatusSuccess();
                        }
                        else {
                        }


                    }

                    @Override
                    public void onError(Throwable e) {

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
                            view.refundmentSuccess();
                        }
                        else {
                            view.refundmentFaile();
//                            view.showNetError(commonModel.strMsg.ResultMsg);
                        }
                    }

                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        view.refundmentFaile();
                    }
                });
    }


}

