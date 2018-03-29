package com.benxiang.noodles.serialport.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.benxiang.noodles.BuildConfig;
import com.benxiang.noodles.contants.Constants;
import com.benxiang.noodles.data.table.RiceOrderND;
import com.benxiang.noodles.entrance.ManyToManyNoodlesUtil;
import com.benxiang.noodles.entrance.RecycleNoodlesUtil;
import com.benxiang.noodles.entrance.SeasoningPackageUtil;
import com.benxiang.noodles.serialport.ComBean;
import com.benxiang.noodles.serialport.bean.MakeNoodlesEvent;
import com.benxiang.noodles.utils.RxCountDown;
import com.benxiang.noodles.utils.TTSHelper;
import com.blankj.utilcode.util.LogUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import timber.log.Timber;

import static com.benxiang.noodles.serialport.cardmac.CardSerialOpenHelper.MAKE_NOODLES;
import static com.benxiang.noodles.serialport.cardmac.CardSerialOpenHelper.RECYCLE_NOODLES;
import static com.benxiang.noodles.serialport.cardmac.CardSerialOpenHelper.SEASONING_PACKAGE;

public class MakeNoodlesService extends Service {

    private ManyToManyNoodlesUtil mManyToManyNoodlesUtil;
    private RecycleNoodlesUtil recycleNoodlesUtil;
    private SeasoningPackageUtil mSeasoningPackageUtil;

    public MakeNoodlesService() {}

    @Override
    public IBinder onBind(Intent intent) {
        Timber.e(">>>>>>onBind");
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Timber.e(">>>>>>onStartCommand");
        recycle();
        initRecycleNoodles();
        initSeasoningPackage();
        initMakeNoodle();
        return START_NOT_STICKY;
//        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);
//        startDownLoad();
    }

    @Override
    public void onDestroy() {
//        Timber.e("开始执行onDestroy方法");
        EventBus.getDefault().unregister(this);
        recycle();
        unDisposable();
        TTSHelper.stop();
//        Timber.e("服务终止");
    }

    private void initMakeNoodle() {
        if (mManyToManyNoodlesUtil == null) {
                mManyToManyNoodlesUtil = new ManyToManyNoodlesUtil() {
                @Override
                protected void dealWithError(String info) {
                    Timber.e("错误码:" + info);
                    MakeNoodlesEvent makeNoodlesEvent = new MakeNoodlesEvent(true,info);
                    EventBus.getDefault().post(makeNoodlesEvent);
                    stopSelf();
                }

                @Override
                protected void onFinish(RiceOrderND riceOrderND) {
                    MakeNoodlesEvent makeNoodlesEvent = new MakeNoodlesEvent(false,riceOrderND);
                    TTSHelper.speak(riceOrderND.sortNo + "号" + riceOrderND.sortNo + "号" +
                            riceOrderND.sortNo + "号" + riceOrderND.noodleName +
                            "制作完成,请在一分钟内从取餐口取走");
                    EventBus.getDefault().post(makeNoodlesEvent);
                    //开始倒计时，开始检测取料箱是否为空
                    makinngNoodleDownTime();
//                    initRecycleNoodles();
                    recycleNoodlesUtil.startCheckBox();
                }

                @Override
                protected void onAllFinish(RiceOrderND riceOrderND) {
                    MakeNoodlesEvent makeNoodlesEvent = new MakeNoodlesEvent(true,riceOrderND);
                    TTSHelper.speak(riceOrderND.sortNo + "号" + riceOrderND.sortNo + "号" +
                            riceOrderND.sortNo + "号" + riceOrderND.noodleName +
                            "制作完成,请在一分钟内从取餐口取走");
                    EventBus.getDefault().post(makeNoodlesEvent);
                    //开始倒计时，开始检测取料箱是否为空
                    makinngNoodleDownTime();
//                    initRecycleNoodles();
                    recycleNoodlesUtil.startCheckBox();
                }

                @Override
                protected void dropPackage(RiceOrderND riceOrderND) {
                    mSeasoningPackageUtil.startDropPackage(riceOrderND);
                }

                @Override
                protected void choiceNoodles(int noodlesNo) {
                    mSeasoningPackageUtil.startChoiceNoodles(noodlesNo);
                }

                protected void startOpenSteam() {
                    mSeasoningPackageUtil.startOpenSteam();
                }
            };
        }
        mManyToManyNoodlesUtil.startReadDB();
    }

    //接收数据
    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onCardEvent(ComBean comRecData) {
//        Timber.e("makeOrRecycleNoodles的值:"+comRecData.makeOrRecycleNoodles);
        if (comRecData.makeOrRecycleNoodles == MAKE_NOODLES){
            mManyToManyNoodlesUtil.handleReceived(comRecData);
        }else if (comRecData.makeOrRecycleNoodles == RECYCLE_NOODLES){
//            initRecycleNoodles();
            recycleNoodlesUtil.handleReceived(comRecData);
        }else if (comRecData.makeOrRecycleNoodles == SEASONING_PACKAGE){
            mSeasoningPackageUtil.handleReceived(comRecData);
        }
    }

    private void recycle(){
//        Timber.e("资源未回收");
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
//        Timber.e("资源已回收");
    }

    int i=0;
    private void initRecycleNoodles(){
//        recycle();
        if (recycleNoodlesUtil == null){
            recycleNoodlesUtil = new RecycleNoodlesUtil() {
                @Override
                protected void dealWithError(String info) {
                    MakeNoodlesEvent makeNoodlesEvent = new MakeNoodlesEvent(true,info);
                    EventBus.getDefault().post(makeNoodlesEvent);
                    Timber.e(info);
                    stopSelf();
                }

                @Override
                protected void onFinish() {
                    i++;
                    Timber.e("米粉回收完成"+">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>哈哈>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>第"+i+"碗");
                    LogUtils.file("米粉回收完成"+">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>第"+i+"碗");
                    //第一碗完成后继续执行第二碗的相应指令
                    unDisposable();
                    //TODO  LIN  执行判断继续找下一碗
                    mManyToManyNoodlesUtil.sendToDoor();
                }
            };
        }
    }

    private void initSeasoningPackage() {
        mSeasoningPackageUtil = new SeasoningPackageUtil() {
            @Override
            protected void dealWithError(String errorCode) {
                MakeNoodlesEvent makeNoodlesEvent = new MakeNoodlesEvent(true,errorCode);
                EventBus.getDefault().post(makeNoodlesEvent);
                Timber.e("写数据失败");
                stopSelf();
            }

            @Override
            protected void onFinish() {
//                Timber.e("写数据完成");
                mManyToManyNoodlesUtil.openDoor();
            }

            @Override
            protected void onChoiceNoodlesFinish() {
                mManyToManyNoodlesUtil.setIsChoiceFinish(true);
            }

            @Override
            protected void onOpenSteamFinish() {
                mManyToManyNoodlesUtil.startCheckBelt();
            }
        };
    }

    private void makinngNoodleDownTime() {
        if (BuildConfig.DEBUG) {
            startDownTime(20);
        } else {
            startDownTime(Constants.COUNT_DOWN_WAIT_TIME);
        }
    }

    //Rx相关
    public CompositeDisposable compositeDisposables;
    public void startDownTime(int countdownTime) {
        unDisposable();
        //被观察者发送格式化后的时间
        Disposable disposable = RxCountDown.countdown(countdownTime)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(@NonNull Integer s) throws Exception {

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        countdownOver();
                    }
                });
        addSubscribe(disposable);
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

    protected void countdownOver() {
        Timber.e("倒计时结束,不循环检测，直接回收");
        if (recycleNoodlesUtil!=null){
            recycleNoodlesUtil.setDirectRecycle(true);
        }
    }

}
