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
public class SeasoningPackageReceiver {

    private static SeasoningPackageReceiver sInstance;

    public static SeasoningPackageReceiver getIns() {
        if (sInstance == null) {
            synchronized (SeasoningPackageReceiver.class) {
                if (sInstance == null) {
                    sInstance = new SeasoningPackageReceiver();
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
//        Timber.e("mExecStep:"+mExecStep);
        switch (mExecStep){
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
                functionCodeFourFive(temp);
                break;
        }
    }

    private void functionCodeFourFive(byte[] temp) {
        byte functionCode = temp[1];
        byte f1OrF2 = temp[0];
        boolean isComplete = false;
        int errorCode;
//        if(ByteUtil.isByteEqual(functionCode,(byte)0x45) && ByteUtil.isByteEqual(f1OrF2,(byte)0xF2)){
        if(ByteUtil.isByteEqual(functionCode,(byte)0x45)){
            errorCode = ByteUtil.add(temp[5],temp[6]);
//            if (ByteUtil.isByteEqual(temp[10],(byte)0xFF)){
            if (ByteUtil.isByteEqual(temp[10],(byte)0xFF) && errorCode==0){
                isComplete = true;
            }else {
                isComplete = false;
            }
        }else {
            logReceive("function45出错");
            return;
        }

        switch (mExecStep){
            case 1:
                if (isComplete){
                    logReceive("提供配料包1完成,mExecStep="+ mExecStep);
                    mOnSuccessListener.dropOnePackage();
                }else {
                    logReceive("提供配料包1没完成,mExecStep="+ mExecStep);
                    mExecStep--;
                    mOnFailureListener.dropOnePackage(errorCode);
                }
                break;
            case 2:
                if (isComplete){
                    logReceive("提供配料包2完成,mExecStep="+ mExecStep);
                    mOnSuccessListener.dropTwoPackage();
                }else {
                    logReceive("提供配料包2没完成,mExecStep="+ mExecStep);
                    mExecStep--;
                    mOnFailureListener.dropTwoPackage(errorCode);
                }
                break;
            case 3:
                if (isComplete){
                    logReceive("提供配料包3完成,mExecStep="+ mExecStep);
                    mOnSuccessListener.dropThreePackage();
                    mExecStep=0;
                }else {
                    logReceive("提供配料包3没完成,mExecStep="+ mExecStep);
                    mOnFailureListener.dropThreePackage(errorCode);
                }
                break;
            case 4:
                if (isComplete){
                    logReceive("选择米粉完成,mExecStep="+ mExecStep);
                    mOnSuccessListener.choiceNoodles();
                    mExecStep=0;
                }else {
                logReceive("选择米粉没完成,mExecStep="+ mExecStep);
                mOnFailureListener.choiceNoodles(errorCode);
            }
                break;
            case 5:
                if (isComplete){
                    logReceive("打开蒸汽完成,mExecStep="+ mExecStep);
                    mOnSuccessListener.openSteam();
                    mExecStep=0;
                }else {
                    logReceive("打开蒸汽没完成,mExecStep="+ mExecStep);
                    mOnFailureListener.openSteam(errorCode);
                }
                break;
            case 6:
                if (isComplete){
                    logReceive("换桶装水完成,mExecStep="+ mExecStep);
                    mOnSuccessListener.bottledWater();
                    mExecStep=0;
                }else {
                    logReceive("换桶装水没完成,mExecStep="+ mExecStep);
                    mOnFailureListener.bottledWater(errorCode);
                }
                break;
        }
    }


    public void setOnSuccessListener(OnSuccessListener onSuccessListener) {
        mOnSuccessListener = onSuccessListener;
    }

    private OnSuccessListener mOnSuccessListener;

    public interface OnSuccessListener {
        void dropOnePackage();

        void dropTwoPackage();

        void dropThreePackage();

        void choiceNoodles();

        void openSteam();

        void bottledWater();
    }

    public void setOnFailureListener(OnFailureListener onFailureListener) {
        mOnFailureListener = onFailureListener;
    }

    private OnFailureListener mOnFailureListener;

    public interface OnFailureListener {
        void dropOnePackage(int errorCode);

        void dropTwoPackage(int errorCode);

        void dropThreePackage(int errorCode);

        void choiceNoodles(int errorCode);

        void openSteam(int errorCode);

        void bottledWater(int errorCode);
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
            }else if (ByteUtil.isByteEqual(functionCode,(byte) 0x10)){
                packLen = 8;
            }else if (ByteUtil.isByteEqual(functionCode,(byte) 0x04)){
                packLen = 3+bytes[2]+2;
            }else if (ByteUtil.isByteEqual(functionCode,(byte) 0x03)){
                packLen = 3+bytes[2]+2;
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
