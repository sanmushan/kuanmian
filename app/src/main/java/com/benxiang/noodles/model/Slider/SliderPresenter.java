package com.benxiang.noodles.model.Slider;

import com.benxiang.noodles.base.RxBasePresenter;
import com.benxiang.noodles.model.remote.CommonModel;
import com.benxiang.noodles.network.NoodlesParams;
import com.benxiang.noodles.rx.RxUtil;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

import static com.benxiang.noodles.network.ApiFactory.getInitchargingPieApi;

/**
 * Created by 刘圣如 on 2017/11/11.
 */

public class SliderPresenter extends RxBasePresenter<SliderView>{
    public void slideShow(String method, String json) {
        getInitchargingPieApi()
                .flexSliderChange(NoodlesParams.sliderMethod(method, json))
                .compose(RxUtil.<CommonModel<SliderModel>>toMain())
                .subscribe(new Consumer<CommonModel<SliderModel>>() {
                    @Override
                    public void accept(@NonNull CommonModel<SliderModel> commonModel) throws Exception {
                        if (commonModel.status == 1 && commonModel.strMsg.Result.equals("1")) {
                        }
                        else {
                        }
                    }

                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                    }
                });
    }
}
