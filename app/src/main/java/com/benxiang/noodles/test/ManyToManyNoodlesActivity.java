package com.benxiang.noodles.test;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.benxiang.noodles.MainActivity;
import com.benxiang.noodles.R;
import com.benxiang.noodles.base.BaseActivity;
import com.benxiang.noodles.contants.NoodleNameConstants;
import com.benxiang.noodles.data.table.RiceOrderND;
import com.benxiang.noodles.entrance.ManyToManyNoodlesUtil;
import com.benxiang.noodles.entrance.RecycleNoodlesUtil;
import com.benxiang.noodles.entrance.SeasoningPackageUtil;
import com.benxiang.noodles.serialport.bean.ErrorEvent;
import com.benxiang.noodles.serialport.service.MakeNoodlesService;
import com.blankj.utilcode.util.LogUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;


public class ManyToManyNoodlesActivity extends BaseActivity {

    private ManyToManyNoodlesUtil mManyToManyNoodlesUtil;
    private RecycleNoodlesUtil recycleNoodlesUtil;
    private SeasoningPackageUtil mSeasoningPackageUtil;
    private EditText etNoodles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getContentViewID() {
        return R.layout.activity_many_to_many;
    }

    @Override
    protected void afterContentViewSet() {
        etNoodles = (EditText) findViewById(R.id.et_noodles);
        setEnableCountdown(false,5);
    }

    @Override
    protected void onStart() {
        super.onStart();
//        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
//        EventBus.getDefault().unregister(this);
    }

    Intent intent;
    public void click(View view){
        //开启服务
        intent = new Intent(this,MakeNoodlesService.class);
        startService(intent);

//        initReadCard();
    }

    public void click2(View view){

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void initReadCard() {
        if (mManyToManyNoodlesUtil == null) {
            mManyToManyNoodlesUtil = new ManyToManyNoodlesUtil() {

                @Override
                protected void dealWithError(String info) {
                    makeToast("错误码:"+info);
                    Timber.e("错误码:"+info);
                }

                @Override
                protected void onFinish(RiceOrderND riceOrderND) {
                    makeToast("制作"+riceOrderND.noodleNo+"米粉完成,请取粉,noodle_state为"+riceOrderND.noodleState);
                    //开始倒计时，开始检测取料箱是否为空
                    Timber.e("制作"+riceOrderND.noodleNo+"米粉完成,请取粉,noodle_state为"+riceOrderND.noodleState);
                    setEnableCountdown(true,5);
                    startDownTime();
                    initRecycleNoodles();
                    recycleNoodlesUtil.startCheckBox();
                }

                @Override
                protected void onAllFinish(RiceOrderND riceOrderND) {
                    makeToast("制作"+riceOrderND.noodleNo+"米粉完成,请取粉,noodle_state为"+riceOrderND.noodleState);
                    //开始倒计时，开始检测取料箱是否为空
                    Timber.e("制作"+riceOrderND.noodleNo+"米粉完成,请取粉,noodle_state为"+riceOrderND.noodleState);
                    setEnableCountdown(true,5);
                    startDownTime();
                    initRecycleNoodles();
                    recycleNoodlesUtil.startCheckBox();
                }

                @Override
                protected void dropPackage(RiceOrderND riceOrderND) {
                    dropSeasoningPackage(riceOrderND);
                }

                @Override
                protected void choiceNoodles(int noodlesNo) {

                }

                @Override
                protected void startOpenSteam() {

                }
            };
        }


        List<RiceOrderND> list = new ArrayList<RiceOrderND>();
        list.removeAll(null);

        RiceOrderND riceOrderND = new RiceOrderND();
        riceOrderND.noodleNo = 7;
        riceOrderND.noodleState = NoodleNameConstants.LUDAN;
        list.add(riceOrderND);

        RiceOrderND riceOrderND1 = new RiceOrderND();
        riceOrderND1.noodleNo = 18;
        riceOrderND1.noodleState = NoodleNameConstants.QINGTANG;
        list.add(riceOrderND1);

        mManyToManyNoodlesUtil.startReadDB();

    }

    int i=0;
    private void initRecycleNoodles(){
        if (recycleNoodlesUtil == null){
            recycleNoodlesUtil = new RecycleNoodlesUtil() {
                @Override
                protected void dealWithError(String info) {
                    makeToast(info);
                    Timber.e(info);
                }

                @Override
                protected void onFinish() {
                    makeToast("米粉回收完成");
                    i++;
                    Timber.e("米粉回收完成"+">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>第"+i+"碗");
                    LogUtils.file("米粉回收完成"+">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>第"+i+"碗");
                    //第一碗完成后继续执行第二碗的相应指令
                    mManyToManyNoodlesUtil.sendToDoor();
                }
            };
        }
    }


    /**
     * 事件响应方法
     * 接收消息
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ErrorEvent event) {
        showError(event.getErrorText());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        recycle();
    }

    private void recycle(){
        if (mManyToManyNoodlesUtil != null) {
            mManyToManyNoodlesUtil.recycle();
            mManyToManyNoodlesUtil = null;
        }
        if (recycleNoodlesUtil!=null){
            recycleNoodlesUtil.recycle();
            recycleNoodlesUtil = null;
        }
        if (mSeasoningPackageUtil!=null){
            mSeasoningPackageUtil.recycle();
            mSeasoningPackageUtil=null;
        }
    }

    @Override
    protected void countdownOver() {
        Timber.e("倒计时结束,不循环检测，直接回收");
        recycleNoodlesUtil.setDirectRecycle(true);
    }

    private void dropSeasoningPackage(RiceOrderND riceOrderND) {
        mSeasoningPackageUtil = new SeasoningPackageUtil() {
            @Override
            protected void dealWithError(String errorCode) {
                Timber.e("写数据失败");
                showError("写数据失败");
            }

            @Override
            protected void onFinish() {
//                Timber.e("写数据完成");
                mManyToManyNoodlesUtil.openDoor();
            }

            @Override
            protected void onChoiceNoodlesFinish() {

            }
        };
//        mSeasoningPackageUtil.startDropPackage(NoodleNameConstants.QINGTANG);
        mSeasoningPackageUtil.startDropPackage(riceOrderND);
    }
}
