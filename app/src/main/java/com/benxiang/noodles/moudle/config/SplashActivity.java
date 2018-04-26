package com.benxiang.noodles.moudle.config;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.benxiang.noodles.BuildConfig;
import com.benxiang.noodles.R;
import com.benxiang.noodles.base.BaseActivity;
import com.benxiang.noodles.contants.Constants;
import com.benxiang.noodles.contants.MethodConstants;
import com.benxiang.noodles.data.DBNoodleHelper;
import com.benxiang.noodles.entrance.WriteFormulaUtil;
import com.benxiang.noodles.model.remote.RecipeModle;
import com.benxiang.noodles.moudle.banner.BannerActivity;
import com.benxiang.noodles.serialport.ComBean;
import com.benxiang.noodles.serialport.cardmac.CardSerialOpenHelper;
import com.benxiang.noodles.serialport.data.sp.FormulaPreferenceConfig;
import com.benxiang.noodles.utils.JsonHelper;
import com.benxiang.noodles.utils.PreferenceUtil;
import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ServiceUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import timber.log.Timber;

/**
 * Created by 刘圣如 on 2017/9/5.
 */

public class SplashActivity extends BaseActivity implements SplashView {

    private SplashPresenter mSplashPresenter;
    private WriteFormulaUtil mWriteFormulaUtil;

    @Override
    public int getContentViewID() {
        return R.layout.activity_splash;
    }

    @Override
    protected void afterContentViewSet() {
        registerMainHandler();
        initView();
    }

    @Override
    protected void onStop() {
        super.onStop();

        if(BarUtils.getNavBarHeight()>0){
            BarUtils.hideNavBar(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSplashPresenter != null) {
            mSplashPresenter.detavh();
            mSplashPresenter = null;
        }
        if (mWriteFormulaUtil != null) {
            mWriteFormulaUtil.recycle();
            mWriteFormulaUtil = null;
        }
    }

    private void initView() {
        DBNoodleHelper.deleteAll();
        mSplashPresenter = new SplashPresenter();
        mSplashPresenter.attachView(this);
        RecipeParam param = new RecipeParam();

        //TODO 机器码
        param.mechanical_num = PreferenceUtil.config().getMacNo(Constants.MAC_NO);

        //配方请求
        if (!TextUtils.isEmpty(param.mechanical_num)){
            mSplashPresenter.getRecipe(MethodConstants.GET_RECIPE_IF, JsonHelper.getGson().toJson(param));
            Log.e("LINBIN","mac_no = " + JsonHelper.getGson().toJson(param));
        }else{
            startNext();
        }
    }

    private void startNext() {
        Intent intent;
        String mac = PreferenceUtil.config().getStringValue(Constants.MAC_NO);
        String noodleNo = PreferenceUtil.config().getStringValue(Constants.NOODLE_NO);
        String riceNo = PreferenceUtil.config().getStringValue(Constants.RICE_NO);
        String freshNo = PreferenceUtil.config().getStringValue(Constants.FRESH_NO);

        if (!TextUtils.isEmpty(noodleNo) || !TextUtils.isEmpty(riceNo)|| !TextUtils.isEmpty(freshNo) || !TextUtils.isEmpty(mac)) {
            intent = new Intent(SplashActivity.this, BannerActivity.class);
        } else {
            intent = new Intent(SplashActivity.this, SettingActivity.class);
        }
        if (BuildConfig.IS_MANY_POWL){
            if (!ServiceUtils.isServiceRunning("com.benxiang.noodles.serialport.service.MakeNoodlesService")){
                //TODO LIN 如需在模拟器上运行，请屏蔽下面代码
//                ServiceUtils.startService(MakeNoodlesService.class);
            }
        }
        startActivity(intent);
        finish();
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showNetError(String error) {
        showError(error);
        finishCurrentPager();
    }

    @Override
    public void getRecipeSuccess(List<RecipeModle.RecipeData> recipeDatas) {
//        r_warm_size==>面加水的大小
        Timber.e("获取的配方:"+recipeDatas.toString());

        //TODO  LIN 打开获取后台配方数据，关闭则是使用PreferenceKey.class中的本地数据 LINBIN
//        for (int i=0;i<=1;i++){
//            RecipeModle.RecipeData recipeData = recipeDatas.get(i);
//            setRecipeDatas(recipeData);
//        }

        //TODO LIN 机器上运行
//        writeFormula();
//        模拟器上运行
        startNext();
    }

    @Override
    public void getRecipeFaile() {
        showError("网络异常，请检查");
        finishCurrentPager();
    }

    /**
     * 接收数据
     * @param comRecData
     */
    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onCardEvent(ComBean comRecData) {
        if (comRecData.makeOrRecycleNoodles == CardSerialOpenHelper.WRITE_FORMULA) {
            mWriteFormulaUtil.handleReceived(comRecData);
        }
    }

    private void writeFormula() {
        mWriteFormulaUtil = new WriteFormulaUtil() {
            @Override
            protected void dealWithError() {
                Timber.e("写配方数据失败");
                showError("写配方数据失败");
                finishCurrentPager();
            }

            @Override
            protected void onFinish() {
                Timber.e("写数据完成");
                getMainHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startNext();
                    }
                }, 3000);
            }
        };
        mWriteFormulaUtil.startWriteFormula();
    }

    private void finishCurrentPager(){
        getMainHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 5000);
    }

    //TODO LIN 设置面主食的种类，如果需要增加多一个C，则需要增加
    //L=0.75*t    L(毫升) t(时间)
    private void setRecipeDatas(RecipeModle.RecipeData recipeData){
        if (recipeData.name.trim().equals("A")){
            FormulaPreferenceConfig.setTypeAWater(recipeData.r_water_time);
            FormulaPreferenceConfig.setTypeAHeat(recipeData.r_warm_time);
            FormulaPreferenceConfig.setTypeABrine(recipeData.r_brine_time);
            FormulaPreferenceConfig.setTypeAVinegar(recipeData.r_vinegar_time);
            FormulaPreferenceConfig.setTypeAOil(recipeData.r_fat_time);
            FormulaPreferenceConfig.setTypeAHeatSize(recipeData.r_warm_size);
        }else if (recipeData.name.trim().equals("B")){
            FormulaPreferenceConfig.setTypeBWater(recipeData.n_water_time);
            FormulaPreferenceConfig.setTypeBHeat(recipeData.n_warm_time);
            FormulaPreferenceConfig.setTypeBBrine(recipeData.n_brine_time);
            FormulaPreferenceConfig.setTypeBVinegar(recipeData.n_vinegar_time);
            FormulaPreferenceConfig.setTypeBOil(recipeData.n_fat_time);
            FormulaPreferenceConfig.setTypeBHeatSize(recipeData.n_warm_size);
        }else if (recipeData.name.trim().equals("C")){
            //TODO 增加做面配方，还需要改，后台数据未配合
            FormulaPreferenceConfig.setTypeCWater(recipeData.n_water_time);
            FormulaPreferenceConfig.setTypeCHeat(recipeData.n_warm_time);
            FormulaPreferenceConfig.setTypeCBrine(recipeData.n_brine_time);
            FormulaPreferenceConfig.setTypeCVinegar(recipeData.n_vinegar_time);
            FormulaPreferenceConfig.setTypeCOil(recipeData.n_fat_time);
            FormulaPreferenceConfig.setTypeCHeatSize(recipeData.n_warm_size);
        }
    }

}
