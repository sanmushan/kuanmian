package com.benxiang.noodles.entrance;

import android.os.Looper;
import android.util.Log;

import com.benxiang.noodles.BuildConfig;
import com.benxiang.noodles.contants.Constants;
import com.benxiang.noodles.data.DBNoodleHelper;
import com.benxiang.noodles.data.table.RiceOrderND;
import com.benxiang.noodles.serialport.ComBean;
import com.benxiang.noodles.serialport.SerialHelper;
import com.benxiang.noodles.serialport.bean.ErrorEvent;
import com.benxiang.noodles.serialport.cardmac.CardSerialOpenHelper;
import com.benxiang.noodles.serialport.cardmac.CardSerialOpenHelper1;
import com.benxiang.noodles.serialport.cardmac.ManyToManySerialReceiver;
import com.benxiang.noodles.serialport.cardmac.NoodlesSerialCommand;
import com.benxiang.noodles.utils.CalNoodlesNum;
import com.blankj.utilcode.util.LogUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import timber.log.Timber;

import static com.benxiang.noodles.serialport.cardmac.CardSerialOpenHelper.MAKE_NOODLES;

/**
 * Created by Zeqiang Fang on 2017/9/1.
 */
public abstract class ManyToManyNoodlesUtil {
    private ManyToManySerialReceiver mManyToManySerialReceiver;
    //模组2
    private SerialHelper mCardSerialHelper;
    //模组1
    private SerialHelper mCardSerialHelper1;
    private ManyToManySerialReceiver.OnSuccessListener mSuccessListener;
    private ManyToManySerialReceiver.OnFailureListener mFailureListener;
    private int errorCount = 0;//出错的次数
    //    private int mNumberNoodles;
    private int[] mNumberNoodlesArr;//做米粉的米粉号
    private List<String> mNoodleStateArr;//做米粉的口味:如：卤蛋口味，酸辣包口味
    private boolean isContinue = false;//用于多单多碗，判断循环部分是否都已经执行完：可删除
    private int mRepeatTimes;//初始值=米粉的数量减1,表示循环部分还需要执行的次数，做的米粉的数量大于等于4会重复选择米粉
    /**
     * 加热命令的索引，初始值：1,用于获取一碗或者两碗的加热或加汤指令
     */
    private int mIndexHeat;
    private int mIndexRepeatChoice;//重复选择米粉的次数，用于循环区，初始值：3，若米粉的数量大于4，则在循环区会发送取米粉的指令
    private int mIndexFinishNumberNoodles;//初始值：0，表示已完成的米粉的索引，每完成一碗，索引加1
    private List<RiceOrderND> mNoodleEventDataArr;

    private boolean mReading;//判断读数据库的线程是否在读取
    private ReadDBThread mReadDBThread;

    protected ManyToManyNoodlesUtil() {

        mCardSerialHelper = CardSerialOpenHelper.getIns().getCardSerialHelper();
        CardSerialOpenHelper.getIns().setTimeoutListener(new CardSerialOpenHelper.TimeoutListener() {
            @Override
            public void onTimeout() {
                dealWithError(500+"");
            }
        });
        mCardSerialHelper1 = CardSerialOpenHelper1.getIns().getCardSerialHelper();
        CardSerialOpenHelper1.getIns().setTimeoutListener(new CardSerialOpenHelper1.TimeoutListener() {
            @Override
            public void onTimeout() {
                dealWithError(500+"");
            }
        });
        mManyToManySerialReceiver = ManyToManySerialReceiver.getIns();
        handleSuccess();
        handleFailure();
    }


    //获取做面的数据，重置变量
    public void setNoodleEventDatas(){

        if (mNoodleEventDataArr.size() == 0){
            Timber.e("没有要做的面,轮询检测");
            return;
        }
        mNumberNoodlesArr = CalNoodlesNum.getNoodleNos(mNoodleEventDataArr);
        for (int i=0;i<mNumberNoodlesArr.length;i++){
            DBNoodleHelper.upateNoodleSign(mNumberNoodlesArr[i], Constants.SIGN_NOT_DO, Constants.SIGN_DOING);
        }
        mNoodleStateArr = CalNoodlesNum.getNoodleStates(mNoodleEventDataArr);
        //后缀重复部分要执行的次数
        mRepeatTimes = mNumberNoodlesArr.length - 1;
        mIndexHeat = 1;
        mIndexRepeatChoice =3;
        mIndexFinishNumberNoodles=0;
        mIsChoiceFinish = false;
    }


    public void addNoodleEventDatas(int indexNumberNoodles){
        List<RiceOrderND> newAddNoodleEventDataArr = DBNoodleHelper.queryWithoutNoodle(Constants.SIGN_NOT_DO);

        if (newAddNoodleEventDataArr.size() <= 0){
            Timber.e("-----------------------------------------------------------------"+"新增数量为"+newAddNoodleEventDataArr.size()+"，不用拼接");
            return;
        }

        //TODO LIN若前一碗没有被选中，就不继续进行多单操作，等下一单
        if (mNoodleEventDataArr.get(indexNumberNoodles-1).isSelected == false){
            Timber.e("-----------------------------------------------------------------"+indexNumberNoodles+"没有选中，不用拼接"+newAddNoodleEventDataArr.size());
            return;
        }

        List<RiceOrderND> cacheNoodleEventDataArr = mNoodleEventDataArr;
        for (int i=0; i<newAddNoodleEventDataArr.size(); i++){
            cacheNoodleEventDataArr.add(newAddNoodleEventDataArr.get(i));
        }
        if (cacheNoodleEventDataArr.size() <  (indexNumberNoodles+1)){
            return;
        }
        //若前一碗没有被选中，就不继续进行多单操作，等下一单
//        if (cacheNoodleEventDataArr.get(indexNumberNoodles-1).isSelected == false){
//            Timber.e("-----------------------------------------------------------------"+indexNumberNoodles+"没有拼接"+newAddNoodleEventDataArr.size());
//            return;
//        }else {
//            for (int j = 0; j < newAddNoodleEventDataArr.size(); j++) {
//                DBNoodleHelper.upateNoodleSign(newAddNoodleEventDataArr.get(j).noodleNo,Constants.SIGN_DOING);
//            }
//            cacheNoodleEventDataArr.get(indexNumberNoodles).isSelected = true;
//            Timber.e("-----------------------------------------------------------------"+indexNumberNoodles+"有拼接"+newAddNoodleEventDataArr.size());
//        }

        for (int j = 0; j < newAddNoodleEventDataArr.size(); j++) {
            DBNoodleHelper.upateNoodleSign(newAddNoodleEventDataArr.get(j).noodleNo, Constants.SIGN_NOT_DO, Constants.SIGN_DOING);
            newAddNoodleEventDataArr.get(j).isSelected = true;
        }

        Timber.e("-----------------------------------------------------------------"+indexNumberNoodles+"有拼接"+newAddNoodleEventDataArr.size());

        mNoodleEventDataArr = cacheNoodleEventDataArr;
        mNumberNoodlesArr = CalNoodlesNum.getNoodleNos(mNoodleEventDataArr);
        mNoodleStateArr = CalNoodlesNum.getNoodleStates(mNoodleEventDataArr);

        mRepeatTimes += newAddNoodleEventDataArr.size();
    }

    public void startCheckBelt() {
        setNoodleEventDatas();
        logStep("===========================================");
        sendSerialData(NoodlesSerialCommand.CHECK_BELT);
    }

    public void startReadDB() {
        stopReadDB();
        mReading = true;
        mReadDBThread = new ReadDBThread();
        mReadDBThread.start();
    }

    public void stopReadDB() {
        if (mReadDBThread != null) {
            mReadDBThread.stopRead();
            mReadDBThread = null;
        }
        mReading = false;
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    int i=7;
    class ReadDBThread extends Thread {
        public void run() {
            Looper.prepare();
            while (mReading) {
//                Timber.e("重新执行");
//                List<RiceOrderND> list = new ArrayList<RiceOrderND>();
//                list.removeAll(null);
//
//                RiceOrderND riceOrderND = new RiceOrderND();
//                riceOrderND.noodleNo = i++;
//                riceOrderND.noodleState = NoodleNameConstants.LUDAN;
//                list.add(riceOrderND);
//
//                RiceOrderND riceOrderND1 = new RiceOrderND();
//                riceOrderND1.noodleNo = 18;
//                riceOrderND1.noodleState = NoodleNameConstants.QINGTANG;
//                list.add(riceOrderND1);
//
//                mNoodleEventDataArr = list;
                //查询RiceOrderND中还没做的面
                mNoodleEventDataArr = DBNoodleHelper.queryWithoutNoodle(Constants.SIGN_NOT_DO);
                if (mNoodleEventDataArr == null || mNoodleEventDataArr.size() == 0){
                    try {
                        sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Timber.e(">>>>>>>>>>>>>>>>>>>>>>>>>>没面需要做，需要做的面的数量为0");
                }else {
                    Timber.e(">>>>>>>>>>>>>>>>>>>>>>>>>>有面需要做，需要做的面的数量为:"+mNoodleEventDataArr.size());
                    //TODO 控制蒸汽开关，true开，false关
                    mReading = false;
                    if (BuildConfig.HAS_STEAM){
                        startOpenSteam();
                    }else {
                        startCheckBelt();
                    }
                }
            }
        }

        public void stopRead() {
            mReading = false;
            try {
                join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void setIsChoiceFinish(boolean isChoiceFinish) {
        LogUtils.file("===========================================选择米粉完成，不循环去等待");
        mIsChoiceFinish = isChoiceFinish;
    }

    public void openDoor() {
        sendSerialData(NoodlesSerialCommand.OPEN_DOOR);
    }

    public void sendToDoor() {
        int arrLength = mNumberNoodlesArr.length;
        if (mRepeatTimes > 0){
            count-=6;
            mManyToManySerialReceiver.setExecStep(14);
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
            }else {
                stopReadDB();
                startReadDB();
            }
        }

        //减少时间，同时发送两条指令，加热与取面同时进行
        if (mRepeatTimes>=2){
            if (mNumberNoodlesArr.length>= mIndexRepeatChoice){
                addNoodleEventDatas(mIndexRepeatChoice);
            }
        }
        if (mRepeatTimes >= 3) {
            ManyToManyNoodlesUtil.this.choiceNoodles(mNumberNoodlesArr[mIndexRepeatChoice++]);
        }else {
            mIsChoiceFinish = true;
        }
    }

    public void handleReceived(ComBean comBean) {
        mManyToManySerialReceiver.handleReceivedData(comBean);
    }

    public void recycle() {
        mManyToManySerialReceiver.setExecStep(0);

        mManyToManySerialReceiver.setOnFailureListener(null);
        mManyToManySerialReceiver.setOnSuccessListener(null);
        mSuccessListener = null;
        mFailureListener = null;
        mManyToManySerialReceiver.releaseListener();
        stopReadDB();
    }

    private int count = 0;
    private boolean mIsChoiceFinish = false;//用于倒计时，判断米粉是否选择成功
    private void handleSuccess() {
        mSuccessListener = new ManyToManySerialReceiver.OnSuccessListener() {

            @Override
            public void checkBelt() {
                logStep("皮带有空位");
                sendSerialData(NoodlesSerialCommand.CHECK_BOX);
            }

            @Override
            public void checkBox() {
                logStep("取料箱有空位");
//                sendSerialData1(NoodlesSerialCommand.CHOICE_NOODLES);
                mNoodleEventDataArr.get(0).isSelected = true;
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
                //TODO LIN 拼单 取第二碗面  减少时间，同时发送两条指令，加汤与取面同时进行
                addNoodleEventDatas(1);
                if (mNumberNoodlesArr.length>1){
                    mNoodleEventDataArr.get(1).isSelected = true;
                    ManyToManyNoodlesUtil.this.choiceNoodles(mNumberNoodlesArr[1]);
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
                //多单
//                addNoodleEventDatas(1);
                if (mNumberNoodlesArr.length>1){
//                    sendSerialData1(NoodlesSerialCommand.getChoiceNoodles(mNumberNoodlesArr[1]));
                    //少了选米粉的流程，直接检查米粉是否到达
                    int execStep = mManyToManySerialReceiver.getExecStep() + 1;
                    count+=1;
                    mManyToManySerialReceiver.setExecStep(execStep);
                    sendSerialData(NoodlesSerialCommand.CHECK_NOODLES);
                }else {
                    count=count+2;
                    mManyToManySerialReceiver.setExecStep(8);
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
//                sendSerialData(NoodlesSerialCommand.START_HEAT);
                if (mNumberNoodlesArr.length >= 2){
                    byte[] soupOrHeatCommand = CalNoodlesNum.getSoupOrHeatCommand(mNumberNoodlesArr[0],mNumberNoodlesArr[1],mNoodleStateArr.get(1));
                    sendSerialData(soupOrHeatCommand);
                }else {
                    byte[] soupOrHeatCommand = CalNoodlesNum.getSoupOrHeatCommand(mNumberNoodlesArr[0],mNoodleStateArr.get(0));
                    sendSerialData(soupOrHeatCommand);
                }
                //减少时间，同时发送两条指令，加热与取面同时进行
                if (mNumberNoodlesArr.length >= 2){
                    addNoodleEventDatas(2);
                }
                if (mNumberNoodlesArr.length >= 3){
                    mNoodleEventDataArr.get(2).isSelected = true;
                    ManyToManyNoodlesUtil.this.choiceNoodles(mNumberNoodlesArr[2]);
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

                //多单
//                addNoodleEventDatas(2);
                if (mNumberNoodlesArr.length >= 3){
//                    sendSerialData1(NoodlesSerialCommand.getChoiceNoodles(mNumberNoodlesArr[2]));
                    //少了选米粉的流程，直接检查米粉是否到达
                    int execStep = mManyToManySerialReceiver.getExecStep() + 1;
                    count+=1;
                    mManyToManySerialReceiver.setExecStep(execStep);
                    sendSerialData(NoodlesSerialCommand.CHECK_NOODLES);
                }else {
                    count=count+2;
                    mManyToManySerialReceiver.setExecStep(12);
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
                    mManyToManySerialReceiver.setExecStep(20);
                    sendSerialData(NoodlesSerialCommand.SEND_TO_DOOR);
                }

                //减少时间，同时发送两条指令，加热与取面同时进行
                if (mNumberNoodlesArr.length >= mIndexRepeatChoice){
                    addNoodleEventDatas(mIndexRepeatChoice);
                }
                if (mRepeatTimes >= 3) {
                    mNoodleEventDataArr.get(mIndexRepeatChoice).isSelected = true;
                    ManyToManyNoodlesUtil.this.choiceNoodles(mNumberNoodlesArr[mIndexRepeatChoice++]);
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
                //多单
//                addNoodleEventDatas(mIndexRepeatChoice);
                //做的米粉的数量大于等于4会重复选择米粉
                if (mRepeatTimes >= 3) {
                    logStep("循环启动加热完成");
//                    sendSerialData1(NoodlesSerialCommand.getChoiceNoodles(mNumberNoodlesArr[mIndexRepeatChoice++]));
                    //少了选米粉的流程，直接检查米粉是否到达
                    int execStep = mManyToManySerialReceiver.getExecStep() + 1;
                    count+=1;
                    mManyToManySerialReceiver.setExecStep(execStep);
                    sendSerialData(NoodlesSerialCommand.CHECK_NOODLES);
                }else {
                    logStep("循环启动加热完成");
                    count+=2;
                    mManyToManySerialReceiver.setExecStep(17);
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
                int execStep = mManyToManySerialReceiver.getExecStep()+1;
                mManyToManySerialReceiver.setExecStep(execStep);
                dropPackage(mNoodleEventDataArr.get(mIndexFinishNumberNoodles));
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
                RiceOrderND noodleEventData = mNoodleEventDataArr.get(mIndexFinishNumberNoodles);
                mIndexFinishNumberNoodles++;
//                DBNoodleHelper.deleteNoodle(noodleEventData);
                DBNoodleHelper.upateNoodleSign(noodleEventData.noodleNo, Constants.SIGN_DOING, Constants.SIGN_HAS_DONE);
                //TODO LIN 做面完成后回调，判断面是否被取走或回收
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
                int execStep = mManyToManySerialReceiver.getExecStep()+1;
                mManyToManySerialReceiver.setExecStep(execStep);
                dropPackage(mNoodleEventDataArr.get(mIndexFinishNumberNoodles));
            }

            @Override
            public void supplyPackage() {
                logStep("提供配料包完成");
                sendSerialData(NoodlesSerialCommand.OPEN_DOOR);
            }

            @Override
            public void openDoor() {
                logStep("打开取粉门完成");
                RiceOrderND noodleEventData = mNoodleEventDataArr.get(mIndexFinishNumberNoodles);
                mIndexFinishNumberNoodles++;
                DBNoodleHelper.upateNoodleSign(noodleEventData.noodleNo, Constants.SIGN_DOING, Constants.SIGN_HAS_DONE);
                //TODO LIN 全部做完
                onAllFinish(noodleEventData);
                count=0;
            }
        };
        mManyToManySerialReceiver.setOnSuccessListener(mSuccessListener);
    }

    private void handleFailure() {
        mFailureListener = new ManyToManySerialReceiver.OnFailureListener() {

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
                        int execStep = mManyToManySerialReceiver.getExecStep();
                        mManyToManySerialReceiver.setExecStep(execStep-1);
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
            //第一次执行加汤指令返回错误码
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
                        int execStep = mManyToManySerialReceiver.getExecStep();
                        mManyToManySerialReceiver.setExecStep(execStep-1);
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
                handleError(""+errorCode);
            }

            @Override
            public void choiceNoodlesThird(int errorCode) {
                if (errorCode == 61){//运动没到位
                    int numberNoodlesMax = CalNoodlesNum.getNumberNoodlesMax(mNumberNoodlesArr[2]);
                    mNumberNoodlesArr[2]++;
                    if (mNumberNoodlesArr[2]<=numberNoodlesMax) {
                        int execStep = mManyToManySerialReceiver.getExecStep();
                        mManyToManySerialReceiver.setExecStep(execStep-1);
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
                handleError(""+errorCode);
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
                        int execStep = mManyToManySerialReceiver.getExecStep();
                        mManyToManySerialReceiver.setExecStep(execStep-1);
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
        mManyToManySerialReceiver.setOnFailureListener(mFailureListener);
    }

    //模组2发送数据
    private void sendSerialData(byte[] data) {

        if (errorCount > 5) {
            mManyToManySerialReceiver.setExecStep(0);
            count=0;
            errorCount=0;
            Timber.e("count="+count);
//            tooMuchError();
            handleError("501");
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
            mManyToManySerialReceiver.setExecStep(0);
            count=0;
            errorCount=0;
            Timber.e("count="+count);
//            tooMuchError();
            handleError("501");
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
        mManyToManySerialReceiver.setExecStep(0);
        errorCount=0;
        count=0;
        stopReadDB();
        mIsChoiceFinish = false;
        dealWithError(info);
    }

    protected abstract void dealWithError(String info);

    protected abstract void onFinish(RiceOrderND riceOrderND);

    protected abstract void onAllFinish(RiceOrderND riceOrderND);

//    protected abstract void dropPackage(String noodlesName);

    protected abstract void dropPackage(RiceOrderND riceOrderND);

    protected abstract void choiceNoodles(int noodlesNo);

    protected abstract void startOpenSteam();

    private void logStep(String msg) {
        Timber.e(">>>>>>>>>>>> 步骤 " + count + "  : " +msg);
        LogUtils.file(">>>>>>>>>>>> 步骤 " + count + "  : " +msg);
        count++;
    }

    private void logPrint(String msg) {
        Timber.e(msg);
        LogUtils.file(msg);
    }

    private void currentThreadSleep(int time){
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
