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
public class WriteFormulaReceiver {

    private static WriteFormulaReceiver sInstance;

    public static WriteFormulaReceiver getIns() {
        if (sInstance == null) {
            synchronized (WriteFormulaReceiver.class) {
                if (sInstance == null) {
                    sInstance = new WriteFormulaReceiver();
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
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
                functionCodeOneZero(temp);
                break;
        }
    }

    private void functionCodeOneZero(byte[] temp) {
        byte functionCode = temp[1];
        boolean isWriteSuccess = false;
        int errorCode;
        if(ByteUtil.isByteEqual(functionCode,(byte)0x10)){
            //写入寄存器个数为6，则写入成功
            if (ByteUtil.isByteEqual(temp[5],(byte)0x06)){
                isWriteSuccess = true;
            }else {
                isWriteSuccess = false;
            }
        }else {
            logReceive("function10出错");
            mExecStep=0;
            return;
        }

        switch (mExecStep){
            case 1:
                if (isWriteSuccess){
                    logReceive("写入AA配方成功,mExecStep="+ mExecStep);
                    mOnSuccessListener.writeAAFormula();
                }else {
                    logReceive("写入AA配方失败,mExecStep="+ mExecStep);
                    mOnFailureListener.writeAAFormula();
                }
                break;
            case 2:
                if (isWriteSuccess){
                    logReceive("写入AB配方成功,mExecStep="+ mExecStep);
                    mOnSuccessListener.writeABFormula();
                }else {
                    logReceive("写入AB配方失败,mExecStep="+ mExecStep);
                    mOnFailureListener.writeABFormula();
                }
                break;
            case 3:
                if (isWriteSuccess){
                    logReceive("写入AC配方成功,mExecStep="+ mExecStep);
                    mOnSuccessListener.writeACFormula();
                }else {
                    logReceive("写入AC配方失败,mExecStep="+ mExecStep);
                    mOnFailureListener.writeACFormula();
                }
                break;
            case 4:
                if (isWriteSuccess){
                    logReceive("写入BA配方成功,mExecStep="+ mExecStep);
                    mOnSuccessListener.writeBAFormula();
                }else {
                    logReceive("写入BA配方失败,mExecStep="+ mExecStep);
                    mOnFailureListener.writeBAFormula();
                }
                break;
            case 5:
                if (isWriteSuccess){
                    logReceive("写入BB配方成功,mExecStep="+ mExecStep);
                    mOnSuccessListener.writeBBFormula();
                }else {
                    logReceive("写入BB配方失败,mExecStep="+ mExecStep);
                    mOnFailureListener.writeBBFormula();
                }
                break;
            case 6:
                if (isWriteSuccess){
                    logReceive("写入BC配方成功,mExecStep="+ mExecStep);
                    mOnSuccessListener.writeBCFormula();
                }else {
                    logReceive("写入BC配方失败,mExecStep="+ mExecStep);
                    mOnFailureListener.writeBCFormula();
                }
                break;
            case 7:
                if (isWriteSuccess){
                    logReceive("写入CA配方成功,mExecStep="+ mExecStep);
                    mOnSuccessListener.writeCAFormula();
                }else {
                    logReceive("写入CA配方失败,mExecStep="+ mExecStep);
                    mOnFailureListener.writeCAFormula();
                }
                break;
            case 8:
                if (isWriteSuccess){
                    logReceive("写入CB配方成功,mExecStep="+ mExecStep);
                    mOnSuccessListener.writeCBFormula();
                }else {
                    logReceive("写入CB配方失败,mExecStep="+ mExecStep);
                    mOnFailureListener.writeCBFormula();
                }
                break;
            case 9:
                if (isWriteSuccess){
                    logReceive("写入CC配方成功,mExecStep="+ mExecStep);
                    mOnSuccessListener.writeCCFormula();
//                    mExecStep=0;
                }else {
                    logReceive("写入CC配方失败,mExecStep="+ mExecStep);
                    mOnFailureListener.writeCCFormula();
                }
                break;
            case 10:
                if (isWriteSuccess){
                    logReceive("写入AA有醋配方成功,mExecStep="+ mExecStep);
                    mOnSuccessListener.writeAAFormulaVinegar();
                }else {
                    logReceive("写入AA有醋配方失败,mExecStep="+ mExecStep);
                    mOnFailureListener.writeAAFormulaVinegar();
                }
                break;
            case 11:
                if (isWriteSuccess){
                    logReceive("写入AB有醋配方成功,mExecStep="+ mExecStep);
                    mOnSuccessListener.writeABFormulaVinegar();
                }else {
                    logReceive("写入AB有醋配方失败,mExecStep="+ mExecStep);
                    mOnFailureListener.writeABFormulaVinegar();
                }
                break;
            case 12:
                if (isWriteSuccess){
                    logReceive("写入AC有醋配方成功,mExecStep="+ mExecStep);
                    mOnSuccessListener.writeACFormulaVinegar();
                }else {
                    logReceive("写入AC有醋配方失败,mExecStep="+ mExecStep);
                    mOnFailureListener.writeACFormulaVinegar();
                }
                break;
            case 13:
                if (isWriteSuccess){
                    logReceive("写入BA有醋配方成功,mExecStep="+ mExecStep);
                    mOnSuccessListener.writeBAFormulaVinegar();
                }else {
                    logReceive("写入BA有醋配方失败,mExecStep="+ mExecStep);
                    mOnFailureListener.writeBAFormulaVinegar();
                }
                break;
            case 14:
                if (isWriteSuccess){
                    logReceive("写入BB有醋配方成功,mExecStep="+ mExecStep);
                    mOnSuccessListener.writeBBFormulaVinegar();
                }else {
                    logReceive("写入BB有醋配方失败,mExecStep="+ mExecStep);
                    mOnFailureListener.writeBBFormulaVinegar();
                }
                break;
            case 15:
                if (isWriteSuccess){
                    logReceive("写入BC有醋配方成功,mExecStep="+ mExecStep);
                    mOnSuccessListener.writeBCFormulaVinegar();
                }else {
                    logReceive("写入BC有醋配方失败,mExecStep="+ mExecStep);
                    mOnFailureListener.writeBCFormulaVinegar();
                }
                break;
            case 16:
                if (isWriteSuccess){
                    logReceive("写入CA有醋配方成功,mExecStep="+ mExecStep);
                    mOnSuccessListener.writeCAFormulaVinegar();
                }else {
                    logReceive("写入CA有醋配方失败,mExecStep="+ mExecStep);
                    mOnFailureListener.writeCAFormulaVinegar();
                }
                break;
            case 17:
                if (isWriteSuccess){
                    logReceive("写入CB有醋配方成功,mExecStep="+ mExecStep);
                    mOnSuccessListener.writeCBFormulaVinegar();
                }else {
                    logReceive("写入CB有醋配方失败,mExecStep="+ mExecStep);
                    mOnFailureListener.writeCBFormulaVinegar();
                }
                break;
            case 18:
                if (isWriteSuccess){
                    logReceive("写入CC有醋配方成功,mExecStep="+ mExecStep);
                    mOnSuccessListener.writeCCFormulaVinegar();
                    mExecStep=0;
                }else {
                    logReceive("写入CC有醋配方失败,mExecStep="+ mExecStep);
                    mOnFailureListener.writeCCFormulaVinegar();
                }
            case 19:
                if (isWriteSuccess){
                    logReceive("读取AA配方成功,mExecStep="+ mExecStep);
                    mOnSuccessListener.readAAFormula();
                    mExecStep=0;
                }else {
                    logReceive("读取AA配方成功,mExecStep="+ mExecStep);
                    mOnFailureListener.readAAFormula();
                }
                break;
        }
    }


    public void setOnSuccessListener(OnSuccessListener onSuccessListener) {
        mOnSuccessListener = onSuccessListener;
    }

    private OnSuccessListener mOnSuccessListener;

    public interface OnSuccessListener {

        void writeAAFormula();

        void writeABFormula();

        void writeACFormula();

        void writeBAFormula();

        void writeBBFormula();

        void writeBCFormula();

        void writeCAFormula();

        void writeCBFormula();

        void writeCCFormula();

        void writeAAFormulaVinegar();

        void writeABFormulaVinegar();

        void writeACFormulaVinegar();

        void writeBAFormulaVinegar();

        void writeBBFormulaVinegar();

        void writeBCFormulaVinegar();

        void writeCAFormulaVinegar();

        void writeCBFormulaVinegar();

        void writeCCFormulaVinegar();

        void readAAFormula();
    }

    public void setOnFailureListener(OnFailureListener onFailureListener) {
        mOnFailureListener = onFailureListener;
    }

    private OnFailureListener mOnFailureListener;

    public interface OnFailureListener {

        void writeAAFormula();

        void writeABFormula();

        void writeACFormula();

        void writeBAFormula();

        void writeBBFormula();

        void writeBCFormula();

        void writeCAFormula();

        void writeCBFormula();

        void writeCCFormula();

        void writeAAFormulaVinegar();

        void writeABFormulaVinegar();

        void writeACFormulaVinegar();

        void writeBAFormulaVinegar();

        void writeBBFormulaVinegar();

        void writeBCFormulaVinegar();

        void writeCAFormulaVinegar();

        void writeCBFormulaVinegar();

        void writeCCFormulaVinegar();

        void readAAFormula();
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
