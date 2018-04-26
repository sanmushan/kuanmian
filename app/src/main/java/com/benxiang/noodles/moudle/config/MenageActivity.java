package com.benxiang.noodles.moudle.config;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.benxiang.noodles.BuildConfig;
import com.benxiang.noodles.MainActivity;
import com.benxiang.noodles.R;
import com.benxiang.noodles.base.BaseMenageActivity;
import com.benxiang.noodles.contants.Constants;
import com.benxiang.noodles.contants.DbTypeContants;
import com.benxiang.noodles.contants.MethodConstants;
import com.benxiang.noodles.data.DBNoodleHelper;
import com.benxiang.noodles.entrance.MakeNoodlesUtil;
import com.benxiang.noodles.entrance.SeasoningPackageUtil;
import com.benxiang.noodles.moudle.pay.PayParam;
import com.benxiang.noodles.serialport.ComBean;
import com.benxiang.noodles.serialport.SerialHelper;
import com.benxiang.noodles.serialport.bean.ErrorEvent;
import com.benxiang.noodles.serialport.cardmac.CardSerialOpenHelper;
import com.benxiang.noodles.serialport.cardmac.NoodlesSerialCommand;
import com.benxiang.noodles.serialport.cardmac.NoodlesSerialReceiver;
import com.benxiang.noodles.serialport.data.constant.PreferenceKey;
import com.benxiang.noodles.serialport.data.sp.FormulaPreferenceConfig;
import com.benxiang.noodles.serialport.service.MakeNoodlesService;
import com.benxiang.noodles.utils.JsonHelper;
import com.benxiang.noodles.utils.NoodleDataUtil;
import com.benxiang.noodles.utils.ParamObtainUtil;
import com.benxiang.noodles.utils.PreferenceUtil;
import com.benxiang.noodles.widget.ErrorDialogFragment;
import com.blankj.utilcode.util.ServiceUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.hugeterry.updatefun.UpdateFunGO;
import cn.hugeterry.updatefun.config.UpdateKey;
import timber.log.Timber;

import static com.benxiang.noodles.serialport.cardmac.CardSerialOpenHelper.MAKE_NOODLES;

/**
 * @author
 * Created by 刘圣如 on 2017/9/25.
 */

public class MenageActivity extends BaseMenageActivity implements MenageView {

    //退款信息
    @BindView(R.id.ed_money)
    EditText ed_money_weixin;
    @BindView(R.id.ed_order_no)
    EditText ed_order_no_weixin;
    @BindView(R.id.ed_order_price)
    EditText ed_order_price_weixin;

    @BindView(R.id.ed_money_ali)
    EditText ed_money_ali;
    @BindView(R.id.ed_order_no_ali)
    EditText ed_order_no_ali;

    @BindView(R.id.ed_exception)
    EditText edException;

    @BindView(R.id.ed_http_address)
    EditText ed_address;
    //读取当前库存的View
    @BindView(R.id.tv_current_noodles)
    TextView tvCurrentNoodles;
    @BindView(R.id.tvFreshCurrent)
    TextView tvFreshCurrent;
    @BindView(R.id.tv_current_rice)
    TextView tvCurrentRice;
    @BindView(R.id.tv_current_spicy)
    protected TextView tvCurrentSpicy;
    @BindView(R.id.tv_current_chicken_leg)
    protected TextView tvCurrentChickenLeg;
    @BindView(R.id.tv_current_eggs)
    protected TextView tvCurrentEggs;
    @BindView(R.id.tv_current_four_catogory)
    protected TextView tvCurrentFourCatogory;

    private MenagePresenter mMenagePresenter;
    //LIN
    private NoodlesSerialReceiver mNoodlesSerialReceiver;
    //模组2
    private SerialHelper mCardSerialHelper;
    private int errorCount = 0;//出错的次数
    private int count = 0;
    @BindView(R.id.btnControl)
    Button btnControl;
    @Override
    public int getContentViewID() {
        return R.layout.activity_menage;
    }

    @Override
    protected void afterContentViewSet() {
        initView();
    }

    private void initView() {
        registerMainHandler();
        mMenagePresenter = new MenagePresenter();
        mMenagePresenter.attachView(this);
        //机器异常卡位后，点击皮带正转，机器复位 LIN
        btnControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendSerialData(NoodlesSerialCommand.SEND_TO_SOUP_AREA);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        getDataToView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        UpdateFunGO.onResume(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        UpdateFunGO.onStop(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mMenagePresenter != null) {
            mMenagePresenter.detavh();
            mMenagePresenter = null;
        }
        if (mSeasoningPackageUtil != null){
            mSeasoningPackageUtil.recycle();
            mSeasoningPackageUtil = null;
        }
    }

    //接收数据
    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onCardEvent(ComBean comRecData) {
        if (comRecData.makeOrRecycleNoodles == CardSerialOpenHelper.BOTTLED_WATER) {
            mSeasoningPackageUtil.handleReceived(comRecData);
        }
    }

    private SeasoningPackageUtil mSeasoningPackageUtil;
    private boolean haveSend = false;
    private void initSeasoningPackage() {
        mSeasoningPackageUtil = new SeasoningPackageUtil() {
            @Override
            protected void dealWithError(String errorCode) {
                showError("换桶装水失败");
                haveSend = false;
            }

            @Override
            protected void onFinish() {}

            @Override
            protected void onBottledWaterFinish() {
                showWarningDialog("确定","换桶装水成功");
                haveSend = false;
            }
        };
        if (!haveSend){
            mSeasoningPackageUtil.startInBottledWater();
            haveSend = true;
        }
    }

    //读取当前库存并显示到View中
    @SuppressLint("SetTextI18n")
    private void getDataToView() {
        //读取当前各个物品的库存量
        int mianTiao = NoodleDataUtil.getDbNum(DbTypeContants.MIANTIAO);
        int miFen = NoodleDataUtil.getDbNum(DbTypeContants.MIFEN);
        int freshNoodles = NoodleDataUtil.getDbNum(DbTypeContants.FRESH_NOODLES);
        int suanLaBao = NoodleDataUtil.getDbNum(DbTypeContants.SUANLABAO);
        int suanLaJiTui = NoodleDataUtil.getDbNum(DbTypeContants.SUANLAJITUI);
        int luJiDan = NoodleDataUtil.getDbNum(DbTypeContants.LUJIDANG);
        int fourCatogory = NoodleDataUtil.getDbNum(DbTypeContants.FOUR_CATEGORY);

        //将读取到的库存显示到View中
        tvCurrentNoodles.setText(Integer.toString(mianTiao));
        tvCurrentRice.setText(Integer.toString(miFen));
        tvFreshCurrent.setText("新鲜面数量：" + Integer.toString(freshNoodles));
        tvCurrentSpicy.setText(Integer.toString(suanLaBao));
        //第二品类 卤蛋
        tvCurrentChickenLeg.setText(Integer.toString(luJiDan));
        //第三品类 鸡腿
        tvCurrentEggs.setText(Integer.toString(suanLaJiTui));

        tvCurrentFourCatogory.setText(Integer.toString(fourCatogory));

    }

    //重置数据库的点击事件
    public void confirmToDb(View view) {
        dataToDB();
    }

    //解除异常
    public void exceptionRelief(View view) {
        if (edExceptionHint(edException)) {
            ReliefParam reliefParam = new ReliefParam();
            reliefParam.mechanical_num = MethodConstants.SHOPCODE;
            reliefParam.abnormal_detail = edException.getText().toString().trim();
            mMenagePresenter.ExceptionResult(MethodConstants.RELIEF_EXCEPTION_IF, JsonHelper.getGson().toJson(reliefParam));
        }
    }
    //模组2发送数据
    private void sendSerialData(byte[] data) {
        mCardSerialHelper = CardSerialOpenHelper.getIns().getCardSerialHelper();
        CardSerialOpenHelper.getIns().setTimeoutListener(new CardSerialOpenHelper.TimeoutListener() {
            @Override
            public void onTimeout() {
            }
        });
        if (errorCount > 5) {
            mNoodlesSerialReceiver.setExecStep(0);
            count=0;
            errorCount=0;
            Timber.e("count="+count);
            return;
        }
        if (!mCardSerialHelper.isOpen()) {
            CardSerialOpenHelper.getIns().startCardSerial();
            Log.e(">>>>>>>>>>>>>>", "isOpen");
        }

        CardSerialOpenHelper.getIns().setMakeOrRecycleNoodles(MAKE_NOODLES);

        if (mCardSerialHelper!=null && mCardSerialHelper.isOpen()) {
            mCardSerialHelper.send(data);
        }else {
            Timber.e("米粉机串口未能正常运行");
            ErrorEvent errorEvent = new ErrorEvent("米粉机串口未能正常运行，请到相应位置办理手续!");
            EventBus.getDefault().post(errorEvent);
        }
    }


    //退款接口
    public void refundment(View view) {
        PayParam payParam = null;
        String method = "";
        switch (view.getId()) {
            case R.id.weixin:
                method = MethodConstants.RUNWXREFUND;
                if (edExceptionHint(ed_money_weixin) && edExceptionHint(ed_order_no_weixin) && edExceptionHint(ed_order_price_weixin)) {
                    payParam = ParamObtainUtil.getPayParam(ed_money_weixin.getText().toString().trim(), ed_order_price_weixin.getText().toString().trim()
                            , ed_order_no_weixin.getText().toString().trim(), Constants.WEIXIN);
                }
                break;
            case R.id.ali:
                method = MethodConstants.RUNalipayREFUND;
                if (edExceptionHint(ed_money_ali) && edExceptionHint(ed_order_no_ali)) {
                    payParam = ParamObtainUtil.getPayParam(ed_money_ali.getText().toString().trim(), String.valueOf(0.00)
                            , ed_order_no_ali.getText().toString().trim(), Constants.ALIPAY);
                }

        }
        if (payParam != null) {
            mMenagePresenter.refundment(method, JsonHelper.getGson().toJson(payParam));
        }
    }

    //返回主界面
    public void backToMain(View view) {
        showLoading();
        getMainHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                hideLoadingDialog();
                startNext(MenageActivity.this, MainActivity.class);
            }
        }, 1000);
    }

    //返回桌面
    public void returnHome(View view) {
        Intent home = new Intent(Intent.ACTION_MAIN);
        home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        home.addCategory(Intent.CATEGORY_HOME);
        startActivity(home);
        finish();
    }

    //换桶装水
    public void bottledWater(View view) {
        initSeasoningPackage();
    }

    //修改服务器后台地址
    public void saveAddress(View view) {
        if (edExceptionHint(ed_address)) {
            showWarningDialog("确定", "确定要修改后台地址", new ErrorDialogFragment.OnErrorClickListener() {
                @Override
                public void onClick(ErrorDialogFragment dialog) {
                    PreferenceUtil.config().setStringValue(Constants.HTTP_ADDRESS, ed_address.getText().toString().trim());
                    killAllErrorDialogs();
                    Toast.makeText(MenageActivity.this, "地址修改成功", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private boolean edExceptionHint(EditText editText) {
        if (editText.getText().toString().trim() == "") {
            showError("信息不能为空");
            return false;
        }
        return true;
    }

    //更新应用
    public void upadeApp(View view) {
        updateFun();
    }

    @Override
    public void showLoading() {
        showLoadingDialog();
    }

    @Override
    public void hideLoading() {
        hideLoadingDialog();
    }

    @Override
    public void ReliefFiale() {
        showError("提交异常失败，请检查信息");
    }

    @Override
    public void ReliefSuccess(String resultMsg) {
        showWarningDialog("确定", "提交异常成功");
        if (BuildConfig.IS_MANY_POWL) {
            if (!ServiceUtils.isServiceRunning("com.benxiang.noodles.serialport.service.MakeNoodlesService")) {
                ServiceUtils.startService(MakeNoodlesService.class);
            }
        }
        PreferenceUtil.config().setBooleanValue(Constants.MACHINE_EXCEPTION, false);
        DBNoodleHelper.deleteAll();
        FormulaPreferenceConfig.setErrorCount(PreferenceKey.ERROR_COUNT_DEFAULT);
    }

    @Override
    public void refundmentSuccess(String resultMsg) {
        showWarningDialog("确定", "退款成功，请查收");
    }

    @Override
    public void upedaAppSuccess(String resultMsg) {
//        checkUpdateVersion(resultMsg);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    public void updateFun(){
        UpdateKey.API_TOKEN = "0fd94d488bb0e6a45efa575271c30808";
        UpdateKey.APP_ID = BuildConfig.APPLICATION_ID;
        //下载方式:
        UpdateFunGO.init(this);
        UpdateFunGO.manualStart(this);
    }


}
