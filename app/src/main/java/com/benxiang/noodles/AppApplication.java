package com.benxiang.noodles;

import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.os.Handler;

import com.benxiang.noodles.data.DBFactory;
import com.benxiang.noodles.utils.LogcatHelper;
import com.benxiang.noodles.utils.SpUtils;
import com.benxiang.noodles.utils.TTSHelper;
import com.blankj.utilcode.util.CrashUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.Utils;
import com.litesuits.orm.db.DataBaseConfig;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.umeng.analytics.MobclickAgent;

import timber.log.Timber;

/**
 * Created by 刘圣如 on 2017/8/24.
 */

public class AppApplication extends Application {
    private static Context mContext;
    RefWatcher mRefWatcher;
    private static int sortNo = 0;
    @Override
    public void onCreate() {
        super.onCreate();
        mContext=this;
        SpUtils.init(this, "dbLuckyDraw");
        initBlankUtil();
        initTimber();
        initUmeng();
        initDB();
        initTTS();
        if (BuildConfig.DEBUG) {
            initLeak();
        }
    }

    private void initSaveLog() {
        LogcatHelper.getInstance(this).start();
    }

    private void initBlankUtil() {
        Utils.init(this);
        //打印log到文件
        LogUtils.getConfig().setDir(Environment.getExternalStorageDirectory().getAbsoluteFile()+"/Log");
        CrashUtils.init();
    }

    private void initLeak() {
        mRefWatcher = LeakCanary.install(this);
    }

    private void initTTS() {
        TTSHelper.init(this,TTSHelper.TTS_ENGINE_IFLYTEK);
    }

    private void initDB() {

        String sdCardPath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/";
        DataBaseConfig dataBaseConfig = new DataBaseConfig(this, sdCardPath+"noodles.db");


        dataBaseConfig.debugged=true;
        dataBaseConfig.dbVersion=1;
        DBFactory.initNoodle(dataBaseConfig);
    }

    public static Context getAppContext(){
        return mContext;
    }
    private void initTimber(){
            Timber.plant(new Timber.DebugTree());
    }
    private void initUmeng() {
        MobclickAgent.setCatchUncaughtExceptions(true);
    }

    public static int getSortNo() {
        return sortNo;
    }

    public static void setSortNo(int sortNo) {
        AppApplication.sortNo = sortNo;
    }


    private static Handler mHandler;

    public static Handler getHandler(){
        if (mHandler == null){
            mHandler = new Handler();
        }
        return mHandler;
    }

}
