package com.benxiang.noodles.moudle.pay;

import android.util.Log;

import com.benxiang.noodles.base.RxBasePresenter;
import com.benxiang.noodles.model.remote.CommonModel;
import com.benxiang.noodles.model.remote.PayModel;
import com.benxiang.noodles.model.remote.QrModel;
import com.benxiang.noodles.network.ApiFactory;
import com.benxiang.noodles.network.NoodlesParams;
import com.benxiang.noodles.rx.RxRetry;
import com.benxiang.noodles.rx.RxUtil;
import com.benxiang.noodles.utils.JsonHelper;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import timber.log.Timber;

import static com.benxiang.noodles.network.ApiFactory.getInitchargingPieApi;

/**
 * Created by 刘圣如 on 2017/9/6.
 */

public class PayPresenter extends RxBasePresenter<PayView> {
    //    private SettingView view;
    private static final String TAG = "PayPresenter";

    public void getQR(String method, String json) {
        getInitchargingPieApi()
                .getQrcodeToNet(NoodlesParams.commonMethod(method, json))
                .compose(RxUtil.<CommonModel<QrModel>>toMain())
                .subscribe(new Consumer<CommonModel<QrModel>>() {
                    @Override
                    public void accept(@NonNull CommonModel<QrModel> commonModel) throws Exception {
//                        Timber.e("获取的二维码的对象："+ JsonHelper.getGson().toJson(commonModel));
                        if (commonModel.status == 1 && commonModel.strMsg.result.equals("1")) {
                            view.getQrCode(commonModel.strMsg.data);
                        }
                        else {
                            view.showNetError("获取二维码失败");
                        }
                    }

                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                            view.showNetError("获取二维码失败");
                    }
                });
    }

    public void payStatus(String method, String json) {
        Disposable disposable=ApiFactory.getInitchargingPieApi().
                getOrderPayStatus(NoodlesParams.commonMethod(method, json))
                .retryWhen(new RxRetry(5,5, TimeUnit.SECONDS))
                .repeatWhen(new Function<Observable<Object>, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(@NonNull Observable<Object> objectObservable) throws Exception {
                        return objectObservable.delay(3, TimeUnit.SECONDS);
                    }
                })
                .takeUntil(new Predicate<CommonModel<PayModel>>() {
                    @Override
                    public boolean test(@NonNull CommonModel<PayModel> payModelCommonModel) throws Exception {
                        return payModelCommonModel.status==1&&payModelCommonModel.strMsg.result.equals("1");
                    }
                })
                .filter(new Predicate<CommonModel<PayModel>>() {
                    @Override
                    public boolean test(@NonNull CommonModel<PayModel> payModelCommonModel) throws Exception {
                        return payModelCommonModel.status==1&&payModelCommonModel.strMsg.result.equals("1");
                    }
                })
                .compose(RxUtil.<CommonModel<PayModel>>toMain())
                .subscribe(new Consumer<CommonModel<PayModel>>() {
                    @Override
                    public void accept(@NonNull CommonModel<PayModel> payModelCommonModel) throws Exception {
                        Log.e(TAG, "accept: " +payModelCommonModel.strMsg.resultMsg);
                        view.paySuccess();

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        view.showNetError("支付失败");
                    }
                });
        addSubscribe(disposable);
    }

    @Override
    public void attachView(PayView view) {
//        this.view=view;
        super.attachView(view);
    }

    @Override
    public void detavh() {

        super.detavh();
    }
}
