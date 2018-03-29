package com.benxiang.noodles.moudle.makenoodle;

import com.benxiang.noodles.base.RxBasePresenter;
import com.benxiang.noodles.model.remote.CommonModel;
import com.benxiang.noodles.model.remote.StrMsgCommonModel;
import com.benxiang.noodles.network.NoodlesParams;
import com.benxiang.noodles.rx.RxUtil;
import com.benxiang.noodles.utils.JsonHelper;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import timber.log.Timber;

import static com.benxiang.noodles.network.ApiFactory.getInitchargingPieApi;

/**
 * Created by 刘圣如 on 2017/9/26.
 */

public class UploadExPresenter extends RxBasePresenter<UplaodExView> {
    public void uploadException(String method, String json) {
        getInitchargingPieApi()
                .uploadException(NoodlesParams.commonMethod(method, json))
                .compose(RxUtil.<CommonModel<StrMsgCommonModel>>toMain())
                .subscribe(new Observer<CommonModel<StrMsgCommonModel>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(CommonModel<StrMsgCommonModel> commonModel) {
                        Timber.e("机器异常后获取的值："+ JsonHelper.getGson().toJson(commonModel));
                        if (commonModel.status == 1 && commonModel.strMsg.Result.equals("1")) {
                            view.UplaodExSuccess();
                        } else {
                            view.showNetError(commonModel.strRes);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
//                        view.showNetError();
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
                            view.refundmentExSuccess();
                        } else {
                            view.refundmentFaile();
//                            view.showNetError(commonModel.strMsg.ResultMsg);
                        }
                    }

                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        view.refundmentFaile();
//                        view.showNetError("网络异常");
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
                                view.retreaFoodSuccess();
                        } else {
                            view.retreaFoodFaile();
//                            view.showNetError(commonModel.strMsg.ResultMsg);
                        }
                    }

                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        view.retreaFoodFaile();
//                        view.showNetError("网络解析异常");
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
                        Timber.e("设置订单状态获取的值:"+JsonHelper.getGson().toJson(commonModel));
                        if (commonModel.status==1&&commonModel.strMsg.Result.equals("1")){
                            view.setOrderStatusSuccess();
                        }
                        else {
                            view.setOrderStatusFail();
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

    public void setOrderNoStatusByGuid(String method, String json) {
        getInitchargingPieApi()
                .setOrderNoStatusByGuid(NoodlesParams.commonMethod(method, json))
                .compose(RxUtil.<CommonModel<StrMsgCommonModel>>toMain())
                .subscribe(new Observer<CommonModel<StrMsgCommonModel>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(CommonModel<StrMsgCommonModel> commonModel) {
                        Timber.e("根据GUID设置订单状态获取的值:"+JsonHelper.getGson().toJson(commonModel));
                        if (commonModel.status==1&&commonModel.strMsg.Result.equals("1")){
                            view.setOrderStatusByIDSuccess();
                        }
                        else {
                            view.setOrderStatusByIDFail();
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
}

