package com.benxiang.noodles.moudle.config;

import com.benxiang.noodles.base.RxBasePresenter;
import com.benxiang.noodles.model.remote.CommonModel;
import com.benxiang.noodles.model.remote.RecipeModle;
import com.benxiang.noodles.network.ApiFactory;
import com.benxiang.noodles.network.NoodlesParams;
import com.benxiang.noodles.rx.RxUtil;
import com.benxiang.noodles.utils.JsonHelper;

import io.reactivex.observers.DefaultObserver;
import timber.log.Timber;

/**
 * Created by 刘圣如 on 2017/9/6.
 */

public class SplashPresenter extends RxBasePresenter<SplashView> {
//    private SettingView view;

    public void getRecipe(String method, String json) {
        ApiFactory.getInitchargingPieApi()
                .getRecipe(NoodlesParams.commonMethod(method, json))
                .compose(RxUtil.<CommonModel<RecipeModle>>toMain())
                .subscribe(new DefaultObserver<CommonModel<RecipeModle>>() {
                    @Override
                    public void onNext(CommonModel<RecipeModle> commonModel) {
                        Timber.e("网络解析成功:" + JsonHelper.getGson().toJson(commonModel));
                        if (commonModel.status == 1 && commonModel.strMsg.Result==1) {
                            view.getRecipeSuccess(commonModel.strMsg.recipeData);
                        } else {
                            view.showNetError(commonModel.strRes);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e("网络解析异常");
                        view.getRecipeFaile();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    @Override
    public void attachView(SplashView view) {
        super.attachView(view);
    }

    @Override
    public void detavh() {
        super.detavh();
    }
}
