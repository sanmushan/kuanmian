package com.benxiang.noodles.test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.benxiang.noodles.R;
import com.benxiang.noodles.utils.ByteUtil;
import com.benxiang.noodles.utils.CRC16;
import com.benxiang.noodles.utils.MyFunc;
import com.blankj.utilcode.util.BarUtils;

import timber.log.Timber;

public class CalculateCRCActivity extends AppCompatActivity {

    byte[] A_A_FORMULA_VINEGAR = new byte[]{(byte) 0xF2, 0x10, 0x00, 0x50, 0x00, 0x06, 0x0C, 0x00, 0x5A , 0x00, 0x46 , 0x00, 0x12 , 0x00, 0x1E , 0x00, 0x55, 0x00,(byte) 0x96};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculate_crc);
    }

    public void click(View view){
//        byte[] bytes = CRC16.crc16Checkout(A_A_FORMULA_VINEGAR);
//        String byteArrToHex = MyFunc.ByteArrToHex(bytes);
//        Timber.e("byteArrToHex="+byteArrToHex);
        finish();
    }

    public void click2(View view){
        BarUtils.hideNavBar(this);
    }

    public void click3(View view){
        BarUtils.showNotificationBar(this,false);
//        int systemUiFlagHideNavigation = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;

    }
}
