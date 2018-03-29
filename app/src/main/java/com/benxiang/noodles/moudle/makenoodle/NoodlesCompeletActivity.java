package com.benxiang.noodles.moudle.makenoodle;

import android.content.Intent;
import android.os.CountDownTimer;
import android.widget.TextView;

import com.benxiang.noodles.MainActivity;
import com.benxiang.noodles.R;
import com.benxiang.noodles.base.BaseActivity;
import com.benxiang.noodles.contants.Constants;
import com.benxiang.noodles.entrance.RecycleNoodlesUtil;
import com.benxiang.noodles.model.ListModle;
import com.benxiang.noodles.serialport.ComBean;
import com.benxiang.noodles.serialport.bean.ErrorEvent;
import com.benxiang.noodles.utils.ShotrNoUtil;
import com.benxiang.noodles.widget.ErrorDialogFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import timber.log.Timber;

/**
 * Created by 刘圣如 on 2017/9/5.
 * 已经废弃，不再使用
 */

public class NoodlesCompeletActivity extends BaseActivity {
    @BindView(R.id.tv_count_time)
    TextView countTime;
    @BindView(R.id.tv_make_nums)
    TextView takeNum;
    private MyCountDownTimer timer;
    private ListModle listModle;

    private RecycleNoodlesUtil mRecycleNoodlesUtil;

    @Override
    public int getContentViewID() {

        return R.layout.activity_goods_complete;
    }

    @Override
    protected void afterContentViewSet() {
        initData();
        takeNum.setText(getString(R.string.complete_num, ShotrNoUtil.getShotrNo(true)));
        registerMainHandler();

    }

    //接收数据
    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onCardEvent(ComBean comRecData) {
        mRecycleNoodlesUtil.handleReceived(comRecData);
    }
    /**
     * 事件响应方法
     * 接收消息
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ErrorEvent event) {
        showError(event.getErrorText());
    }

    private void initData() {
//        NoodleTradeModel noodle = getIntent().getParcelableExtra("listModle");
        listModle = getIntent().getParcelableExtra("listModle");
        Timber.e("订单号"+listModle.goods_no);
        if (listModle == null) {
            return;
        }

//        listModle = noodle.listModles.get(0);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        initRecycleNoodles();
        startTimer();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cannelTimer();
        recycle();
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    private void recycle() {

        if (mRecycleNoodlesUtil != null) {
            mRecycleNoodlesUtil.recycle();
            mRecycleNoodlesUtil = null;
        }
    }

    private void initRecycleNoodles() {
        if (mRecycleNoodlesUtil == null) {
            mRecycleNoodlesUtil = new RecycleNoodlesUtil() {
                @Override
                protected void dealWithError(String info) {
                    showCommonErrorDialog("确定", "设备异常,请联系88888888", new ErrorDialogFragment.OnErrorClickListener() {
                        @Override
                        public void onClick(ErrorDialogFragment dialog) {
                            startMainActivity();
                        }
                    });
                }

                @Override
                protected void onFinish() {
                    getMainHandler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startMainActivity();
                        }
                    }, 1000);

                }
            };
        }
        mRecycleNoodlesUtil.startCheckBox();
    }

    private void startMainActivity() {
        Intent it = new Intent(NoodlesCompeletActivity.this, MainActivity.class);
        startActivity(it);
    }

  /*  public int getShotrNo() {
        int num=AppApplication.getSortNo()+1;
        if (num > 99) {
            AppApplication.setSortNo(1);
        }
        else {
            AppApplication.setSortNo(num);
        }

        return AppApplication.getSortNo();
    }*/

    //倒计时相关
    private class MyCountDownTimer extends CountDownTimer {

        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            countTime.setText(millisUntilFinished / 1000 + "'");
        }

        @Override
        public void onFinish() {

            mRecycleNoodlesUtil.setDirectRecycle(true);
//            initRecycleNoodles();

            initRecycleNoodles();

//            initRecycleNoodles();

//            startMainActivity();
        }
    }

    private void startTimer() {
        if (timer == null) {
            timer = new MyCountDownTimer(Constants.Wait_COUNT_DOWN_TIME, 1000);
        }
        timer.start();
    }

    private void cannelTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }
}
