package com.benxiang.noodles.test;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.benxiang.noodles.R;
import com.benxiang.noodles.base.BaseActivity;
import com.benxiang.noodles.contants.NoodleNameConstants;
import com.benxiang.noodles.data.noodle.NoodleEventData;
import com.benxiang.noodles.entrance.MakeNoodlesUtil;
import com.benxiang.noodles.entrance.RecycleNoodlesUtil;
import com.benxiang.noodles.entrance.SeasoningPackageUtil;
import com.benxiang.noodles.serialport.ComBean;
import com.benxiang.noodles.serialport.bean.ErrorEvent;
import com.benxiang.noodles.serialport.data.constant.NoodlesType;
import com.blankj.utilcode.util.LogUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

import static com.benxiang.noodles.serialport.cardmac.CardSerialOpenHelper.MAKE_NOODLES;
import static com.benxiang.noodles.serialport.cardmac.CardSerialOpenHelper.RECYCLE_NOODLES;
import static com.benxiang.noodles.serialport.cardmac.CardSerialOpenHelper.SEASONING_PACKAGE;


public class MakeMultiNoodlesActivity extends BaseActivity {

    private MakeNoodlesUtil mMakeNoodlesUtil;
    private RecycleNoodlesUtil mRecycleNoodlesUtil;
    SeasoningPackageUtil mSeasoningPackageUtil;
    private EditText etNoodles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getContentViewID() {
        return R.layout.activity_make_noodles;
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

    public void click(View view){
//        int i = 3;
//        i = i / 0;
        String trim = etNoodles.getText().toString().trim();
        if (trim.isEmpty()){
            return;
        }

        NoodlesType noodlesType;
        if (trim.toUpperCase().equals("A")){
            noodlesType = NoodlesType.TYPE_A;
        }else {
            noodlesType = trim.toUpperCase().equals("B")? NoodlesType.TYPE_B : NoodlesType.TYPE_C;
        }
        initReadCard(noodlesType);
    }

    //    private void initReadCard(int numberNoodles) {
    private void initReadCard(NoodlesType noodlesType) {
//        recycle();
        if (mMakeNoodlesUtil == null) {
            mMakeNoodlesUtil = new MakeNoodlesUtil() {

                @Override
                protected void dealWithError(String info) {
                    makeToast("错误码:"+info);
                    Timber.e("错误码:"+info);
                }

                @Override
                protected void onFinish(NoodleEventData noodleEventData) {
                    makeToast("制作"+noodleEventData.noodle_no+"米粉完成,请取粉,noodle_state为"+noodleEventData.noodle_state);
                    //开始倒计时，开始检测取料箱是否为空
                    Timber.e("制作"+noodleEventData.noodle_no+"米粉完成,请取粉,noodle_state为"+noodleEventData.noodle_state);
                    setEnableCountdown(true,5);
                    startDownTime();
                    initRecycleNoodles();
                    mRecycleNoodlesUtil.startCheckBox();
                }

                @Override
                protected void onAllFinish(NoodleEventData noodleEventData) {
                    makeToast("制作"+noodleEventData.noodle_no+"米粉完成,请取粉,noodle_state为"+noodleEventData.noodle_state);
                    //开始倒计时，开始检测取料箱是否为空
                    Timber.e("制作"+noodleEventData.noodle_no+"米粉完成,请取粉,noodle_state为"+noodleEventData.noodle_state);
                    setEnableCountdown(true,5);
                    startDownTime();
                    initRecycleNoodles();
                    mRecycleNoodlesUtil.startCheckBox();
                }


                @Override
                protected void tooMuchError() {
                    makeToast("多次检查皮带出错");
                    Timber.e("多次检查皮带出错");
                }

                @Override
                protected void dropPackage(NoodleEventData noodleEventData) {
                    dropSeasoningPackage(noodleEventData);
                }

                @Override
                protected void choiceNoodles(int noodlesNo) {
                    startChoiceNoodles(noodlesNo);
                }
            };
        }

        List<NoodleEventData> list = new ArrayList<NoodleEventData>();
        list.removeAll(null);
        NoodleEventData noodleEventData = new NoodleEventData();
        noodleEventData.noodle_no = 5;
        noodleEventData.noodle_state = NoodleNameConstants.HAOHUA;
        list.add(noodleEventData);

        NoodleEventData noodleEventData2 = new NoodleEventData();
        noodleEventData2.noodle_no = 13;
        noodleEventData2.noodle_state = NoodleNameConstants.SUANLA;
        list.add(noodleEventData2);


        mMakeNoodlesUtil.setNoodleEventDatas(list);
        mMakeNoodlesUtil.startCheckBelt();

    }

    public void click2(View view){
        int[] numberNoodlesArr =new int[]{8};
        mMakeNoodlesUtil.addNumberNoodles(numberNoodlesArr);
    }

    int i=0;
    private void initRecycleNoodles(){
//        recycle();
        if (mRecycleNoodlesUtil == null){
            mRecycleNoodlesUtil = new RecycleNoodlesUtil() {
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
                    mMakeNoodlesUtil.sendToDoor();
                }
            };
        }
    }

    //接收数据
    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onCardEvent(ComBean comRecData) {
        if (comRecData.makeOrRecycleNoodles == MAKE_NOODLES){
            mMakeNoodlesUtil.handleReceived(comRecData);
        }else if (comRecData.makeOrRecycleNoodles == RECYCLE_NOODLES){
//            initRecycleNoodles();
            mRecycleNoodlesUtil.handleReceived(comRecData);
        }else if (comRecData.makeOrRecycleNoodles == SEASONING_PACKAGE){
            mSeasoningPackageUtil.handleReceived(comRecData);
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
        if (mMakeNoodlesUtil != null) {
            mMakeNoodlesUtil.recycle();
            mMakeNoodlesUtil = null;
        }
        if (mRecycleNoodlesUtil !=null){
            mRecycleNoodlesUtil.recycle();
            mRecycleNoodlesUtil = null;
        }
        if (mSeasoningPackageUtil!=null){
            mSeasoningPackageUtil.recycle();
            mSeasoningPackageUtil=null;
        }
    }

    @Override
    protected void countdownOver() {
        Timber.e("倒计时结束,不循环检测，直接回收");
        mRecycleNoodlesUtil.setDirectRecycle(true);
    }

    private void dropSeasoningPackage(NoodleEventData noodleEventData) {
        mSeasoningPackageUtil = new SeasoningPackageUtil() {
            @Override
            protected void dealWithError(String errorCode) {
                Timber.e("写数据失败");
                showError("写数据失败");
            }

            @Override
            protected void onFinish() {
//                Timber.e("写数据完成");
                mMakeNoodlesUtil.openDoor();
            }

            @Override
            protected void onChoiceNoodlesFinish() {
                mMakeNoodlesUtil.setIsChoiceFinish(true);
            }
        };
        mSeasoningPackageUtil.startDropPackage(noodleEventData);
//        mSeasoningPackageUtil.startDropPackage(NoodleNameConstants.LUDAN);
    }

    private void startChoiceNoodles(int noodleNo) {
        mSeasoningPackageUtil = new SeasoningPackageUtil() {
            @Override
            protected void dealWithError(String errorCode) {
                Timber.e("写数据失败");
                showError("写数据失败");
            }

            @Override
            protected void onFinish() {
//                Timber.e("写数据完成");
                mMakeNoodlesUtil.openDoor();
            }

            @Override
            protected void onChoiceNoodlesFinish() {
                mMakeNoodlesUtil.setIsChoiceFinish(true);
            }
        };
        mSeasoningPackageUtil.startChoiceNoodles(noodleNo);
//        mSeasoningPackageUtil.startDropPackage(NoodleNameConstants.LUDAN);
    }
}
