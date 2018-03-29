package com.benxiang.noodles.moudle.makemeal;

import com.benxiang.noodles.base.RxBasePresenter;
import com.benxiang.noodles.model.remote.CommonModel;
import com.benxiang.noodles.model.remote.OrderNumQueryModel;
import com.benxiang.noodles.network.NoodlesParams;
import com.benxiang.noodles.rx.RxUtil;
import com.benxiang.noodles.utils.JsonHelper;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

import static com.benxiang.noodles.network.ApiFactory.getInitchargingPieApi;

/**
 * Created by 刘圣如 on 2017/9/26.
 */

public class OrderMealNumPresenter extends RxBasePresenter<OrderMealNumView> {
    public  void orderQuery(String method, String json) {
        getInitchargingPieApi()
                .getOrderDetial(NoodlesParams.commonMethod(method, json))
                .compose(RxUtil.<CommonModel<OrderNumQueryModel>>toMain())
                .subscribe(new Observer<CommonModel<OrderNumQueryModel>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(CommonModel<OrderNumQueryModel> commonModel) {
                        Timber.e("根据取餐号获取订单状态:"+ JsonHelper.getGson().toJson(commonModel));
                        if (commonModel.status==1&&commonModel.strMsg.result.equals("1")){
                            view.queryMealNoSuccess(commonModel.strMsg);
                            Timber.e("订单时间"+commonModel.status);
                            Timber.e("订单时间"+commonModel.strMsg.result);
                            Timber.e("订单时间"+commonModel.strMsg.billHis.BillNo);
                        }
                        else {
                            view.showNetError(commonModel.strMsg.resultMsg);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.queryMealNoFaile();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

}

