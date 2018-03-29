package com.benxiang.noodles.entrance;

import android.util.Log;

import com.benxiang.noodles.serialport.ComBean;
import com.benxiang.noodles.serialport.SerialHelper;
import com.benxiang.noodles.serialport.cardmac.CardSerialOpenHelper;
import com.benxiang.noodles.serialport.cardmac.NoodlesSerialCommand;
import com.benxiang.noodles.serialport.cardmac.RecycleSerialReceiver;
import com.blankj.utilcode.util.LogUtils;

import timber.log.Timber;

import static com.benxiang.noodles.serialport.cardmac.CardSerialOpenHelper.RECYCLE_NOODLES;

/**
 * Created by Zeqiang Fang on 2017/9/1.
 */
public abstract class RecycleNoodlesUtil {
    private RecycleSerialReceiver mCardSerialReceiver;
    //模组2
    private SerialHelper mCardSerialHelper;

    private RecycleSerialReceiver.OnSuccessListener mCardSuccessListener;
    private RecycleSerialReceiver.OnFailureListener mCardFailureListener;
    private int errorCount = 0;
    private int mNumberNoodles;
    private boolean isDirectRecycle = false;
//    private boolean isBeltForeward = false;

    protected RecycleNoodlesUtil() {
//        CardSerialOpenHelper.getIns(MAKE_NOODLES).closeCardSerial();
        mCardSerialHelper = CardSerialOpenHelper.getIns().getCardSerialHelper();
        CardSerialOpenHelper.getIns().setTimeoutListener(new CardSerialOpenHelper.TimeoutListener() {
            @Override
            public void onTimeout() {
                dealWithError(202+"");
            }
        });
        mCardSerialReceiver = RecycleSerialReceiver.getIns();
        handleSuccess();
        handleFailure();
    }

    public void setDirectRecycle(boolean directRecycle) {
//        Timber.e("倒计时结束,不循环检测，直接回收");
        LogUtils.file("===========================================倒计时结束,不循环检测，直接回收");
        isDirectRecycle = directRecycle;
    }


    public void startCheckBox() {
        logStep("===========================================");
        sendSerialData(NoodlesSerialCommand.CHECK_BOX);
    }

    public void handleReceived(ComBean comBean) {
        mCardSerialReceiver.handleReceivedData(comBean);
    }

    public void recycle() {
        mCardSerialReceiver.setExecStep(0);
        mCardSerialReceiver.setOnFailureListener(null);
        mCardSerialReceiver.setOnSuccessListener(null);
        mCardSuccessListener = null;
        mCardFailureListener = null;
        mCardSerialReceiver.releaseListener();
    }

    private int count = 0;

    private void handleSuccess() {
        mCardSuccessListener = new RecycleSerialReceiver.OnSuccessListener() {

            @Override
            public void checkBox() {
                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                logStep("取料箱有空位");
                sendSerialData(NoodlesSerialCommand.CLOSE_DOOR);
            }

            @Override
            public void closeDoor() {
                logStep("关闭取粉门完成");
                sendSerialData(NoodlesSerialCommand.PUSH_BOWL);
            }

            @Override
            public void pushBowl() {
                logStep("推碗回收完成");
                    onFinish();
                    count=0;
                    mCardSerialReceiver.setExecStep(0);
            }

            @Override
            public void beltForeward() {
                logStep("皮带正转完成");
                onFinish();
                count = 0;
                mCardSerialReceiver.setExecStep(0);
            }


        };
        mCardSerialReceiver.setOnSuccessListener(mCardSuccessListener);
    }

        private void handleFailure() {
        mCardFailureListener = new RecycleSerialReceiver.OnFailureListener() {

            @Override
            public void checkBox() {

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (isDirectRecycle){
                    //取走碗后，4秒后发送回收指令，但是该指令没动作，检测到有碗才会进行倒计时60秒进行回收
                    try {
                        Thread.sleep(4000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    isDirectRecycle = false;
                    logStep("取料箱不为空");
                    sendSerialData(NoodlesSerialCommand.CLOSE_DOOR);
                }else {
                    Timber.e("取料箱不为空");
                    int execStep = mCardSerialReceiver.getExecStep();
                    mCardSerialReceiver.setExecStep(execStep-1);
                    sendSerialData(NoodlesSerialCommand.CHECK_BOX);
                }
            }

            @Override
            public void closeDoor(int errorCode) {
                logStep("关门没完成");
                handleError(""+errorCode);
            }


            @Override
            public void pushBowl(int errorCode) {
                logStep("推碗出错");
                handleError(""+errorCode);
            }

            @Override
            public void beltForeward(int errorCode) {
                logStep("皮带正转出错");
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

        CardSerialOpenHelper.getIns().setMakeOrRecycleNoodles(RECYCLE_NOODLES);
        mCardSerialHelper.send(data);
    }


    private void handleError(String info){
        mCardSerialReceiver.setExecStep(0);
        errorCount=0;
        count=0;
        dealWithError(info);
    }

    protected abstract void dealWithError(String info);

    protected abstract void onFinish();

    protected void tooMuchError() {
        Timber.e("多次错误异常");
    }

    private void logStep(String msg) {
        Timber.e(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> 步骤 " + count + "  : " +msg);
        LogUtils.file(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> 步骤 " + count + "  : " +msg);
        count++;
    }
}
