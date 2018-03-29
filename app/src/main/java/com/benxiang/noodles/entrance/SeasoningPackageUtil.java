package com.benxiang.noodles.entrance;

import android.util.Log;

import com.benxiang.noodles.AppApplication;
import com.benxiang.noodles.data.noodle.NoodleEventData;
import com.benxiang.noodles.data.table.RiceOrderND;
import com.benxiang.noodles.serialport.ComBean;
import com.benxiang.noodles.serialport.SerialHelper;
import com.benxiang.noodles.serialport.cardmac.CardSerialOpenHelper;
import com.benxiang.noodles.serialport.cardmac.CardSerialOpenHelper1;
import com.benxiang.noodles.serialport.cardmac.NoodlesSerialCommand;
import com.benxiang.noodles.serialport.cardmac.SeasoningPackageReceiver;
import com.benxiang.noodles.utils.CalNoodlesNum;
import com.blankj.utilcode.util.LogUtils;

import java.util.ArrayList;

import timber.log.Timber;

import static com.benxiang.noodles.serialport.cardmac.CardSerialOpenHelper.BOTTLED_WATER;
import static com.benxiang.noodles.serialport.cardmac.CardSerialOpenHelper.SEASONING_PACKAGE;

/**
 * Created by Zeqiang Fang on 2017/9/1.
 */
public abstract class SeasoningPackageUtil {

    private SeasoningPackageReceiver mSeasoningPackageReceiver;
    //模组2
    private SerialHelper mCardSerialHelper;
    //模组1
    private SerialHelper mCardSerialHelper1;
    private SeasoningPackageReceiver.OnSuccessListener mFormulaSuccessListener;
    private SeasoningPackageReceiver.OnFailureListener mFormulaFailureListener;
    private int errorCount = 0;

    private ArrayList<byte[]> choiceChannel;
    int mNoodlesNO;

    protected SeasoningPackageUtil() {
        mCardSerialHelper = CardSerialOpenHelper.getIns().getCardSerialHelper();
        CardSerialOpenHelper.getIns().setTimeoutListener(new CardSerialOpenHelper.TimeoutListener() {
            @Override
            public void onTimeout() {
                dealWithError(202+"");
            }
        });

        mCardSerialHelper1 = CardSerialOpenHelper1.getIns().getCardSerialHelper();
        CardSerialOpenHelper1.getIns().setTimeoutListener(new CardSerialOpenHelper1.TimeoutListener() {
            @Override
            public void onTimeout() {
                dealWithError(202+"");
            }
        });
        mSeasoningPackageReceiver = SeasoningPackageReceiver.getIns();
        handleSuccess();
        handleFailure();
    }

    //处理第二代机蒸汽问题
    public void startInBottledWater(){
        logStep("开始打开蒸汽");
        mSeasoningPackageReceiver.setExecStep(5);
        count+=5;
        sendSerialDataBottledWater(NoodlesSerialCommand.BOTTLED_WATER);
    }

    //处理第二代机蒸汽问题
    public void startOpenSteam(){
        logStep("开始打开蒸汽");
        mSeasoningPackageReceiver.setExecStep(4);
        count+=4;
        sendSerialData(NoodlesSerialCommand.OPEN_STEAM);
    }

    //处理工位问题，选择米粉的同时会加热和加汤料
    public void startChoiceNoodles(int noodlesNO){
        mNoodlesNO = noodlesNO;
        logStep("开始选择米粉");
        mSeasoningPackageReceiver.setExecStep(3);
        count+=3;
        sendSerialData1(NoodlesSerialCommand.getChoiceNoodles(noodlesNO));
    }

    public void startDropPackage(NoodleEventData noodleEventData) {
        choiceChannel = NoodlesSerialCommand.getChoiceChannel(noodleEventData);
        Timber.e("choiceChannel.size()="+choiceChannel.size());
        for (int i=0;i<choiceChannel.size();i++){

        }
        if (choiceChannel.size() == 0){
            onFinish();
            count=0;
            return;
        }
        logStep("===========================================");
        byte[] bytes = choiceChannel.get(0);
        sendSerialData1(bytes);
    }

    public void startDropPackage(RiceOrderND riceOrderND) {
        //将RiceOrderND转化为NoodleEventData
        NoodleEventData noodleEventData = new NoodleEventData();
        noodleEventData.spoilName = riceOrderND.spoilName;
        noodleEventData.noodle_state = riceOrderND.noodleState;
        startDropPackage(noodleEventData);
    }

    public void handleReceived(ComBean comBean) {
        mSeasoningPackageReceiver.handleReceivedData(comBean);
    }

    public void recycle() {

        mSeasoningPackageReceiver.setExecStep(0);
        mSeasoningPackageReceiver.setOnFailureListener(null);
        mSeasoningPackageReceiver.setOnSuccessListener(null);
        mFormulaSuccessListener = null;
        mFormulaFailureListener = null;
        mSeasoningPackageReceiver.releaseListener();
    }

    private int count = 0;

    private void handleSuccess() {
        mFormulaSuccessListener = new SeasoningPackageReceiver.OnSuccessListener() {
            @Override
            public void dropOnePackage() {
                logStep("掉配料包1成功");
                if (choiceChannel.size() >= 2){
                    byte[] bytes = choiceChannel.get(1);
                    sendSerialData1(bytes);
                }else {
                    onFinish();
                    mSeasoningPackageReceiver.setExecStep(0);
                    count = 0;
                }
            }

            @Override
            public void dropTwoPackage() {
                logStep("掉配料包2成功");
                Timber.e("choiceChannel.size()="+choiceChannel.size());
                if (choiceChannel.size() >= 3){
                    byte[] bytes = choiceChannel.get(2);
                    sendSerialData1(bytes);
                }else {
                    onFinish();
                    mSeasoningPackageReceiver.setExecStep(0);
                    count = 0;
                }
            }

            @Override
            public void dropThreePackage() {
                logStep("掉配料包3成功");
                onFinish();
                count = 0;
            }

            @Override
            public void choiceNoodles() {
                logStep("选择米粉成功");
                onChoiceNoodlesFinish();
                mSeasoningPackageReceiver.setExecStep(0);
                count = 0;
            }

            @Override
            public void openSteam() {
                logStep("打开蒸汽成功");
                onOpenSteamFinish();
                mSeasoningPackageReceiver.setExecStep(0);
                count = 0;
            }

            @Override
            public void bottledWater() {
                logStep("换桶装水成功");
                onBottledWaterFinish();
                mSeasoningPackageReceiver.setExecStep(0);
                count = 0;
            }
        };
        mSeasoningPackageReceiver.setOnSuccessListener(mFormulaSuccessListener);
    }

    private void handleFailure() {
        mFormulaFailureListener = new SeasoningPackageReceiver.OnFailureListener() {
            @Override
            public void dropOnePackage(int errorCode) {
                logStep("掉配料包1失败");
                handleError(""+errorCode);
            }

            @Override
            public void dropTwoPackage(int errorCode) {
                logStep("掉配料包2失败");
                handleError(""+errorCode);
            }

            @Override
            public void dropThreePackage(int errorCode) {
                logStep("掉配料包3失败");
                handleError(""+errorCode);
            }

            @Override
            public void choiceNoodles(int errorCode) {
                if (errorCode == 61){//运动没到位
                    int numberNoodlesMax = CalNoodlesNum.getNumberNoodlesMax(mNoodlesNO);
                    mNoodlesNO++;
                    if (mNoodlesNO<=numberNoodlesMax) {
                        int execStep = mSeasoningPackageReceiver.getExecStep();
                        mSeasoningPackageReceiver.setExecStep(execStep-1);
                        sendSerialData1(NoodlesSerialCommand.getChoiceNoodles(mNoodlesNO));
                    }else {
                        handleError(""+errorCode);
                    }
                }else {
                    handleError(""+errorCode);
                }
            }

            @Override
            public void openSteam(final int errorCode) {
                logStep("打开蒸汽失败");
                AppApplication.getHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (errorCount>5){
                            dealWithError(""+errorCode);
                            return;
                        }
                        sendSerialData(NoodlesSerialCommand.OPEN_STEAM);
                        errorCount++;
                    }
                },10*1000);
            }

            @Override
            public void bottledWater(int errorCode) {
                logStep("换桶装水失败");
                handleError(""+errorCode);
            }
        };
         mSeasoningPackageReceiver.setOnFailureListener(mFormulaFailureListener);
    }

    //模组2发送数据
    private void sendSerialData(byte[] data) {
        if (!mCardSerialHelper.isOpen()) {
            CardSerialOpenHelper.getIns().startCardSerial();
            Log.e(">>>>>>>>>>>>>>", "isOpen");
        }
        CardSerialOpenHelper.getIns().setMakeOrRecycleNoodles(SEASONING_PACKAGE);
        mCardSerialHelper.send(data);
    }

    //模组2发送数据
    private void sendSerialDataBottledWater(byte[] data) {
        if (!mCardSerialHelper.isOpen()) {
            CardSerialOpenHelper.getIns().startCardSerial();
            Log.e(">>>>>>>>>>>>>>", "isOpen");
        }
        CardSerialOpenHelper.getIns().setMakeOrRecycleNoodles(BOTTLED_WATER);
        mCardSerialHelper.send(data);
    }

    //模组1发送数据
    private void sendSerialData1(byte[] data) {
        if (errorCount > 7) {
            mSeasoningPackageReceiver.setExecStep(0);
            count=0;
            errorCount=0;
            Timber.e("count="+count);
            tooMuchError();
            return;
        }
        if (!mCardSerialHelper1.isOpen()) {
            CardSerialOpenHelper.getIns().startCardSerial();
            Log.e(">>>>>>>>>>>>>>", "isOpen");
        }
        CardSerialOpenHelper1.getIns().setMakeOrRecycleNoodles(SEASONING_PACKAGE);
        mCardSerialHelper1.send(data);
    }

    private void handleError(String info){
        mSeasoningPackageReceiver.setExecStep(0);
        errorCount=0;
        count=0;
        dealWithError(info);
    }

    protected abstract void dealWithError(String info);

    protected abstract void onFinish();

    protected void onChoiceNoodlesFinish(){};

    protected void onOpenSteamFinish(){};

    protected void onBottledWaterFinish(){};

    protected void tooMuchError() {
        Timber.e("多次错误异常");
    }

    private void logStep(String msg) {
        Timber.e(">>>>>>>>>>>> 步骤 " + count + "  : " +msg);
        LogUtils.file(">>>>>>>>>>>> 步骤 " + count + "  : " +msg);
        count++;
    }
}
