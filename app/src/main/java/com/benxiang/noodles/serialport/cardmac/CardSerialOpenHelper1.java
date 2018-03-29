package com.benxiang.noodles.serialport.cardmac;


import com.benxiang.noodles.serialport.ComBean;
import com.benxiang.noodles.serialport.SerialHelper;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

import static com.benxiang.noodles.serialport.cardmac.CardSerialOpenHelper.MAKE_NOODLES;

/**
 * Created by Zeqiang Fang on 2017/8/24.
 */
public class CardSerialOpenHelper1 {

    private static CardSerialOpenHelper1 mCardSerialUtil;

    private SerialHelper mCardSerialHelper;
//    private static int mMakeOrRecycleNoodles;

    private volatile int mMakeOrSeasonPackage = MAKE_NOODLES;

    private CardSerialOpenHelper1(){
        mCardSerialHelper = new SerialHelper("/dev/ttyS1",9600) {
            @Override
            protected void onDataReceived(ComBean ComRecData) {
//                Timber.e("com数量：" + ComRecData.iSize + ">>>>>" + MyFunc.ByteArrToHex(ComRecData.bRec));
//                ComRecData.makeOrRecycleNoodles = mMakeOrRecycleNoodles;
                ComRecData.makeOrRecycleNoodles = mMakeOrSeasonPackage;
                EventBus.getDefault().post(ComRecData);
            }

            @Override
            protected void onOverTime() {
                if (mTimeoutListener!=null){
                    mTimeoutListener.onTimeout();
                }
            }
        };
        try {
            mCardSerialHelper.open();
//            Toast.makeText(HotelApplication.getContext(), "发卡机串口打开成功", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
//            Toast.makeText(HotelApplication.getContext(), "发卡机串口打开失败", Toast.LENGTH_SHORT).show();
        }
    }

    public static CardSerialOpenHelper1 getIns() {
        if (mCardSerialUtil == null) {
            synchronized (CardSerialOpenHelper1.class) {
                if (mCardSerialUtil == null) {
//                    mMakeOrRecycleNoodles = makeOrRecycleNoodles;
                    mCardSerialUtil = new CardSerialOpenHelper1();
                }
            }
        }
        return mCardSerialUtil;
    }

    public CardSerialOpenHelper1 startCardSerial() {
        if (!mCardSerialHelper.isOpen()) {
            try {
                mCardSerialHelper.open();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return this;
    }

    public SerialHelper getCardSerialHelper() {
        return mCardSerialHelper;
    }

    public boolean isOpen() {
        return mCardSerialHelper.isOpen();
    }

    public void closeCardSerial() {
        if (mCardSerialHelper.isOpen()) {
            mCardSerialHelper.close();
            mCardSerialHelper = null;
        }
    }

    public void setMakeOrRecycleNoodles(int makeOrRecycleNoodles) {
        mMakeOrSeasonPackage = makeOrRecycleNoodles;
    }

    private TimeoutListener mTimeoutListener;
    public void setTimeoutListener(TimeoutListener timeoutListener){
        mTimeoutListener = timeoutListener;
    }

    public interface TimeoutListener {
        void onTimeout();
    }

}
