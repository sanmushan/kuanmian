package com.benxiang.noodles.serialport.cardmac;


import com.benxiang.noodles.serialport.ComBean;
import com.benxiang.noodles.serialport.SerialHelper;
import com.benxiang.noodles.utils.MyFunc;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

import timber.log.Timber;

import static com.benxiang.noodles.serialport.cardmac.CardSerialOpenHelper.RECYCLE_NOODLES;

/**
 * Created by Zeqiang Fang on 2017/8/24.
 */
public class RecycleSerialOpenHelper {

    private static RecycleSerialOpenHelper mCardSerialUtil;

    private SerialHelper mCardSerialHelper;

    private RecycleSerialOpenHelper(){

        mCardSerialHelper = new SerialHelper("/dev/ttyS3",9600) {
            @Override
            protected void onDataReceived(ComBean ComRecData) {
                Timber.e("com数量：" + ComRecData.iSize + ">>>>>" + MyFunc.ByteArrToHex(ComRecData.bRec));
                ComRecData.makeOrRecycleNoodles = RECYCLE_NOODLES;
                EventBus.getDefault().post(ComRecData);
            }

            @Override
            protected void onOverTime() {
                
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

    public static RecycleSerialOpenHelper getIns() {
        if (mCardSerialUtil == null) {
            synchronized (RecycleSerialOpenHelper.class) {
                if (mCardSerialUtil == null) {
                    mCardSerialUtil = new RecycleSerialOpenHelper();
                }
            }
        }
        return mCardSerialUtil;
    }

    public RecycleSerialOpenHelper startCardSerial() {
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

}
