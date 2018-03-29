package com.benxiang.noodles.serialport.cardmac;


import com.benxiang.noodles.serialport.ComBean;
import com.benxiang.noodles.serialport.SerialHelper;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

/**
 * Created by Zeqiang Fang on 2017/8/24.
 */
public class CardSerialOpenHelper {

    public static final int MAKE_NOODLES = 101;
    public static final int RECYCLE_NOODLES = 102;
    public static final int SEASONING_PACKAGE = 103;
    public static final int WRITE_FORMULA = 104;
    public static final int BOTTLED_WATER = 105;

    private volatile int mMakeOrRecycleNoodles = MAKE_NOODLES;

    private static CardSerialOpenHelper mCardSerialUtil;

    private SerialHelper mCardSerialHelper;

    private CardSerialOpenHelper(){

        mCardSerialHelper = new SerialHelper("/dev/ttyS3",9600) {
            @Override
            protected void onDataReceived(ComBean ComRecData) {
//                Timber.e("com数量：" + ComRecData.iSize + ">>>>>" + MyFunc.ByteArrToHex(ComRecData.bRec));
//                ComRecData.makeOrRecycleNoodles = mMakeOrRecycleNoodles;
                ComRecData.makeOrRecycleNoodles = mMakeOrRecycleNoodles;
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static CardSerialOpenHelper getIns() {
        if (mCardSerialUtil == null) {
            synchronized (CardSerialOpenHelper.class) {
                if (mCardSerialUtil == null) {
//                    mMakeOrRecycleNoodles = makeOrRecycleNoodles;
                    mCardSerialUtil = new CardSerialOpenHelper();
                }
            }
        }
        return mCardSerialUtil;
    }

    public CardSerialOpenHelper startCardSerial() {
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
        if (mCardSerialUtil!=null){
            mCardSerialUtil = null;
        }
    }

    public void setMakeOrRecycleNoodles(int makeOrRecycleNoodles) {
        mMakeOrRecycleNoodles = makeOrRecycleNoodles;
    }

    private TimeoutListener mTimeoutListener;
    public void setTimeoutListener(TimeoutListener timeoutListener){
        mTimeoutListener = timeoutListener;
    }

    public interface TimeoutListener {
        void onTimeout();
    }

}
