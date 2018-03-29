package com.benxiang.noodles.test;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.benxiang.noodles.AppApplication;
import com.benxiang.noodles.R;
import com.benxiang.noodles.base.BaseActivity;
import com.benxiang.noodles.entrance.WriteFormulaUtil;
import com.benxiang.noodles.serialport.ComBean;
import com.benxiang.noodles.serialport.SerialHelper;
import com.benxiang.noodles.serialport.cardmac.CardSerialOpenHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import timber.log.Timber;

import static com.benxiang.noodles.serialport.cardmac.CardSerialOpenHelper.WRITE_FORMULA;

public class WriteFormulaActivity extends BaseActivity {

    WriteFormulaUtil mWriteFormulaUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_formula);
        EventBus.getDefault().register(this);
    }

    @Override
    public int getContentViewID() {
        return R.layout.activity_write_formula;
    }

    @Override
    protected void afterContentViewSet() {}

    public void click(View view){
//        NoodlesIsEnable();
        writeFormula();
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    private void writeFormula() {
        mWriteFormulaUtil = new WriteFormulaUtil() {
            @Override
            protected void dealWithError() {
                Timber.e("写数据失败");
                showError("写数据失败");
            }

            @Override
            protected void onFinish() {
                Timber.e("写数据完成");
            }
        };
        mWriteFormulaUtil.startWriteFormula();
    }

    //接收数据
    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onCardEvent(ComBean comRecData) {
//        Timber.e("makeOrRecycleNoodles的值:"+ MyFunc.ByteArrToHex(comRecData.bRec));
        if (comRecData.makeOrRecycleNoodles == WRITE_FORMULA){
            mWriteFormulaUtil.handleReceived(comRecData);
        }
    }

    private void NoodlesIsEnable() {
        SerialHelper cardSerialHelper = CardSerialOpenHelper.getIns().getCardSerialHelper();
        if (cardSerialHelper != null && cardSerialHelper.isOpen()) {
//            cardSerialHelper.send(CardSerialCommand.CHECK_STATE);

            Timber.e("米粉机串口正常运行");
            Toast.makeText(AppApplication.getAppContext(), "米粉机串口正常运行", Toast.LENGTH_SHORT).show();
        } else {
//            showErrorDialogNoActon(getString(R.string.tip_card_mac_doest_work));

            Timber.e("米粉机串口未能正常运行");
            Toast.makeText(AppApplication.getAppContext(), "米粉机串口未能正常运行", Toast.LENGTH_SHORT).show();
        }
    }


}
