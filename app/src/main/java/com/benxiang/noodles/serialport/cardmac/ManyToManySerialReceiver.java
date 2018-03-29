package com.benxiang.noodles.serialport.cardmac;

import com.benxiang.noodles.BuildConfig;
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
public class ManyToManySerialReceiver {

    private static ManyToManySerialReceiver sInstance;

    public static ManyToManySerialReceiver getIns() {
        if (sInstance == null) {
            synchronized (ManyToManySerialReceiver.class) {
                if (sInstance == null) {
                    sInstance = new ManyToManySerialReceiver();
                }
            }
        }
        return sInstance;
    }

    public void handleReceivedData(ComBean comBean) {
        dealRevData(comBean.bRec);
    }


    private int mExecStep =0;
    private int mNoSoupWarging = 0;
    private boolean mIsFirstSelect = true;

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
        switch (mExecStep){
            case 1:
            case 2:
                functionCodeTwo(temp);
                break;
            case 3:
                //模组1和模组2的45功能码返回值不同，分开处理==>f1
                if(ByteUtil.isByteEqual(functionCode,(byte)0x45) && ByteUtil.isByteEqual(f1OrF2,(byte)0xF1)){
                    int errorCode = ByteUtil.add(temp[5],temp[6]);
                    if (ByteUtil.isByteEqual(temp[10],(byte)0xFF) && errorCode==0){
                        logReceive("选择完成,mExecStep="+ mExecStep);
                        mOnSuccessListener.choiceNoodles();
                    }else {
                        logReceive("选择没完成mExecStep="+ mExecStep);
//                        mExecStep--;
                        mOnFailureListener.choiceNoodles(errorCode);
                    }
                }
                break;
            case 4:functionCodeTwo(temp);
                break;
            case 5://f2
            case 6:
                functionCodeFourFive(temp);
                break;
                //--------------------------------------------------------------重复选择选粉2--------------------------------------------------------------
            case 7:
                //模组1和模组2的45功能码返回值不同，分开处理==>f1
                if(ByteUtil.isByteEqual(functionCode,(byte)0x45) && ByteUtil.isByteEqual(f1OrF2,(byte)0xF1)){
                    int errorCode = ByteUtil.add(temp[5],temp[6]);
                    if (ByteUtil.isByteEqual(temp[10],(byte)0xFF) && errorCode==0){
                        logReceive("选择完成,mExecStep="+ mExecStep);
                        mOnSuccessListener.choiceNoodlesSecond();
                    }else {
                        logReceive("选择没完成mExecStep="+ mExecStep);
//                        mExecStep--;
                        mOnFailureListener.choiceNoodlesSecond(errorCode);
                    }
                }
                break;
            case 8:functionCodeTwo(temp);
                break;
                //---------------------------------------------------------------------------------------------------------------------------------------
            case 9:
            case 10:functionCodeFourFive(temp);
                break;
                //--------------------------------------------------------------重复选择选粉3--------------------------------------------------------------
            case 11:
                //模组1和模组2的45功能码返回值不同，分开处理==>f1
                if(ByteUtil.isByteEqual(functionCode,(byte)0x45) && ByteUtil.isByteEqual(f1OrF2,(byte)0xF1)){
                    int errorCode = ByteUtil.add(temp[5],temp[6]);
                    if (ByteUtil.isByteEqual(temp[10],(byte)0xFF) && errorCode==0){
                        logReceive("选择完成,mExecStep="+ mExecStep);
                        mOnSuccessListener.choiceNoodlesThird();
                    }else {
                        logReceive("选择没完成mExecStep="+ mExecStep);
//                        mExecStep--;
                        mOnFailureListener.choiceNoodlesThird(errorCode);
                    }
                }
                break;
            case 12:
                functionCodeTwo(temp);
                break;
                //---------------------------------------------------------------------------------------------------------------------------------------
            case 13:
            case 14:
                //--------------------------------------------------------------执行轮询部分：启动加热，送成品到取粉区，提供调料包，打开取粉门--------------------------------------------------------------
            case 15:
                functionCodeFourFive(temp);
                break;
            case 16:
                //模组1和模组2的45功能码返回值不同，分开处理==>f1
                if(ByteUtil.isByteEqual(functionCode,(byte)0x45) && ByteUtil.isByteEqual(f1OrF2,(byte)0xF1)){
                    int errorCode = ByteUtil.add(temp[5],temp[6]);
                    if (ByteUtil.isByteEqual(temp[10],(byte)0xFF) && errorCode==0){
                        logReceive("循环选择完成,mExecStep="+ mExecStep);
                        mOnSuccessListener.recycleChoiceNoodles();
                    }else {
                        logReceive("循环选择没完成mExecStep="+ mExecStep);
//                        mExecStep--;
                        mOnFailureListener.recycleChoiceNoodles(errorCode);
                    }
                }
                break;
            case 17:
                functionCodeTwo(temp);
                break;
            case 18:
            case 19:
            case 20:
                //---------------------------------------------------------------------------------------------------------------------------------------
            case 21:
            case 22:
            case 23:
                functionCodeFourFive(temp);
                break;
        }
    }

    private void functionCodeFourFive(byte[] temp){
        byte functionCode = temp[1];
        byte f1OrF2 = temp[0];
        byte specialCode=temp[9];
        boolean isComplete = false;
        int errorCode;
        if(ByteUtil.isByteEqual(functionCode,(byte)0x45)){
            errorCode = ByteUtil.add(temp[5],temp[6]);
            if (ByteUtil.isByteEqual(temp[10],(byte)0xFF) && errorCode==0){
                isComplete = true;
            }else {
                isComplete = false;
            }
        }else {
            Timber.e("mExecStep="+mExecStep);
            logReceive("function45出错");
            return;
        }

        switch (mExecStep){
            case 5:
                if (isComplete){
                    logReceive("送完成,mExecStep="+ mExecStep);
                    mOnSuccessListener.sendToSoupArea();
                }else {
                    logReceive("送没完成,mExecStep="+ mExecStep);
                    mExecStep--;
                    mOnFailureListener.sendToSoupArea(errorCode);
                }
                break;
            case 6:
                if (isComplete){
                    logReceive("加汤完成,mExecStep="+ mExecStep);
                    mOnSuccessListener.addSoup();
                }else {
                    logReceive("加汤没完成,mExecStep="+ mExecStep);
                    mOnFailureListener.addSoup(errorCode);
                }
                break;
            case 9:
                if (isComplete){
                    logReceive("送完成,mExecStep="+ mExecStep);
                    mOnSuccessListener.sendToHeat();
                }else {
                    logReceive("送没完成,mExecStep="+ mExecStep);
                    mExecStep--;
                    mOnFailureListener.sendToHeat(errorCode);
                }
                break;
            case 10:
                if (isComplete){
                    logReceive("启动加热,mExecStep="+ mExecStep);
                    mOnSuccessListener.startHeat();
                }else {

                    logReceive("启动加热没完成,mExecStep="+ mExecStep);
                    mOnFailureListener.startHeat(errorCode);
                }
                break;
            case 13:
                if (isComplete){
                    logReceive("送到回收区,mExecStep="+ mExecStep);
                    mOnSuccessListener.sendResycle();
                }else {
                    logReceive("送到回收区没完成,mExecStep="+ mExecStep);
                    mExecStep--;
                    mOnFailureListener.sendResycle(errorCode);
                }
                break;
            case 14:
                if (isComplete){
                    logReceive("关闭取粉门完成,mExecStep="+ mExecStep);
                    mOnSuccessListener.closeDoor();
                }else {
                    logReceive("关闭加热没完成,mExecStep="+ mExecStep);
                    mExecStep--;
                    mOnFailureListener.closeDoor(errorCode);
                }
                break;
            case 15:
                if (isComplete){
                    logReceive("循环启动加热,mExecStep="+ mExecStep);
                    mOnSuccessListener.recycleStartHeat();
                }else {

                    logReceive("循环启动加热没完成,mExecStep="+ mExecStep);
                    mOnFailureListener.recycleStartHeat(errorCode);
                }
                break;
            case 18:
                if (isComplete){
                    logReceive("循环送到取粉区完成,mExecStep="+ mExecStep);
                    mOnSuccessListener.recycleSendToDoor();
                }else {
                    logReceive("循环送到取粉区没完成,mExecStep="+ mExecStep);
                    mExecStep--;
                    mOnFailureListener.recycleSendToDoor(errorCode);
                }
                break;
            case 19:
                if (isComplete){
                    logReceive("循环提供配料包完成,mExecStep="+ mExecStep);
                    mOnSuccessListener.recycleSupplyPackage();
                }else {
                    logReceive("循环提供配料包没完成,mExecStep="+ mExecStep);
                    mExecStep--;
                    mOnFailureListener.recycleSupplyPackage(errorCode);
                }
                break;
            case 20:
                if (isComplete){
                    logReceive("循环开门完成,mExecStep="+ mExecStep);
                    mOnSuccessListener.recycleOpenDoor();
//                    mExecStep =0;
                }else {
                    logReceive("循环开门没完成,mExecStep="+ mExecStep);
                    mOnFailureListener.recycleOpenDoor(errorCode);
                }
                break;
            case 21:
                if (isComplete){
                    logReceive("送到取粉区完成,mExecStep="+ mExecStep);
                    mOnSuccessListener.sendToDoor();
                }else {
                    logReceive("送到取粉区没完成,mExecStep="+ mExecStep);
                    mExecStep--;
                    mOnFailureListener.sendToDoor(errorCode);
                }
                break;
            case 22:
                if (isComplete){
                    logReceive("提供配料包完成,mExecStep="+ mExecStep);
                    mOnSuccessListener.supplyPackage();
                }else {
                    logReceive("提供配料包没完成,mExecStep="+ mExecStep);
                    mExecStep--;
                    mOnFailureListener.supplyPackage(errorCode);
                }
                break;
            case 23:
                if (isComplete){
                    logReceive("开门完成,mExecStep="+ mExecStep);
                    mOnSuccessListener.openDoor();
                    mExecStep =0;
                }else {
                    logReceive("开门没完成,mExecStep="+ mExecStep);
                    mOnFailureListener.openDoor(errorCode);
                }
                break;
        }
    }

    private void functionCodeTwo(byte[] temp){
        byte functionCode = temp[1];
        boolean empty = false;
        if(ByteUtil.isByteEqual(functionCode,(byte)0x02)){
            if (temp[3] == 0x00) {
                logReceive("没有空位,mExecStep="+ mExecStep);
                empty = false;
            } else {
                logReceive("有空位,mExecStep="+ mExecStep);
                empty = true;
            }
        }else {
            Timber.e("mExecStep="+mExecStep);
            logReceive("function2出错");
            return;
        }
        switch (mExecStep){
            //皮带有空位
            case 1:
                if (empty|| BuildConfig.MACHINE_TEST){
                    mOnSuccessListener.checkBelt();
                }else {
                    mExecStep--;
                    mOnFailureListener.checkBelt();
                }
                break;
            //取料箱有空位
            case 2:
                if (empty|| BuildConfig.MACHINE_TEST){
                    mOnSuccessListener.checkBox();
                }else {
                    mExecStep--;
                    mOnFailureListener.checkBox();
                }
                break;
            //检查米粉已到 ==》没空位
            case 4:
                if (!empty){
                    mOnSuccessListener.checkNoodles();
                }else {
                    mExecStep--;
                    mOnFailureListener.checkNoodles();
                }
                break;
            case 8:
                if (!empty){
                    mOnSuccessListener.checkNoodlesSecond();
                }else {
                    mExecStep--;
                    mOnFailureListener.checkNoodlesSecond();
                }
                break;
            case 12:
                if (!empty){
                    mOnSuccessListener.checkNoodlesThird();
                }else {
                    mExecStep--;
                    mOnFailureListener.checkNoodlesThird();
                }
                break;
            case 17:
                if (!empty){
                    mOnSuccessListener.recycleCheckNoodles();
                }else {
                    mExecStep--;
                    mOnFailureListener.recycleCheckNoodles();
                }
                break;
        }
    }

    public void setOnSuccessListener(OnSuccessListener onSuccessListener) {
        mOnSuccessListener = onSuccessListener;
    }

    private OnSuccessListener mOnSuccessListener;

    public interface OnSuccessListener {
        void checkBelt();

        void checkBox();

        void choiceNoodles();

        void checkNoodles();

        void sendToSoupArea();

        void addSoup();
        //重复选米粉2
        void choiceNoodlesSecond();
        //检查米粉是否到达
        void checkNoodlesSecond();

        void sendToHeat();

        void startHeat();

        //重复选米粉3
        void choiceNoodlesThird();
        //检查米粉是否到达
        void checkNoodlesThird();

        void sendResycle();

        void closeDoor();

        //-----轮询部分-----
        void recycleStartHeat();

        void recycleChoiceNoodles();

        void recycleCheckNoodles();

        void recycleSendToDoor();

        void recycleSupplyPackage();

        void recycleOpenDoor();
        //-----------------

        void sendToDoor();

        void supplyPackage();

        void openDoor();
    }

    public void setOnFailureListener(OnFailureListener onFailureListener) {
        mOnFailureListener = onFailureListener;
    }

    private OnFailureListener mOnFailureListener;

    public interface OnFailureListener {
        void checkBelt();

        void checkBox();

        void choiceNoodles(int errorCode);

        void checkNoodles();

        void sendToSoupArea(int errorCode);

        void addSoup(int errorCode);
        //重复选米粉2
        void choiceNoodlesSecond(int errorCode);
        //检查米粉2是否到达
        void checkNoodlesSecond();

        void sendToHeat(int errorCode);

        void startHeat(int errorCode);

        //重复选米粉3
        void choiceNoodlesThird(int errorCode);
        //检查米粉3是否到达
        void checkNoodlesThird();

        void sendResycle(int errorCode);

        void closeDoor(int errorCode);
        //-----轮询部分-----
        void recycleStartHeat(int errorCode);

        void recycleChoiceNoodles(int errorCode);

        void recycleCheckNoodles();

        void recycleSendToDoor(int errorCode);

        void recycleSupplyPackage(int errorCode);

        void recycleOpenDoor(int errorCode);
        //-----------------
        void sendToDoor(int errorCode);

        void supplyPackage(int errorCode);

        void openDoor(int errorCode);
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
