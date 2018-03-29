package com.benxiang.noodles.entrance;

import android.util.Log;

import com.benxiang.noodles.serialport.ComBean;
import com.benxiang.noodles.serialport.SerialHelper;
import com.benxiang.noodles.serialport.cardmac.CardSerialOpenHelper;
import com.benxiang.noodles.serialport.cardmac.CardSerialOpenHelper1;
import com.benxiang.noodles.serialport.cardmac.CardSerialReceiver;
import com.benxiang.noodles.serialport.cardmac.NoodlesSerialCommand;
import com.benxiang.noodles.serialport.data.constant.NoodlesType;
import com.benxiang.noodles.utils.CalNoodlesNum;
import com.blankj.utilcode.util.LogUtils;

import java.util.ArrayList;

import timber.log.Timber;

import static com.benxiang.noodles.serialport.cardmac.CardSerialOpenHelper.MAKE_NOODLES;

/**
 * Created by Zeqiang Fang on 2017/9/1.
 */
public abstract class ReadReceiveCardUtil {
    private CardSerialReceiver mCardSerialReceiver;
    //模组2
    private SerialHelper mCardSerialHelper;
    //模组1
    private SerialHelper mCardSerialHelper1;
    private CardSerialReceiver.OnSuccessListener mCardSuccessListener;
    private CardSerialReceiver.OnFailureListener mCardFailureListener;
    private int errorCount = 0;
    private int[] mNumberNoodlesArr;

    protected ReadReceiveCardUtil() {
        mCardSerialHelper = CardSerialOpenHelper.getIns().getCardSerialHelper();
        mCardSerialHelper1 = CardSerialOpenHelper1.getIns().getCardSerialHelper();
        mCardSerialReceiver = CardSerialReceiver.getIns();
        handleSuccess();
        handleFailure();
    }

    public void setNumberNoodles(int numberNoodles){
        mNumberNoodlesArr = new int[]{numberNoodles};
    }


    public void setTypeNoodles(ArrayList<NoodlesType> noodleTypeArr){
        mNumberNoodlesArr = CalNoodlesNum.getNumberNoodlesMinArr(noodleTypeArr);
    }

    public void startCheckBelt() {
        logStep("===========================================");
        sendSerialData(NoodlesSerialCommand.CHECK_BELT);
    }

    public void handleReceived(ComBean comBean) {
        mCardSerialReceiver.handleReceivedData(comBean);
    }

    public void recycle() {
        mCardSerialReceiver.setOnFailureListener(null);
        mCardSerialReceiver.setOnSuccessListener(null);
        mCardSuccessListener = null;
        mCardFailureListener = null;
        mCardSerialReceiver.releaseListener();
    }

    private int count = 0;

    private void handleSuccess() {
        mCardSuccessListener = new CardSerialReceiver.OnSuccessListener() {

            @Override
            public void checkBelt() {
                logStep("皮带有空位");
                sendSerialData(NoodlesSerialCommand.CHECK_BOX);
            }

            @Override
            public void checkBox() {
                logStep("取料箱有空位");
                sendSerialData1(NoodlesSerialCommand.getChoiceNoodles(mNumberNoodlesArr[0]));
            }

            @Override
            public void choiceNoodles() {
                logStep("选择米粉完成");
                sendSerialData(NoodlesSerialCommand.CHECK_NOODLES);
            }

            @Override
            public void checkNoodles() {
                logStep("米粉到达完成");
                sendSerialData(NoodlesSerialCommand.SEND_TO_SOUP_AREA);
            }

            @Override
            public void sendToSoupArea() {
                logStep("送到加汤区完成");
                byte[] soupOrHeatCommand = CalNoodlesNum.getSoupOrHeatCommand(mNumberNoodlesArr[0],false);
                sendSerialData(soupOrHeatCommand);
            }

            @Override
            public void addSoup() {
                logStep("加汤完成");
                sendSerialData(NoodlesSerialCommand.SEND_TO_HEAT_AREA);
            }

            @Override
            public void sendToHeat() {
                logStep("送到加热区完成");
//                sendSerialData(NoodlesSerialCommand.START_HEAT);
                byte[] soupOrHeatCommand = CalNoodlesNum.getSoupOrHeatCommand(mNumberNoodlesArr[0],false);
                sendSerialData(soupOrHeatCommand);
            }

            @Override
            public void startHeat() {
                logStep("启动加热完成");
                sendSerialData(NoodlesSerialCommand.SEND_TO_RECYLE);
            }

            @Override
            public void sendResycle() {
                logStep("送到回收区完成");
                sendSerialData(NoodlesSerialCommand.CLOSE_DOOR);
            }

            @Override
            public void closeDoor() {
                logStep("关闭取粉门完成");
                sendSerialData(NoodlesSerialCommand.SEND_TO_DOOR);
            }

            @Override
            public void sendToDoor() {
                logStep("送到取粉门完成");
                sendSerialData1(NoodlesSerialCommand.SUPPLY_PACKAGE);
            }

            @Override
            public void supplyPackage() {
                logStep("提供配料包完成");
                sendSerialData(NoodlesSerialCommand.OPEN_DOOR);
            }

            @Override
            public void openDoor() {
                logStep("打开取粉门完成");
                onFinish(mNumberNoodlesArr[0]);
                count=0;
            }
        };
        mCardSerialReceiver.setOnSuccessListener(mCardSuccessListener);
    }

    private void handleFailure() {
        mCardFailureListener = new CardSerialReceiver.OnFailureListener() {

            @Override
            public void checkBelt() {
                sendSerialData(NoodlesSerialCommand.CHECK_BELT);
                errorCount++;
            }

            @Override
            public void checkBox() {
                sendSerialData(NoodlesSerialCommand.CHECK_BOX);
                errorCount++;
            }

            @Override
            public void choiceNoodles(int errorCode) {
                if (errorCode == 61){//运动没到位
                    int numberNoodlesMax = CalNoodlesNum.getNumberNoodlesMax(mNumberNoodlesArr[0]);
                    mNumberNoodlesArr[0]++;
                    if (mNumberNoodlesArr[0]<=numberNoodlesMax) {
                        int execStep = mCardSerialReceiver.getExecStep();
                        mCardSerialReceiver.setExecStep(execStep-1);
                        sendSerialData1(NoodlesSerialCommand.getChoiceNoodles(mNumberNoodlesArr[0]));
                    }else {
                        handleError(""+errorCode);
                    }
                }else {
                    handleError(""+errorCode);
                }
            }

            @Override
            public void checkNoodles() {
                sendSerialData(NoodlesSerialCommand.CHECK_NOODLES);
                errorCount++;
            }

            @Override
            public void sendToSoupArea(int errorCode) {
                handleError(""+errorCode);
            }

            @Override
            public void addSoup(int errorCode) {
                handleError(""+errorCode);
            }

            @Override
            public void sendToHeat(int errorCode) {
                handleError(""+errorCode);
            }

            @Override
            public void startHeat(int errorCode) {
                if (errorCount>3){
                    handleError(""+errorCode);
//                    errorCount=0;
                    return;
                }else{
                    sendSerialData(NoodlesSerialCommand.START_HEAT);
                }
                errorCount++;
            }

            @Override
            public void sendResycle(int errorCode) {
                handleError(""+errorCode);
            }

            @Override
            public void closeDoor(int errorCode) {
                handleError(""+errorCode);
            }

            @Override
            public void sendToDoor(int errorCode) {
                handleError(""+errorCode);
            }

            @Override
            public void supplyPackage(int errorCode) {
                handleError(""+errorCode);
            }

            @Override
            public void openDoor(int errorCode) {
                handleError(""+errorCode);
            }
        };
        mCardSerialReceiver.setOnFailureListener(mCardFailureListener);
    }

    //模组2发送数据
    private void sendSerialData(byte[] data) {
        if (errorCount > 5) {
            mCardSerialReceiver.setExecStep(0);
            count=0;
            errorCount=0;
            Timber.e("count="+count);
            tooMuchError();
            return;
        }
        if (!mCardSerialHelper.isOpen()) {
            CardSerialOpenHelper.getIns().startCardSerial();
            Log.e(">>>>>>>>>>>>>>", "isOpen");
        }
        CardSerialOpenHelper.getIns().setMakeOrRecycleNoodles(MAKE_NOODLES);
        mCardSerialHelper.send(data);
    }

    //模组1发送数据
    private void sendSerialData1(byte[] data) {
        if (errorCount > 5) {
            mCardSerialReceiver.setExecStep(0);
            count=0;
            errorCount=0;
            Timber.e("count="+count);
            tooMuchError();
            return;
        }
        if (!mCardSerialHelper1.isOpen()) {
            CardSerialOpenHelper1.getIns().startCardSerial();
            Log.e(">>>>>>>>>>>>>>", "isOpen");
        }
        mCardSerialHelper1.send(data);
    }

    private void handleError(String info){
        mCardSerialReceiver.setExecStep(0);
        errorCount=0;
        count=0;
        dealWithError(info);
    }

    protected abstract void dealWithError(String info);

    protected abstract void onFinish(int numberNoodles);

    protected void tooMuchError() {
    }

    private void logStep(String msg) {
        Timber.e(">>>>>>>>>>>> 步骤 " + count + "  : " +msg);
        LogUtils.file(">>>>>>>>>>>> 步骤 " + count + "  : " +msg);
        count++;
    }
}
