package com.benxiang.noodles.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.benxiang.noodles.AppApplication;
import com.benxiang.noodles.moudle.config.SplashActivity;

import timber.log.Timber;

/**
 * Created by 刘圣如 on 2017/9/30.
 */

public class BootBroadcast extends BroadcastReceiver{

//    @Override
//    public void onReceive(Context context, Intent intent) {
//        startAct(context,intent);
//    }
//
//    private Intent mIntent;
//    private void startAct(final Context context,Intent intent) {
//        mIntent = new Intent(context, SplashActivity.class);
//        mIntent.setFlags(Intent.FLAG_RECEIVER_FOREGROUND);
//        AppApplication.getHandler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                context.startActivity(mIntent);
//            }
//        },20*1000);
//    }


        static final String ACTION = "android.intent.action.BOOT_COMPLETED";
        @Override
        public void onReceive(Context context, Intent intent) {
            Timber.e("接收到的广播:"+intent.getAction());
            if (intent.getAction().equals(ACTION)) {
                try {
                    Thread.sleep(30 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Intent activityIntent = new Intent(context, SplashActivity.class);
                activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(activityIntent);
            }
        }



}
