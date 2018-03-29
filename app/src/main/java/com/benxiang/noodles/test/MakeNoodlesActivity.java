package com.benxiang.noodles.test;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.benxiang.noodles.R;
import com.benxiang.noodles.base.BaseActivity;
import com.benxiang.noodles.entrance.ReadReceiveCardUtil;
import com.benxiang.noodles.entrance.RecycleNoodlesUtil;

import org.greenrobot.eventbus.EventBus;

import timber.log.Timber;

public class MakeNoodlesActivity extends BaseActivity {

    private ReadReceiveCardUtil readReceiveCardUtil;
    private RecycleNoodlesUtil recycleNoodlesUtil;
    private EditText etNoodles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(getContentViewID());
//        afterContentViewSet();
    }

    @Override
    public int getContentViewID() {
        return R.layout.activity_make_noodles;
    }

//    @Override
    protected void afterContentViewSet() {
        etNoodles = (EditText) findViewById(R.id.et_noodles);
//        initCommandView();
        setEnableCountdown(false,10);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    public void click(View view){

//        int i = 3;
//        i = i / 0;

        makeToast("点击事件");
        String trim = etNoodles.getText().toString().trim();
        if (trim.isEmpty()){
            return;
        }
        int numberNoodles = Integer.parseInt(trim);
        initReadCard(numberNoodles);
//        NoodlesType noodlesType;
//        if (trim.toUpperCase().equals("A")){
//            noodlesType = NoodlesType.TYPE_A;
//        }else {
//            noodlesType = trim.toUpperCase().equals("B")? NoodlesType.TYPE_B: NoodlesType.TYPE_C;
//        }
//        initReadCard(noodlesType);
    }

    private void initReadCard(int numberNoodles) {
//        private void initReadCard(NoodlesType noodlesType) {
        if (readReceiveCardUtil == null) {
            readReceiveCardUtil = new ReadReceiveCardUtil() {

                @Override
                protected void dealWithError(String info) {
                    makeToast("错误码:"+info);
                    Timber.e("错误码:"+info);
                }

                @Override
                protected void onFinish(int numberNoodles) {
                    makeToast("制作米粉完成,请取粉");
                    //开始倒计时，开始检测取料箱是否为空
                    Timber.e("制作"+numberNoodles+"号米粉完成,请取粉");
                    setEnableCountdown(true,10);
                    startDownTime();
//                    startCountdownTask();
                    initRecycleNoodles();
                    recycleNoodlesUtil.startCheckBox();
                }

                @Override
                protected void tooMuchError() {
                    makeToast("多次检查皮带出错");
                    Timber.e("多次检查皮带出错");
                }
            };
        }
        readReceiveCardUtil.setNumberNoodles(numberNoodles);
//        ArrayList<NoodlesType> list = new ArrayList<NoodlesType>();
//        list.add(noodlesType);
//        readReceiveCardUtil.setNumberNoodles(list);
        readReceiveCardUtil.startCheckBelt();
    }

    private void initRecycleNoodles(){
//        recycle();
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
                    Timber.e("米粉回收完成");
                }

            };
        }
    }

//    //接收数据
//    @Subscribe(threadMode = ThreadMode.POSTING)
//    public void onCardEvent(ComBean comRecData) {
////        Timber.e("makeOrRecycleNoodles的值:"+comRecData.makeOrRecycleNoodles);
//        if (comRecData.makeOrRecycleNoodles == MAKE_NOODLES){
//            readReceiveCardUtil.handleReceived(comRecData);
//        }else{
////            initRecycleNoodles();
//            recycleNoodlesUtil.handleReceived(comRecData);
//        }
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        recycle();
    }

    private void recycle(){
        if (readReceiveCardUtil != null) {
            readReceiveCardUtil.recycle();
            readReceiveCardUtil = null;
        }
        if (recycleNoodlesUtil!=null){
            recycleNoodlesUtil.recycle();
            recycleNoodlesUtil = null;
        }
    }

    @Override
    protected void countdownOver() {
        recycleNoodlesUtil.setDirectRecycle(true);
    }
}
