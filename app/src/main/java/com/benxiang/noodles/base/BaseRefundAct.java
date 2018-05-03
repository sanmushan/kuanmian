package com.benxiang.noodles.base;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.benxiang.noodles.R;
import com.benxiang.noodles.contants.Constants;
import com.benxiang.noodles.data.table.RiceOrderND;
import com.benxiang.noodles.model.NoodleTradeModel;
import com.benxiang.noodles.moudle.banner.BannerActivity;
import com.benxiang.noodles.moudle.config.SplashActivity;
import com.benxiang.noodles.serialport.data.sp.FormulaPreferenceConfig;
import com.benxiang.noodles.utils.PreferenceUtil;
import com.benxiang.noodles.utils.RxCountDown;
import com.benxiang.noodles.utils.TTSHelper;
import com.benxiang.noodles.widget.ErrorDialogFragment;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.ButterKnife;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import timber.log.Timber;

/**
 * Created by ZeQiang Fang on 2017/8/28.
 */

public abstract class BaseRefundAct extends AppCompatActivity {
    private final String TAG = getClass().getSimpleName();
    private int countdownTime = Constants.COUNT_DOWN_TIME;
    private TextView tvCountDown;

    private TextView tv_noodle_news;
    public static final String ERROR_FRAGMENT_DIALOG = "ERROR_FRAGMENT_DIALOG";
    private boolean isCountDownOnClick = true;
    protected RiceOrderND mRiceOrderND;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        beforeContentViewSet();
        setContentView(getContentViewID());
        ButterKnife.bind(this);
        initCommonView();
        registerMainHandler();
        afterContentViewSet();
        initerlog("onCreace");
        mRiceOrderND = getIntent().getParcelableExtra("riceOrderND");
//        EventBus.getDefault().register(this);
    }

    //初始化共有控件
    private void initCommonView() {
        tvCountDown = (TextView) findViewById(R.id.count_down);
        if (tvCountDown != null && isCountDownOnClick) {
            tvCountDown.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                     jumpToNextPager();

                }
            });

        }
        tv_noodle_news = (TextView) findViewById(R.id.tv_noodle_news_title);
    }

    protected void jumpToNextPager(){
        Timber.e("有执行到");
        unDisposable();
        //lin
        ReturnBanner();
    }

    private void ReturnBanner() {
        Intent intent = new Intent(this, BannerActivity.class);
//        Intent intent = new Intent(this, VideoActivity.class);
        startActivity(intent);
    }

    protected void SetCountDownOnClick(boolean istrue) {
        isCountDownOnClick = istrue;
    }

    //隐藏导航栏
    protected void hideNavigationBar() {
        // Hide both the navigation bar and the status bar.


        View decorView = getWindow().getDecorView();
        // SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
        // a general rule, you should design your app to hide the status bar whenever you
        // hide the navigation bar.
        int uiOptions = View.SYSTEM_UI_FLAG_LOW_PROFILE   //状态栏显示处于低能显示状态(low profile模式)，状态栏上一些图标显示会被隐藏
                | View.SYSTEM_UI_FLAG_FULLSCREEN              //activity全屏显示
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE           //标志来帮助你的应用维持一个稳定的布局，也是透明状态栏，让应用的主体内容占用系统状态栏的空间
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION   //效果同View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;        //隐藏虚拟按键
        decorView.setSystemUiVisibility(uiOptions);
    }

    //一开始设置屏幕是横屏
    protected void beforeContentViewSet() {
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    //设置布局
    public abstract int getContentViewID();

    protected abstract void afterContentViewSet();

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
//        startCountDownTask();
        startDownTime();
        hideNavigationBar();
        initerlog("onStart");
//        stopTTS();
        if (FormulaPreferenceConfig.isDisplay()){
            if (mRiceOrderND!=null){
                showTitle(mRiceOrderND);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        MobclickAgent.onResume(this);
        initerlog("onResume");
    }

    @Override
    protected void onStop() {
        super.onStop();
//        cancelCountdownTask();
        unDisposable();
        initerlog("onStop");
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        initerlog("onPause");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        killLoading();
        killAllErrorDialogs();
        unregisterMainHandler();
        initerlog("onDestroy");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    // 主线程 handler 相关

    private Handler mainHandler;

    public Handler getMainHandler() {
        return mainHandler;
    }

    protected void registerMainHandler() {
        if (mainHandler == null) {
            mainHandler = new Handler(getMainLooper()) {
                @Override
                public void handleMessage(Message msg) {
                    onHandleMessage(msg);
                }
            };
        }
    }


    private void unregisterMainHandler() {
        if (mainHandler != null) {
            mainHandler.removeCallbacksAndMessages(null);
        }
    }

    protected void onHandleMessage(Message msg) {
    }

    //倒计时相关
    private AsyncTask<Integer, Integer, Void> countDownTask;
    private boolean isEnableCountdown = false;

    @SuppressLint("StaticFieldLeak")
    private void startCountDownTask() {
        if (isEnableCountdown) {
            cancelCountdownTask();
            countDownTask = new AsyncTask<Integer, Integer, Void>() {
                @Override
                protected Void doInBackground(Integer... params) {
                    int countDown = params[0];
                    publishProgress(countDown);
                    while (countDown > 0 && !isCancelled()) {
                        try {
                            Thread.sleep(1000);
                            countDown--;
                            publishProgress(countDown);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    return null;
                }

                @Override
                protected void onProgressUpdate(Integer... values) {
                    super.onProgressUpdate(values);
                    if (tvCountDown != null) {
                        tvCountDown.setText(getString(R.string.count_down, values[0]));
                    }
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
                    countdownOver();
                }
            }.execute(countdownTime);
//                    .executeOnExecutor(Executors.newCachedThreadPool(),countdownTime);
        }
    }

    //设置是否需要倒计时
    protected void setEnableCountdown(boolean enableCountdown) {
        setEnableCountdown(enableCountdown, Constants.COUNT_DOWN_TIME);
    }

    protected void setEnableCountdown(final boolean enableCountdown, int countTime) {
        isEnableCountdown = enableCountdown;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setTvCountdownVisible(enableCountdown);
            }
        });
        countdownTime = countTime;
    }

    protected void setTvCountdownVisible(boolean visible) {
        if (tvCountDown != null) {
            tvCountDown.setVisibility(visible ? View.VISIBLE : View.GONE);
        }
    }
    protected void setTvTitleVisible(boolean visible) {
        if (tv_noodle_news != null) {
            tv_noodle_news.setVisibility(visible ? View.VISIBLE : View.GONE);
        }
    }

    protected void countdownOver() {
        finish();
    }

    private void cancelCountdownTask() {
        if (countDownTask != null) {
            countDownTask.cancel(true);
            countDownTask = null;
        }
        if (tvCountDown != null) {
            tvCountDown.setText("");
        }
    }

    //Rx相关
    public CompositeDisposable compositeDisposables;

    public void startDownTime() {
        unDisposable();
        //被观察者发送格式化后的时间
        if (isEnableCountdown) {
            Disposable disposable = RxCountDown.countdown(countdownTime)
                    .subscribe(new Consumer<Integer>() {
                        @Override
                        public void accept(@NonNull Integer s) throws Exception {
                            if (tvCountDown != null) {
                                tvCountDown.setText(getString(R.string.count_down, s));
                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(@NonNull Throwable throwable) throws Exception {
                            Log.e(TAG, "accept: " + throwable);
                        }
                    }, new Action() {
                        @Override
                        public void run() throws Exception {
//                            finish();
                            countdownOver();
                        }
                    });
            addSubscribe(disposable);
        }
    }

    protected void addSubscribe(Disposable disposable) {
        if (compositeDisposables == null) {
            compositeDisposables = new CompositeDisposable();
        }
        compositeDisposables.add(disposable);
    }

    protected void unDisposable() {
        if (compositeDisposables != null) {
            compositeDisposables.clear();
            compositeDisposables = null;
        }
    }


    public void showError(String error) {
        showCommonErrorDialog(getString(R.string.confirm), error,
                new ErrorDialogFragment.OnErrorClickListener() {
                    @Override
                    public void onClick(ErrorDialogFragment dialog) {
                        dialog.dismiss();
                    }
                });
    }
    public void showDialog(String str) {
        showWarningDialog(getString(R.string.confirm), str,
                new ErrorDialogFragment.OnErrorClickListener() {
                    @Override
                    public void onClick(ErrorDialogFragment dialog) {
                        dialog.dismiss();
                    }
                });
    }
    private ArrayList<ErrorDialogFragment> errorDialogs;

    public void showCommonErrorDialog(String buttonText, String exception,
                                      ErrorDialogFragment.OnErrorClickListener listener) {
        showCommonDialog(getString(R.string.desc_exception_tip), buttonText, exception,false,false, listener);
    }

    public void showWarningDialog(String buttonText, String warning,
                                  ErrorDialogFragment.OnErrorClickListener listener) {
        showCommonDialog(getString(R.string.desc_friend_tip), buttonText, warning,false,false, listener);
    }

    public void showWarningDialog(String buttonText, String warning) {
        showCommonDialog(getString(R.string.desc_friend_tip), buttonText, warning, false,false,new ErrorDialogFragment.OnErrorClickListener() {
            @Override
            public void onClick(ErrorDialogFragment dialog) {
                dialog.dismiss();
            }
        });
    }

   public void showWarningDialog(String buttonText, String warning,boolean isSpanned,boolean iscancel) {
        showCommonDialog(getString(R.string.desc_friend_tip), buttonText, warning,isSpanned, iscancel,new ErrorDialogFragment.OnErrorClickListener() {
            @Override
            public void onClick(ErrorDialogFragment dialog) {
                dialog.dismiss();
            }
        });
    }

    public void showWarningDialog(String buttonText, String warning,boolean isSpanned,boolean iscancel,ErrorDialogFragment.OnErrorClickListener listener) {
        showCommonDialog(getString(R.string.desc_friend_tip), buttonText, warning,isSpanned, iscancel,listener);
    }

    public void showCommonDialog(String title, String buttonText, String exception,
                                 boolean isSpanned,boolean iscancel,ErrorDialogFragment.OnErrorClickListener listener) {
        ErrorDialogFragment errorDialogFragment =
                ErrorDialogFragment.newInstance(title, buttonText, exception,isSpanned,iscancel);
//    errorDialogFragment.showAllowingStateLoss(getFragmentManager(), ERROR_FRAGMENT_DIALOG);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(errorDialogFragment, ERROR_FRAGMENT_DIALOG);
        ft.commitAllowingStateLoss();
        errorDialogFragment.setOnErrorClickListener(listener);
        if (errorDialogs == null) {
            errorDialogs = new ArrayList<>();
        }
        errorDialogs.add(errorDialogFragment);
    }

    protected void killAllErrorDialogs() {
        try {
            if (errorDialogs != null) {
                for (ErrorDialogFragment errorDialog : errorDialogs) {
                    errorDialog.setOnErrorClickListener(null);
                    if (!errorDialog.isHidden()) {
                        errorDialog.dismiss();
                    }
                }
            }
        } catch (Exception e) {
            Timber.e(TAG + "  exception>>>>>>>>>" + e.toString());
        }

    }

    // 加载 dialog 相关

    private AlertDialog loadingDialog;

    public void showLoadingDialog() {
        if (loadingDialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.LoadingDialog);
            View rootView = getLayoutInflater().inflate(R.layout.dialog_loading, null);
            builder.setView(rootView);
            loadingDialog = builder.create();
            loadingDialog.setCanceledOnTouchOutside(false);
            Window window = loadingDialog.getWindow();
            if (window != null) {
                window.setBackgroundDrawable(null);
            }
        }
        if (!loadingDialog.isShowing()) {
            loadingDialog.show();
        }
    }

    public void hideLoadingDialog() {
        if (loadingDialog != null) {
            if (loadingDialog.isShowing()) {
                loadingDialog.dismiss();
            }
        }
    }

    public void killLoading() {
        if (loadingDialog != null) {
            if (loadingDialog.isShowing()) {
                loadingDialog.dismiss();
                loadingDialog = null;
            }
        }
    }

    //语音相关
    public void speakText(final String text) {
        TTSHelper.speak(text);
    }

    private boolean isHaveSpeak = false;

    public void speakOpening(String text) {
        if (!isHaveSpeak) {
            TTSHelper.speak(text);
            isHaveSpeak = true;
        }
    }

    private void stopTTS() {
        TTSHelper.stop();
    }

    protected void StartBanner() {
        if (!TextUtils.isEmpty(PreferenceUtil.config().getMacNo(Constants.MAC_NO))){
            Intent intent = new Intent(this, BannerActivity.class);
//            Intent intent = new Intent(this, VideoActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable("riceOrderND",mRiceOrderND);
            intent.putExtras(bundle);
            startActivity(intent);
        }else{
            Intent intent = new Intent(this, SplashActivity.class);
            startActivity(intent);
        }
        /*Intent intent = new Intent(this, SplashActivity.class);
//        Intent intent = new Intent(this, BannerActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("riceOrderND",mRiceOrderND);
        intent.putExtras(bundle);
        startActivity(intent);*/
    }

    protected void startNext(Context context, Class<?> cls) {
        Intent intent = new Intent(context, cls);
        Bundle bundle = new Bundle();
        bundle.putParcelable("riceOrderND",mRiceOrderND);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    protected void startNext(Context context, Class<?> cls, NoodleTradeModel mNoodleTradeModel) {
        Intent intent = new Intent(context, cls);
        Bundle bundle = new Bundle();
        bundle.putParcelable("riceOrderND",mRiceOrderND);
        bundle.putParcelable("noodle", mNoodleTradeModel);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void startRefundment(Context context, Class<?> cls, NoodleTradeModel mNoodleTradeModel, int payType) {
        Intent intent = new Intent(context, cls);
        Bundle bundle = new Bundle();
        bundle.putParcelable("noodle", mNoodleTradeModel);
        bundle.putParcelable("riceOrderND",mRiceOrderND);
        intent.putExtras(bundle);
        intent.putExtra("payType", payType);
        startActivity(intent);
    }

    private void initerlog(String log) {
        Timber.e(TAG + ">>>>>>>>>>" + log);
    }

    protected void makeToast(final String str) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(BaseRefundAct.this, str, Toast.LENGTH_LONG).show();
            }
        });
    }

    protected void killDialogTime() {
    }

    protected void showTitle(final RiceOrderND riceOrderND){
        mRiceOrderND=riceOrderND;
        getMainHandler().post(new Runnable() {
            @Override
            public void run() {
                setTitleNewsShow(riceOrderND);
            }
        });
    }

    protected void setTitleNewsShow(RiceOrderND riceOrderND){
        Log.e(TAG, "setTitleNewsShow: 做面提示" );
        if (tv_noodle_news != null) {
            tv_noodle_news.setText(riceOrderND.sortNo+"号"+riceOrderND.sortNo+"号"+riceOrderND.sortNo+"号的"+riceOrderND.noodleName+"已制作完成");
//            tv_noodle_news.setForegroundGravity();
            tv_noodle_news.setVisibility(View.VISIBLE);
            FormulaPreferenceConfig.setDisplay(true);
            getMainHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    FormulaPreferenceConfig.setDisplay(false);
                    tv_noodle_news.setVisibility(View.GONE);
                }
            }, 8*1000);
//            speakText(riceOrderND.sortNo + "号" + riceOrderND.sortNo + "号" +
//                    riceOrderND.sortNo + "号" + riceOrderND.noodleName +
//                    "制作完成,请在一分钟内从取餐口取走");
        }
    }

}
