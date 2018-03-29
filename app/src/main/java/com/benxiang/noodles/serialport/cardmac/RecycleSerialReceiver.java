package com.benxiang.noodles.serialport.cardmac;

import com.benxiang.noodles.serialport.ComBean;
import com.benxiang.noodles.utils.ByteUtil;
import com.benxiang.noodles.utils.CRC16;
import com.benxiang.noodles.utils.MyFunc;
import com.blankj.utilcode.util.LogUtils;

import java.io.ByteArrayOutputStream;

import timber.log.Timber;


/**
 * Created by Zeqiang Fang on 2017/8/29.
 */
public class RecycleSerialReceiver {

    private static RecycleSerialReceiver sInstance;

    public static RecycleSerialReceiver getIns() {
        if (sInstance == null) {
            synchronized (RecycleSerialReceiver.class) {
                if (sInstance == null) {
                    sInstance = new RecycleSerialReceiver();
                }
            }
        }
        return sInstance;
    }

    public void handleReceivedData(ComBean comBean) {
        dealRevData(comBean.bRec);
    }


    private int mExecStep =0;

    public void setExecStep(int execStep){
        mExecStep = execStep;
    }

    public int getExecStep(){
        return mExecStep;
    }

    private void dealWithReceived(byte[] temp) {
        logReceive("拼接的字符"+ MyFunc.ByteArrToHex(temp));
//        Timber.e("测试皮带字符:"+ MyFunc.ByteArrToHex(CRC16.crc16Checkout(NoodlesSerialCommand.beltTest)));
        //CRC校验,200表示正确，100表示错误
        int correctOrWrong = CRC16.verifyCommandCRC(temp)?200:100;
//        Timber.e(MyFun2.);
        byte functionCode = temp[1];
        byte f1OrF2 = temp[0];
        mExecStep++;
        if (correctOrWrong==100){
            logReceive("接收出错,需重新发送数据");
            return;
        }
        Timber.e("mExecStep:"+mExecStep);
        switch (mExecStep){
            case 1:functionCodeTwo(temp);
                break;
            case 2:
            case 3:
            case 4:functionCodeFourFive(temp);
                break;
        }
    }

    private void functionCodeFourFive(byte[] temp){
        byte functionCode = temp[1];
        byte f1OrF2 = temp[0];
        boolean isComplete = false;
        int errorCode;
//        if(ByteUtil.isByteEqual(functionCode,(byte)0x45) && ByteUtil.isByteEqual(f1OrF2,(byte)0xF2)){
        if(ByteUtil.isByteEqual(functionCode,(byte)0x45)){
            errorCode = ByteUtil.add(temp[5],temp[6]);
            if (ByteUtil.isByteEqual(temp[10],(byte)0xFF)){
//            if (ByteUtil.isByteEqual(temp[10],(byte)0xFF) && errorCode==0){
                isComplete = true;
            }else {
                isComplete = false;
            }
        }else {
            logReceive("function45出错");
            return;
        }

        switch (mExecStep){
            case 2:
                if (isComplete){
                    logReceive("关门完成,mExecStep="+ mExecStep);
                    mOnSuccessListener.closeDoor();
                }else {
                    logReceive("关门没完成,mExecStep="+ mExecStep);
                    mOnFailureListener.closeDoor(errorCode);
                }
                break;
            case 3:
                if (isComplete){
                    logReceive("推碗回收完成,mExecStep="+ mExecStep);
                    mOnSuccessListener.pushBowl();
//                    mExecStep = 0;
                }else {
                    logReceive("推碗回收没完成,mExecStep="+ mExecStep);
                    mOnFailureListener.pushBowl(errorCode);
                }
                break;
            case 4:
                if (isComplete){
                    logReceive("皮带正转完成,mExecStep="+ mExecStep);
                    mOnSuccessListener.beltForeward();
                    mExecStep = 0;
                }else {
                    logReceive("皮带正转没完成,mExecStep="+ mExecStep);
                    mExecStep--;
                    mOnFailureListener.beltForeward(errorCode);
                }
                break;
        }
    }

    private void functionCodeTwo(byte[] temp){
        byte functionCode = temp[1];
        boolean empty = false;
        if(ByteUtil.isByteEqual(functionCode,(byte)0x02)){
            if (temp[3] == 0x00) {
                logReceive("没有空位,numberSensor="+ mExecStep);
                empty = false;
            } else {
                logReceive("有空位,numberSensor="+ mExecStep);
                empty = true;
            }
        }else {
            logReceive("function2出错");
            return;
        }
        switch (mExecStep){
            //取碗位有空位
            case 1:
                if (empty){
                    mOnSuccessListener.checkBox();
                }else {
                    mOnFailureListener.checkBox();
                }
                break;
        }
    }

    public void setOnSuccessListener(OnSuccessListener onSuccessListener) {
        mOnSuccessListener = onSuccessListener;
    }

    private OnSuccessListener mOnSuccessListener;

    public interface OnSuccessListener {

        void checkBox();

        void closeDoor();

//        void beltInversion();

        void pushBowl();

        void beltForeward();
    }

    public void setOnFailureListener(OnFailureListener onFailureListener) {
        mOnFailureListener = onFailureListener;
    }

    private OnFailureListener mOnFailureListener;

    public interface OnFailureListener {
        void checkBox();

        void closeDoor(int errorCode);

//        void beltInversion(int errorCode);

        void pushBowl(int errorCode);

        void beltForeward(int errorCode);
    }

    public void releaseListener() {
        mOnSuccessListener = null;
        mOnFailureListener = null;
    }

    /**
     * 检测校验码是否正确
     */
    private boolean checkCRCSuccess(byte[] bytes) {
        int len = bytes.length;
        byte checkByte = bytes[0];
        for (int i = 0; i < bytes.length - 2; i++) {
            checkByte = (byte) (checkByte ^ bytes[i + 1]);
        }
        if (checkByte == bytes[len - 1]) {
            return true;
        } else {
            return false;
        }
    }


    public void dealRevData(byte[] data) {
        for (byte b : data) {
            combineData(b);
        }
    }

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    boolean isStartCombine = false;
    int packLen = -1;

    private void combineData(byte b) {
        if (ByteUtil.isByteEqual(b, (byte) 0xF2) || ByteUtil.isByteEqual(b, (byte) 0xF1)) {
            if (!isStartCombine) {
                isStartCombine = true;
            }
        }
        if (isStartCombine) {
            baos.write(b);
        }

        byte[] bytes = baos.toByteArray();
        int bufferLen = bytes.length;

        if (bufferLen == 5) {//3
            byte functionCode = bytes[1];
            if (ByteUtil.isByteEqual(functionCode,(byte) 0x02)){
                packLen = 3 + bytes[2] + 2;
            }else if (ByteUtil.isByteEqual(functionCode,(byte) 0x45)){
                packLen = 5 + bytes[4] +2;
            }
        }
        if (packLen == bufferLen) {
            dealWithResult(bytes);
            reset();
            return;
        }
        if (bufferLen > 1024) {
            reset();
        }
    }

    private void dealWithResult(byte[] bytes) {
        dealWithReceived(bytes);
    }

    public void reset() {
        baos.reset();
        isStartCombine = false;
        packLen = -1;
    }

    private void logReceive(String msg) {
        Timber.e(msg);
        LogUtils.file(msg);
    }

}
