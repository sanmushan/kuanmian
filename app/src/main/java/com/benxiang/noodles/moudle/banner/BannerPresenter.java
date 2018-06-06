package com.benxiang.noodles.moudle.banner;

import android.util.Log;

import com.benxiang.noodles.base.RxBasePresenter;
import com.benxiang.noodles.model.Slider.SliderModel;
import com.benxiang.noodles.model.remote.CommonModel;
import com.benxiang.noodles.network.NoodlesParams;
import com.benxiang.noodles.rx.RxUtil;
import com.benxiang.noodles.utils.JsonHelper;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import timber.log.Timber;

import static com.benxiang.noodles.moudle.banner.ApiBannerFactory.getInitchargingPieApi;


/**
 * Created by LIN on 2018/5/11.
 */

public class BannerPresenter extends RxBasePresenter<BannerView> {

    public void getBanner(String method, final String json){
        Timber.d("method = "+ method);
        Timber.d("keys = "+ json);
        getInitchargingPieApi()
                .getBannerData(NoodlesParams.commonMethod(method, json))
                .compose(RxUtil.<CommonModel<BannerModel>>toMain())
                .subscribe(new Consumer<CommonModel<BannerModel>>() {
                    @Override
                    public void accept(@NonNull CommonModel<BannerModel> bannerModel) throws Exception {
                        Log.d("Banner","获取JSON数据成功 : " + JsonHelper.getGson().toJson(bannerModel));
                        if (bannerModel.status == 1 && bannerModel.strMsg.Result.equals("1")) {
                            view.getBannerSuccess(bannerModel);

                        } else {
                            view.showNetError(bannerModel.strMsg.ResultMsg);
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
