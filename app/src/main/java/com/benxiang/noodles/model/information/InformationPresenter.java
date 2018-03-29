package com.benxiang.noodles.model.information;

import android.util.Log;

import com.benxiang.noodles.base.RxBasePresenter;
import com.benxiang.noodles.model.remote.CommonModel;
import com.benxiang.noodles.network.NoodlesParams;
import com.benxiang.noodles.rx.RxUtil;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import timber.log.Timber;

import static com.benxiang.noodles.network.ApiFactory.getInitchargingPieApi;

/**
 * Created by 刘圣如 on 2017/9/6.
 */

public class InformationPresenter extends RxBasePresenter<InformationView> {

    private static final String TAG = "PayPresenter";

    public void getInformation(String method, String json) {
        getInitchargingPieApi()
                .getInformation(NoodlesParams.commonMethod(method, json))
                .compose(RxUtil.<CommonModel<InformationModle>>toMain())
                .subscribe(new Consumer<CommonModel<InformationModle>>() {
                    @Override
                    public void accept(@NonNull CommonModel<InformationModle> commonModel) throws Exception {
                        if (commonModel.status == 1 && commonModel.strMsg.Result.equals("1")) {
                            Timber.e("物品的种类："+commonModel.strMsg.menuData.get(0).MenuTypeName);
                            for (int i=0;i<commonModel.strMsg.menuData.get(0).menuItemData.size();i++) {
                                Timber.e("物品的名称：" + commonModel.strMsg.menuData.get(0).menuItemData.get(i).MenuItemCName);
                                Timber.e("物品的价格：" + commonModel.strMsg.menuData.get(0).menuItemData.get(i).MenuItemPrice);
                            }
                            view.getInformationSuccess(commonModel);
                        } else {
                            view.showNetError("网络异常");
                        }
                    }

                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        view.showNetError("机器维修中，请稍后再进行购买");
                        Log.e("Error","InformationPresenter = "+throwable);
                    }
                });
    }


    @Override
    public void attachView(InformationView view) {
        super.attachView(view);
    }

    @Override
    public void detavh() {

        super.detavh();
    }
}
