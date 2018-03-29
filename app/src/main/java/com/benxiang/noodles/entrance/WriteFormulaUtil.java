package com.benxiang.noodles.entrance;

import android.util.Log;

import com.benxiang.noodles.serialport.ComBean;
import com.benxiang.noodles.serialport.SerialHelper;
import com.benxiang.noodles.serialport.cardmac.CardSerialOpenHelper;
import com.benxiang.noodles.serialport.cardmac.NoodlesSerialCommand;
import com.benxiang.noodles.serialport.cardmac.WriteFormulaReceiver;
import com.blankj.utilcode.util.LogUtils;

import timber.log.Timber;

/**
 * Created by Zeqiang Fang on 2017/9/1.
 */
public abstract class WriteFormulaUtil {
    private WriteFormulaReceiver mWriteFormulaReceiver;
    //模组2
    private SerialHelper mCardSerialHelper;

    private WriteFormulaReceiver.OnSuccessListener mFormulaSuccessListener;
    private WriteFormulaReceiver.OnFailureListener mFormulaFailureListener;
    private int errorCount = 0;

    protected WriteFormulaUtil() {
        mCardSerialHelper = CardSerialOpenHelper.getIns().getCardSerialHelper();
        CardSerialOpenHelper.getIns().setTimeoutListener(new CardSerialOpenHelper.TimeoutListener() {
            @Override
            public void onTimeout() {
                dealWithError();
            }
        });
        mWriteFormulaReceiver = WriteFormulaReceiver.getIns();
        handleSuccess();
        handleFailure();
    }


    public void startWriteFormula() {
        logStep("===========================================");
        sendSerialData(NoodlesSerialCommand.A_A_FORMULA);
    }

    public void handleReceived(ComBean comBean) {
        mWriteFormulaReceiver.handleReceivedData(comBean);
    }

    public void recycle() {
//        CardSerialOpenHelper.getIns(MAKE_NOODLES).closeCardSerial();
        mWriteFormulaReceiver.setOnFailureListener(null);
        mWriteFormulaReceiver.setOnSuccessListener(null);
        mFormulaSuccessListener = null;
        mFormulaFailureListener = null;
        mWriteFormulaReceiver.releaseListener();
    }

    private int count = 0;

    private void handleSuccess() {
        mFormulaSuccessListener = new WriteFormulaReceiver.OnSuccessListener() {
            @Override
            public void writeAAFormula() {
                logStep("写入AA配方成功");
                sendSerialData(NoodlesSerialCommand.A_B_FORMULA);
            }

            @Override
            public void writeABFormula() {
                logStep("写入AB配方成功");
                sendSerialData(NoodlesSerialCommand.A_C_FORMULA);
            }

            @Override
            public void writeACFormula() {
                logStep("写入AC配方成功");
                sendSerialData(NoodlesSerialCommand.B_A_FORMULA);
            }

            @Override
            public void writeBAFormula() {
                logStep("写入BA配方成功");
                sendSerialData(NoodlesSerialCommand.B_B_FORMULA);
            }

            @Override
            public void writeBBFormula() {
                logStep("写入BB配方成功");
                sendSerialData(NoodlesSerialCommand.B_C_FORMULA);
            }

            @Override
            public void writeBCFormula() {
                logStep("写入BC配方成功");
                sendSerialData(NoodlesSerialCommand.C_A_FORMULA);
            }

            @Override
            public void writeCAFormula() {
                logStep("写入CA配方成功");
                sendSerialData(NoodlesSerialCommand.C_B_FORMULA);
            }

            @Override
            public void writeCBFormula() {
                logStep("写入CB配方成功");
                sendSerialData(NoodlesSerialCommand.C_C_FORMULA);
            }

            @Override
            public void writeCCFormula() {
                logStep("写入CC配方成功");
                sendSerialData(NoodlesSerialCommand.A_A_FORMULA_VINEGAR);
//                sendSerialData(NoodlesSerialCommand.CODE_THREE_A_A);
            }
            //------------------------------------------------------------------------------------加醋命令------------------------------------------------------------------------------------------------------------
            @Override
            public void writeAAFormulaVinegar() {
                logStep("写入AA有醋配方成功");
                sendSerialData(NoodlesSerialCommand.A_B_FORMULA_VINEGAR);
            }

            @Override
            public void writeABFormulaVinegar() {
                logStep("写入AB有醋配方成功");
                sendSerialData(NoodlesSerialCommand.A_C_FORMULA_VINEGAR);
            }

            @Override
            public void writeACFormulaVinegar() {
                logStep("写入AC有醋配方成功");
                sendSerialData(NoodlesSerialCommand.B_A_FORMULA_VINEGAR);
            }

            @Override
            public void writeBAFormulaVinegar() {
                logStep("写入BA有醋配方成功");
                sendSerialData(NoodlesSerialCommand.B_B_FORMULA_VINEGAR);
            }

            @Override
            public void writeBBFormulaVinegar() {
                logStep("写入BB有醋配方成功");
                sendSerialData(NoodlesSerialCommand.B_C_FORMULA_VINEGAR);
            }

            @Override
            public void writeBCFormulaVinegar() {
                logStep("写入BC有醋配方成功");
                sendSerialData(NoodlesSerialCommand.C_A_FORMULA_VINEGAR);
            }

            @Override
            public void writeCAFormulaVinegar() {
                logStep("写入CA有醋配方成功");
                sendSerialData(NoodlesSerialCommand.C_B_FORMULA_VINEGAR);
            }

            @Override
            public void writeCBFormulaVinegar() {
                logStep("写入CB有醋配方成功");
                sendSerialData(NoodlesSerialCommand.C_C_FORMULA_VINEGAR);
            }

            @Override
            public void writeCCFormulaVinegar() {
                logStep("写入CC有醋配方成功");
                onFinish();
                count = 0;
                sendSerialData(NoodlesSerialCommand.CODE_THREE_A_A);
            }

            @Override
            public void readAAFormula() {
                onFinish();
                count = 0;
            }

        };
        mWriteFormulaReceiver.setOnSuccessListener(mFormulaSuccessListener);
    }

        private void handleFailure() {
        mFormulaFailureListener = new WriteFormulaReceiver.OnFailureListener() {
            @Override
            public void writeAAFormula() {
                logStep("写数据失败");
                handleError();
            }

            @Override
            public void writeABFormula() {
                logStep("写数据失败");
                handleError();
            }

            @Override
            public void writeACFormula() {
                logStep("写数据失败");
                handleError();
            }

            @Override
            public void writeBAFormula() {
                logStep("写数据失败");
                handleError();
            }

            @Override
            public void writeBBFormula() {
                logStep("写数据失败");
                handleError();
            }

            @Override
            public void writeBCFormula() {
                logStep("写数据失败");
                handleError();
            }

            @Override
            public void writeCAFormula() {
                logStep("写数据失败");
                handleError();
            }

            @Override
                public void writeCBFormula() {
                logStep("写数据失败");
                handleError();
            }

            @Override
            public void writeCCFormula() {
                logStep("写数据失败");
                handleError();
            }

            @Override
            public void writeAAFormulaVinegar() {
                logStep("写数据失败");
                handleError();
            }

            @Override
            public void writeABFormulaVinegar() {
                logStep("写数据失败");
                handleError();
            }

            @Override
            public void writeACFormulaVinegar() {
                logStep("写数据失败");
                handleError();
            }

            @Override
            public void writeBAFormulaVinegar() {
                logStep("写数据失败");
                handleError();
            }

            @Override
            public void writeBBFormulaVinegar() {
                logStep("写数据失败");
                handleError();
            }

            @Override
            public void writeBCFormulaVinegar() {
                logStep("写数据失败");
                handleError();
            }

            @Override
            public void writeCAFormulaVinegar() {
                logStep("写数据失败");
                handleError();
            }

            @Override
            public void writeCBFormulaVinegar() {
                logStep("写数据失败");
                handleError();
            }

            @Override
            public void writeCCFormulaVinegar() {
                logStep("写数据失败");
                handleError();
            }

            @Override
            public void readAAFormula() {
                logStep("读数据失败");
                handleError();
            }

        };
        mWriteFormulaReceiver.setOnFailureListener(mFormulaFailureListener);
    }

    //模组2发送数据
    private void sendSerialData(byte[] data) {
        if (errorCount > 5) {
            mWriteFormulaReceiver.setExecStep(0);
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
        CardSerialOpenHelper.getIns().setMakeOrRecycleNoodles(CardSerialOpenHelper.WRITE_FORMULA);

        mCardSerialHelper.send(data);
    }


    private void handleError(){
        mWriteFormulaReceiver.setExecStep(0);
        errorCount=0;
        count=0;
        dealWithError();
    }

    protected abstract void dealWithError();

    protected abstract void onFinish();

    protected void tooMuchError() {
        Timber.e("多次错误异常");
    }

    private void logStep(String msg) {
        Timber.e(">>>>>>>>>>>> 步骤 " + count + "  : " +msg);
        LogUtils.file(">>>>>>>>>>>> 步骤 " + count + "  : " +msg);
        count++;
    }
}
