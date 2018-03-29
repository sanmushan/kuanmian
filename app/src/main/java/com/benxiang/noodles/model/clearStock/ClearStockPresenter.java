package com.benxiang.noodles.model.clearStock;

import com.benxiang.noodles.base.RxBasePresenter;
import com.benxiang.noodles.model.remote.CommonModel;
import com.benxiang.noodles.network.NoodlesParams;
import com.benxiang.noodles.rx.RxUtil;
import com.benxiang.noodles.utils.JsonHelper;

import java.sql.Time;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import timber.log.Timber;

import static com.benxiang.noodles.network.ApiFactory.getInitchargingPieApi;

/**
 * Created by Administrator on 2017/12/14.
 */

public class ClearStockPresenter extends RxBasePresenter<ClearStockView> {

    public void clearStock(String method, String json) {
        getInitchargingPieApi()
                .clearStock(NoodlesParams.commonMethod(method, json))
                .compose(RxUtil.<CommonModel<ClearStockModel>>toMain())
                .subscribe(new Consumer<CommonModel<ClearStockModel>>() {
                    @Override
                    public void accept(@NonNull CommonModel<ClearStockModel> commonModel) throws Exception {
                        Timber.e("物品库存清零接口获得的值："+ JsonHelper.getGson().toJson(commonModel));
                        if (commonModel.status == 1 && commonModel.strMsg.Result.equals("1")) {
                            Timber.e("清除数据成功");
                            view.clearStockSuccess(commonModel.strMsg);
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
