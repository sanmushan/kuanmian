package com.benxiang.noodles.entrance;

import android.util.Log;

import com.benxiang.noodles.data.noodle.NoodleEventData;
import com.benxiang.noodles.serialport.ComBean;
import com.benxiang.noodles.serialport.SerialHelper;
import com.benxiang.noodles.serialport.bean.ErrorEvent;
import com.benxiang.noodles.serialport.cardmac.CardSerialOpenHelper;
import com.benxiang.noodles.serialport.cardmac.CardSerialOpenHelper1;
import com.benxiang.noodles.serialport.cardmac.NoodlesSerialCommand;
import com.benxiang.noodles.serialport.cardmac.NoodlesSerialReceiver;
import com.benxiang.noodles.utils.CalNoodlesNum;
import com.blankj.utilcode.util.LogUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import timber.log.Timber;

import static com.benxiang.noodles.serialport.cardmac.CardSerialOpenHelper.MAKE_NOODLES;

/**
 * Created by Zeqiang Fang on 2017/9/1.
 */
public abstract class MakeNoodlesUtil {

    private NoodlesSerialReceiver mNoodlesSerialReceiver;
    //模组2
    private SerialHelper mCardSerialHelper;
    //模组1
    private SerialHelper mCardSerialHelper1;
    private NoodlesSerialReceiver.OnSuccessListener mCardSuccessListener;
    private NoodlesSerialReceiver.OnFailureListener mCardFailureListener;
    //出错的次数
    private int errorCount = 0;
    private int mNumberNoodles;
    //做米粉的米粉号
    private int[] mNumberNoodlesArr;
    //做米粉的口味:如：卤蛋口味，酸辣包口味
    private List<String> mNoodleStateArr;
    //用于多单多碗，判断循环部分是否都已经执行完：可删除
    private boolean isContinue = false;
    //初始值=米粉的数量减1,表示循环部分还需要执行的次数，做的米粉的数量大于等于4会重复选择米粉
    private int mRepeatTimes;
    /**
     * 加热命令的索引，初始值：1,用于获取一碗或者两碗的加热或加汤指令
     */
    private int mIndexHeat;
    //重复选择米粉的次数，用于循环区，初始值：3，若米粉的数量大于4，则在循环区会发送取米粉的指令
    private int mIndexRepeatChoice;
    //初始值：0，表示已完成的米粉的索引，每完成一碗，索引加1
    private int mIndexFinishNumberNoodles;

    private List<NoodleEventData> mNoodleEventDataArrs;
    protected MakeNoodlesUtil() {
//        CardSerialOpenHelper.getIns(RECYCLE_NOODLES).closeCardSerial();
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
        mNoodlesSerialReceiver = NoodlesSerialReceiver.getIns();
        handleSuccess();
        handleFailure();
    }


    private void setNumberNoodles(int[] numberNoodlesArr){
        mNumberNoodlesArr = numberNoodlesArr;
        //后缀重复部分要执行的次数
        mRepeatTimes = mNumberNoodlesArr.length - 1;
        mIndexHeat = 1;
        mIndexRepeatChoice =3;
    }


    public void setNoodleEventDatas(List<NoodleEventData> noodleEventDataArr){
        this.mNoodleEventDataArrs = noodleEventDataArr;

        mNumberNoodlesArr = CalNoodlesNum.getNoodleNoArr(noodleEventDataArr);
        mNoodleStateArr = CalNoodlesNum.getNoodleStateArr(noodleEventDataArr);
        //后缀重复部分要执行的次数
        mRepeatTimes = mNumberNoodlesArr.length - 1;
        mIndexHeat = 1;
        mIndexRepeatChoice =3;
        mIndexFinishNumberNoodles=0;
    }

    public void addNumberNoodles(int[] numberNoodlesArr){
        //将后面添加的数据与之前的数组合并
        int[] combine = new int[mNumberNoodlesArr.length + numberNoodlesArr.length];
        System.arraycopy(mNumberNoodlesArr, 0, combine, 0, mNumberNoodlesArr.length);
        System.arraycopy(numberNoodlesArr, 0, combine, mNumberNoodlesArr.length, numberNoodlesArr.length);
        setNumberNoodles(combine);
    }


    public void setIsChoiceFinish(boolean isChoiceFinish) {
        LogUtils.file("===========================================选择米粉完成，不循环去等待");
        mIsChoiceFinish = isChoiceFinish;
    }

    public void startCheckBelt() {
        logStep("===========================================");
        sendSerialData(NoodlesSerialCommand.CHECK_BELT);
    }

    public void openDoor() {
        sendSerialData(NoodlesSerialCommand.OPEN_DOOR);
    }

    public void sendToDoor() {
        int arrLength = mNumberNoodlesArr.length;
        if (mRepeatTimes > 0){
            count-=6;
            mNoodlesSerialReceiver.setExecStep(14);
            //arrLeng-1为重复的次数
            if ((arrLength-1) == mIndexHeat){
                byte[] soupOrHeatCommand = CalNoodlesNum.getSoupOrHeatCommand(mNumberNoodlesArr[mIndexHeat],mNoodleStateArr.get(mIndexHeat));//2
                mIndexHeat++;
                sendSerialData(soupOrHeatCommand);
            }else {
                byte[] soupOrHeatCommand = CalNoodlesNum.getSoupOrHeatCommand(mNumberNoodlesArr[mIndexHeat],mNumberNoodlesArr[mIndexHeat+1],mNoodleStateArr.get(mIndexHeat+1));//1,2
                mIndexHeat++;
                sendSerialData(soupOrHeatCommand);
            }
        }else {
            if (isContinue){
                sendSerialData(NoodlesSerialCommand.SEND_TO_DOOR);
                isContinue =false;
            }
        }

        //减少时间，同时发送两条指令，加热与取面同时进行
        if (mRepeatTimes >= 3) {
            MakeNoodlesUtil.this.choiceNoodles(mNumberNoodlesArr[mIndexRepeatChoice++]);
        }else {
            mIsChoiceFinish = true;
        }
    }

    public void handleReceived(ComBean comBean) {
        mNoodlesSerialReceiver.handleReceivedData(comBean);
    }

    public void recycle() {
        mNoodlesSerialReceiver.setOnFailureListener(null);
        mNoodlesSerialReceiver.setOnSuccessListener(null);
        mCardSuccessListener = null;
        mCardFailureListener = null;
        mNoodlesSerialReceiver.releaseListener();
    }

    private int count = 0;
    private boolean mIsChoiceFinish = false;//用于倒计时，判断米粉是否选择成功
    private void handleSuccess() {
        mCardSuccessListener = new NoodlesSerialReceiver.OnSuccessListener() {

            @Override
            public void checkBelt() {
                logStep("皮带有空位");
                sendSerialData(NoodlesSerialCommand.CHECK_BOX);
            }

            @Override
            public void checkBox() {
                logStep("取料箱有空位");
                boolean isSendSuccess = false;
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
                byte[] soupOrHeatCommand = CalNoodlesNum.getSoupOrHeatCommand(mNumberNoodlesArr[0],mNoodleStateArr.get(0));
                sendSerialData(soupOrHeatCommand);
                //减少时间，同时发送两条指令，加汤与取面同时进行
                if (mNumberNoodlesArr.length>1){
                    MakeNoodlesUtil.this.choiceNoodles(mNumberNoodlesArr[1]);
                }else {
                    mIsChoiceFinish = true;
                }
            }

            @Override
            public void addSoup() {
                logStep("加汤完成");
                //保存变量，用于判断是否选择米粉完成
                while (!mIsChoiceFinish){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                mIsChoiceFinish = false;

                if (mNumberNoodlesArr.length>1){
                    //少了选米粉的流程，直接检查米粉是否到达  //TODO LIN 跳过280的步骤，因为上面已经做出了这个指令256行
                    int execStep = mNoodlesSerialReceiver.getExecStep() + 1;
                    count+=1;
                    mNoodlesSerialReceiver.setExecStep(execStep);
                    sendSerialData(NoodlesSerialCommand.CHECK_NOODLES);
                }else {
                    count=count+2;
                    mNoodlesSerialReceiver.setExecStep(8);
                    sendSerialData(NoodlesSerialCommand.SEND_TO_HEAT_AREA);
                }
            }

            //--------------------------------------------------------------重复选择米粉2--------------------------------------------------------------步骤7
            @Override
            public void choiceNoodlesSecond() {
                logStep("选择米粉2完成");
                sendSerialData(NoodlesSerialCommand.CHECK_NOODLES);

            }

            @Override
            public void checkNoodlesSecond() {
                logStep("米粉2到达完成");
                sendSerialData(NoodlesSerialCommand.SEND_TO_SOUP_AREA);
            }
            //---------------------------------------------------------------------------------------------------------------------------------------
            @Override
            public void sendToHeat() {
                logStep("送到加热区完成");
                if (mNumberNoodlesArr.length >= 2){
                    byte[] soupOrHeatCommand = CalNoodlesNum.getSoupOrHeatCommand(mNumberNoodlesArr[0],mNumberNoodlesArr[1],mNoodleStateArr.get(1));
                    sendSerialData(soupOrHeatCommand);
                }else {
                    byte[] soupOrHeatCommand = CalNoodlesNum.getSoupOrHeatCommand(mNumberNoodlesArr[0],mNoodleStateArr.get(0));
                    sendSerialData(soupOrHeatCommand);
                }
                //减少时间，同时发送两条指令，加热与取面同时进行
                if (mNumberNoodlesArr.length >= 3){
                    MakeNoodlesUtil.this.choiceNoodles(mNumberNoodlesArr[2]);
                }else {
                    mIsChoiceFinish = true;
                }
            }

            @Override
            public void startHeat() {
                logStep("启动加热完成");

                //保存变量，用于判断是否选择米粉完成
                while (!mIsChoiceFinish){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                mIsChoiceFinish = false;

                if (mNumberNoodlesArr.length >= 3){
//                    sendSerialData1(NoodlesSerialCommand.getChoiceNoodles(mNumberNoodlesArr[2]));
                    //少了选米粉的流程，直接检查米粉是否到达
                    int execStep = mNoodlesSerialReceiver.getExecStep() + 1;
                    count+=1;
                    mNoodlesSerialReceiver.setExecStep(execStep);
                    sendSerialData(NoodlesSerialCommand.CHECK_NOODLES);
                }else {
                    count=count+2;
                    mNoodlesSerialReceiver.setExecStep(12);
                    sendSerialData(NoodlesSerialCommand.SEND_TO_RECYLE);
                }
            }
            //--------------------------------------------------------------重复选择选粉3-------------------------------------------------------------- 步骤11
            @Override
            public void choiceNoodlesThird() {
                logStep("选择米粉3完成");
                sendSerialData(NoodlesSerialCommand.CHECK_NOODLES);
            }

            @Override
            public void checkNoodlesThird() {
                logStep("米粉3到达完成");
                sendSerialData(NoodlesSerialCommand.SEND_TO_SOUP_AREA);
            }
            //---------------------------------------------------------------------------------------------------------------------------------------
            @Override
            public void sendResycle() {
                logStep("送到回收区完成");
                sendSerialData(NoodlesSerialCommand.CLOSE_DOOR);
            }

            @Override
            public void closeDoor() {
                logStep("关闭取粉门完成");
                if (mNumberNoodlesArr.length >=2){
                    if(mNumberNoodlesArr.length == 2){
                        Timber.e("mIndexHeat的值:"+mIndexHeat);
                        byte[] soupOrHeatCommand = CalNoodlesNum.getSoupOrHeatCommand(mNumberNoodlesArr[mIndexHeat],mNoodleStateArr.get(mIndexHeat));//1
                        mIndexHeat++;
                        sendSerialData(soupOrHeatCommand);
                    }else if(mNumberNoodlesArr.length >= 3){
                        byte[] soupOrHeatCommand = CalNoodlesNum.getSoupOrHeatCommand(mNumberNoodlesArr[mIndexHeat],mNumberNoodlesArr[mIndexHeat+1],mNoodleStateArr.get(mIndexHeat));//1,2
                        mIndexHeat++;
                        sendSerialData(soupOrHeatCommand);
                    }
                }else {
                    count+=6;
                    mNoodlesSerialReceiver.setExecStep(20);
                    sendSerialData(NoodlesSerialCommand.SEND_TO_DOOR);
                }

                //减少时间，同时发送两条指令，加热与取面同时进行
                if (mRepeatTimes >= 3) {
                    MakeNoodlesUtil.this.choiceNoodles(mNumberNoodlesArr[mIndexRepeatChoice++]);
                }else {
                    mIsChoiceFinish = true;
                }
            }

            //--------------------------------------------------------------执行轮询部分：启动加热，送成品到取粉区，提供调料包，打开取粉门-------------------------------------------------------------- 步骤15
            @Override
            public void recycleStartHeat() {
                //保存变量，用于判断是否选择米粉完成
                while (!mIsChoiceFinish){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                mIsChoiceFinish = false;

                //做的米粉的数量大于等于4会重复选择米粉
                if (mRepeatTimes >= 3) {
                    logStep("循环启动加热完成");
//                    sendSerialData1(NoodlesSerialCommand.getChoiceNoodles(mNumberNoodlesArr[mIndexRepeatChoice++]));
                    //mRepeatTimes=3   3            mRepeatTimes=4   3       mRepeatTimes=5   3
                    //                              mRepeatTimes=3   4       mRepeatTimes=4   4
                    //                                                       mRepeatTimes=3   5
                    //少了选米粉的流程，直接检查米粉是否到达
                    int execStep = mNoodlesSerialReceiver.getExecStep() + 1;
                    count+=1;
                    mNoodlesSerialReceiver.setExecStep(execStep);
                    sendSerialData(NoodlesSerialCommand.CHECK_NOODLES);
                }else {
                    logStep("循环启动加热完成");
                    count+=2;
                    mNoodlesSerialReceiver.setExecStep(17);
                    sendSerialData(NoodlesSerialCommand.SEND_TO_DOOR);
                }
            }

            @Override
            public void recycleChoiceNoodles() {
                logStep("循环选择米粉完成");
                sendSerialData(NoodlesSerialCommand.CHECK_NOODLES);
            }

            @Override
            public void recycleCheckNoodles() {
                logStep("循环米粉到达完成");
                sendSerialData(NoodlesSerialCommand.SEND_TO_DOOR);
            }

            @Override
            public void recycleSendToDoor() {
                logStep("循环送到取粉门完成");
//                byte[] choicePackage = NoodlesSerialCommand.getChoicePackage(mIndexFinishNumberNoodles);
//                sendSerialData1(choicePackage);
                count++;
                int execStep = mNoodlesSerialReceiver.getExecStep()+1;
                mNoodlesSerialReceiver.setExecStep(execStep);
                dropPackage(mNoodleEventDataArrs.get(mIndexFinishNumberNoodles));
            }

            @Override
            public void recycleSupplyPackage() {
                logStep("循环提供配料包完成");
                sendSerialData(NoodlesSerialCommand.OPEN_DOOR);
            }

            @Override
            public void recycleOpenDoor() {
                mRepeatTimes--;
                logStep("循环打开取粉门完成");
                isContinue = true;
                //我的修改
                NoodleEventData noodleEventData = mNoodleEventDataArrs.get(mIndexFinishNumberNoodles);
                mIndexFinishNumberNoodles++;
                onFinish(noodleEventData);
//                onFinish(mNumberNoodlesArr[mIndexFinishNumberNoodles++]);
            }
            //--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------


            @Override
            public void sendToDoor() {
                logStep("送到取粉门完成");
//                byte[] choicePackage = NoodlesSerialCommand.getChoicePackage(1);
//                sendSerialData1(choicePackage);
//                sendSerialData1(NoodlesSerialCommand.SUPPLY_PACKAGE);
                count++;
                int execStep = mNoodlesSerialReceiver.getExecStep()+1;
                mNoodlesSerialReceiver.setExecStep(execStep);
//                dropPackage(mNoodleStateArr.get(mIndexFinishNumberNoodles));
                dropPackage(mNoodleEventDataArrs.get(mIndexFinishNumberNoodles));
            }

            @Override
            public void supplyPackage() {
                logStep("提供配料包完成");
                sendSerialData(NoodlesSerialCommand.OPEN_DOOR);
            }

            @Override
            public void openDoor() {
                logStep("打开取粉门完成");
                //我的修改
                NoodleEventData noodleEventData = mNoodleEventDataArrs.get(mIndexFinishNumberNoodles);
                mIndexFinishNumberNoodles++;
                onAllFinish(noodleEventData);
                count=0;
            }
        };
        mNoodlesSerialReceiver.setOnSuccessListener(mCardSuccessListener);
    }

    private void handleFailure() {
        mCardFailureListener = new NoodlesSerialReceiver.OnFailureListener() {

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
                Timber.e("错误码是:"+errorCode);
                if (errorCode == 61){//运动没到位
                    int numberNoodlesMax = CalNoodlesNum.getNumberNoodlesMax(mNumberNoodlesArr[0]);
                    Timber.e("重庆小面A类型的最大米粉号:"+numberNoodlesMax);
                    mNumberNoodlesArr[0]++;
                    if (mNumberNoodlesArr[0]<=numberNoodlesMax) {
                        int execStep = mNoodlesSerialReceiver.getExecStep();
                        mNoodlesSerialReceiver.setExecStep(execStep-1);
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
            public void choiceNoodlesSecond(int errorCode) {
                if (errorCode == 61){//运动没到位
                    int numberNoodlesMax = CalNoodlesNum.getNumberNoodlesMax(mNumberNoodlesArr[1]);
                    mNumberNoodlesArr[1]++;
                    if (mNumberNoodlesArr[1]<=numberNoodlesMax) {
                        int execStep = mNoodlesSerialReceiver.getExecStep();
                        mNoodlesSerialReceiver.setExecStep(execStep-1);
                        sendSerialData1(NoodlesSerialCommand.getChoiceNoodles(mNumberNoodlesArr[1]));
                    }else {
                        handleError(""+errorCode);
                    }
                }else {
                    handleError(""+errorCode);
                }
            }

            @Override
            public void checkNoodlesSecond() {
                sendSerialData(NoodlesSerialCommand.CHECK_NOODLES);
                errorCount++;
            }

            @Override
            public void sendToHeat(int errorCode) {
                handleError(""+errorCode);
            }

            @Override
            public void startHeat(int errorCode) {
//                if (errorCount>3){
                    handleError(""+errorCode);
//                    return;
//                }else{
//                    sendSerialData(NoodlesSerialCommand.START_HEAT);
//                }
//                errorCount++;
            }

            @Override
            public void choiceNoodlesThird(int errorCode) {
                if (errorCode == 61){//运动没到位
                    int numberNoodlesMax = CalNoodlesNum.getNumberNoodlesMax(mNumberNoodlesArr[2]);
                    mNumberNoodlesArr[2]++;
                    if (mNumberNoodlesArr[2]<=numberNoodlesMax) {
                        int execStep = mNoodlesSerialReceiver.getExecStep();
                        mNoodlesSerialReceiver.setExecStep(execStep-1);
                        sendSerialData1(NoodlesSerialCommand.getChoiceNoodles(mNumberNoodlesArr[2]));
                    }else {
                        handleError(""+errorCode);
                    }
                }else {
                    handleError(""+errorCode);
                }
            }

            @Override
            public void checkNoodlesThird() {
                sendSerialData(NoodlesSerialCommand.CHECK_NOODLES);
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
            //--------------------------------------------------------------执行轮询部分：启动加热，送成品到取粉区，提供调料包，打开取粉门-------------------------------------------------------------- 步骤15
            //加热从第三碗开始加热，选择米粉从第四碗
            @Override
            public void recycleStartHeat(int errorCode) {
//                if (errorCount>3){
                    handleError(""+errorCode);
//                    return;
//                }else{
//                    sendSerialData(NoodlesSerialCommand.START_HEAT);
//                }
//                errorCount++;
            }

            @Override
            public void recycleChoiceNoodles(int errorCode) {
                if (errorCode == 61){//运动没到位
                    int mIndexRepeatChoiceNew = mIndexRepeatChoice;
                    if (mIndexRepeatChoice>=3){
                        mIndexRepeatChoiceNew = mIndexRepeatChoice-1;
                    }
                    int numberNoodlesMax = CalNoodlesNum.getNumberNoodlesMax(mNumberNoodlesArr[mIndexRepeatChoiceNew]);
                    mNumberNoodlesArr[mIndexRepeatChoiceNew]++;
                    if (mNumberNoodlesArr[mIndexRepeatChoiceNew]<=numberNoodlesMax) {
                        int execStep = mNoodlesSerialReceiver.getExecStep();
                        mNoodlesSerialReceiver.setExecStep(execStep-1);
                        sendSerialData1(NoodlesSerialCommand.getChoiceNoodles(mNumberNoodlesArr[mIndexRepeatChoiceNew]));
                    }else {
                        handleError(""+errorCode);
                    }
                }else {
                    handleError(""+errorCode);
                }
            }

            @Override
            public void recycleCheckNoodles() {
                sendSerialData(NoodlesSerialCommand.CHECK_NOODLES);
                errorCount++;
            }

            @Override
            public void recycleSendToDoor(int errorCode) {
                handleError(""+errorCode);
            }

            @Override
            public void recycleSupplyPackage(int errorCode) {
                handleError(""+errorCode);
            }

            @Override
            public void recycleOpenDoor(int errorCode) {
                handleError(""+errorCode);
            }
            //--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
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
        mNoodlesSerialReceiver.setOnFailureListener(mCardFailureListener);
    }

    //模组2发送数据
    private void sendSerialData(byte[] data) {
        if (errorCount > 5) {
            mNoodlesSerialReceiver.setExecStep(0);
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

        if (mCardSerialHelper!=null && mCardSerialHelper.isOpen()) {
            mCardSerialHelper.send(data);
//            Timber.e("米粉机串口正常运行");
//            ErrorEvent errorEvent = new ErrorEvent("米粉机串口正常运行");
//            EventBus.getDefault().post(errorEvent);
        }else {
            Timber.e("米粉机串口未能正常运行");
            ErrorEvent errorEvent = new ErrorEvent("米粉机串口未能正常运行，请到相应位置办理手续!");
            EventBus.getDefault().post(errorEvent);
        }
    }

    //模组1发送数据
    private void sendSerialData1(byte[] data) {
        if (errorCount > 5) {
            mNoodlesSerialReceiver.setExecStep(0);
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

        CardSerialOpenHelper1.getIns().setMakeOrRecycleNoodles(MAKE_NOODLES);

        if (mCardSerialHelper1!=null && mCardSerialHelper1.isOpen()) {
            mCardSerialHelper1.send(data);
        }else {
            ErrorEvent errorEvent = new ErrorEvent("米粉机串口未能正常运行，请到前台办理手续!");
            EventBus.getDefault().post(errorEvent);
        }
    }

    private void handleError(String info){
        mNoodlesSerialReceiver.setExecStep(0);
        errorCount=0;
        count=0;
        mIsChoiceFinish = false;
        dealWithError(info);
    }

    protected abstract void dealWithError(String info);

    protected abstract void onFinish(NoodleEventData noodleEventData);


    protected abstract void onAllFinish(NoodleEventData noodleEventData);
    protected void tooMuchError() {
    }

    protected abstract void dropPackage(NoodleEventData noodleEventData);

//    protected abstract void dropPackage(String noodlesName);

    protected abstract void choiceNoodles(int noodlesNo);

    private void logStep(String msg) {
        Timber.e(">>>>>>>>>>>> 步骤 " + count + "  : " +msg);
        LogUtils.file(">>>>>>>>>>>> 步骤 " + count + "  : " +msg);
        count++;
    }
}
