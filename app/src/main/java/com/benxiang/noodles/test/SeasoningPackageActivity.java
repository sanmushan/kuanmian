
package com.benxiang.noodles.test;

import android.os.Bundle;
import android.view.View;

import com.benxiang.noodles.R;
import com.benxiang.noodles.base.BaseActivity;
import com.benxiang.noodles.entrance.SeasoningPackageUtil;
import com.benxiang.noodles.serialport.ComBean;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import timber.log.Timber;

public class SeasoningPackageActivity extends BaseActivity {

    SeasoningPackageUtil mSeasoningPackageUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public int getContentViewID() {
        return R.layout.activity_seasoning_package;
    }

    @Override
    protected void afterContentViewSet() {}

    public void click(View view){
//        NoodlesIsEnable();
        dropSeasoningPackage();
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
        if (mSeasoningPackageUtil!=null){
            mSeasoningPackageUtil.recycle();
            mSeasoningPackageUtil=null;
        }
    }

    private void dropSeasoningPackage() {
        mSeasoningPackageUtil = new SeasoningPackageUtil() {

            @Override
            protected void dealWithError(String info) {
                Timber.e("写数据失败");
                showError("写数据失败");
            }

            @Override
            protected void onFinish() {
                Timber.e("写数据完成");
            }

        };
//        mSeasoningPackageUtil.startDropPackage(NoodleNameConstants.SUANLA);
    }

    //接收数据
    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onCardEvent(ComBean comRecData) {
//        Timber.e("makeOrRecycleNoodles的值:"+ comRecData.makeOrRecycleNoodles);
//        Timber.e("makeOrRecycleNoodles的值:"+ MyFunc.ByteArrToHex(comRecData.bRec));
        mSeasoningPackageUtil.handleReceived(comRecData);
    }
}

