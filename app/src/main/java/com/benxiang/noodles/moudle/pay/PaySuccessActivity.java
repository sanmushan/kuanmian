package com.benxiang.noodles.moudle.pay;

import android.os.CountDownTimer;
import android.util.Log;
import android.widget.Button;

import com.benxiang.noodles.BuildConfig;
import com.benxiang.noodles.R;
import com.benxiang.noodles.base.BaseActivity;
import com.benxiang.noodles.contants.Constants;
import com.benxiang.noodles.contants.MethodConstants;
import com.benxiang.noodles.model.NoodleTradeModel;
import com.benxiang.noodles.model.addMeal.AddOrderItemModel;
import com.benxiang.noodles.model.addMeal.AddOrderItemParam;
import com.benxiang.noodles.model.addMeal.AddOrderItemPresenter;
import com.benxiang.noodles.model.addMeal.AddOrderItemView;
import com.benxiang.noodles.model.placeorder.PlaceOrderModel;
import com.benxiang.noodles.model.placeorder.PlaceOrderParam;
import com.benxiang.noodles.model.placeorder.PlaceOrderPresenter;
import com.benxiang.noodles.model.placeorder.PlaceOrderView;
import com.benxiang.noodles.moudle.luckydraw.LuckyDrawActivity;
import com.benxiang.noodles.moudle.makenoodle.NoodlesMakeActivity;
import com.benxiang.noodles.utils.DataEncrypt;
import com.benxiang.noodles.utils.JsonHelper;
import com.benxiang.noodles.utils.NoodleDataUtil;
import com.benxiang.noodles.utils.NoodleTradeFieldUtil;
import com.benxiang.noodles.utils.ParamObtainUtil;
import com.benxiang.noodles.utils.PreferenceUtil;
import com.benxiang.noodles.utils.ShotrNoUtil;
import com.benxiang.noodles.utils.SpUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import timber.log.Timber;

/**
 * Created by 刘圣如 on 2017/9/11.
 */

public class PaySuccessActivity extends BaseActivity implements PaySuccessView, PlaceOrderView,AddOrderItemView {

    private static final String TAG = "PaySuccessActivity";
    @BindView(R.id.payment_confrim)
    Button countTime;
    private NoodleTradeModel mNoodleTradeModel;
    private MyCountDownTimer timer;
    private int payWay;
    private PaySuccessPresenter mPaySuccessPresenter;
    private PlaceOrderPresenter placeOrderPresenter;
    private AddOrderItemPresenter mAddOrderItemPresenter;
    private String payItemTime = DataEncrypt.dataFormat();

    @Override
    public int getContentViewID() {
        return R.layout.activity_paysuccess;
    }

    @Override
    protected void afterContentViewSet() {
        registerMainHandler();
        initView();
        setFinishOnTouchOutside(false);
    }

    private void dbTest(int shotrNo, NoodleTradeModel mNoodleTradeModel) {
        NoodleDataUtil.getOrderToDB(shotrNo, mNoodleTradeModel);
    }

    private void initView() {
        mNoodleTradeModel = getIntent().getParcelableExtra("noodle");
        payWay = getIntent().getIntExtra("payType", 0);
        mPaySuccessPresenter = new PaySuccessPresenter();
        mPaySuccessPresenter.attachView(this);

        placeOrderPresenter=new PlaceOrderPresenter();
        placeOrderPresenter.attachView(this);

        Log.e(TAG, "initView: " + payWay);

        ToPlaceOrder();
        if (BuildConfig.DEBUG){
            //TODO  LIN  支付成功后自动退款
            ToRefundment(payWay);
        }

        getMainHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i=0;i<mNoodleTradeModel.listModles.size();i++) {
                    mNoodleTradeModel.listModles.get(i).itemTime = payItemTime;
                }
                //LIN 获取是偶可以抽奖 1可以，0不可以
                String luckyDraw = SpUtils.loadValue("lucky");
                //抽奖时抽到的数据,全部设置为谢谢惠顾
                NoodleTradeFieldUtil.setListModlesWithLottery(mNoodleTradeModel);
//                if (BuildConfig.IS_MANY_POWL){//原先抽奖的判断
                if (luckyDraw.equals("1")){
                    startRefundment(PaySuccessActivity.this, LuckyDrawActivity.class, mNoodleTradeModel, payWay);
                }else {
                    dbTest(ShotrNoUtil.getShotrNo(true), mNoodleTradeModel);
                    startRefundment(PaySuccessActivity.this, NoodlesMakeActivity.class, mNoodleTradeModel, payWay);
                }
                finish();
            }
        }, 2000);
    }

    private void ToPlaceOrder() {
        PlaceOrderParam placeOrderParam = ParamObtainUtil.getPlaceOrderParam(mNoodleTradeModel,mNoodleTradeModel.listModles);
//        Timber.e("下单的参数:"+JsonHelper.getGson().toJson(placeOrderParam));
        placeOrderPresenter.placeOrder(MethodConstants.GREATEORDER, JsonHelper.getGson().toJson(placeOrderParam));
    }

    //待完善
    private void ToBuckleDb() {
        BuckleParam buckleParam = new BuckleParam();
        List<BuckleParam.BuckleData> buckleDatas = new ArrayList<>();
        buckleParam.BillNo = PreferenceUtil.config().getStringValue("orderNo");
        buckleParam.LID = MethodConstants.SHOPCODE;
        BuckleParam.BuckleData buckleData = new BuckleParam.BuckleData();
        buckleData.LID = MethodConstants.SHOPCODE;
        buckleData.MenuItemCode = "0101";
        buckleData.Quantity = 1;
        buckleData.Standard = "个";
        buckleData.SellingMoney = 15;
        buckleData.SellingPrice = 15;
        buckleDatas.add(buckleData);
        buckleParam.Detail = buckleDatas;
        mPaySuccessPresenter.buckleDb(MethodConstants.KOUKU, JsonHelper.getGson().toJson(buckleParam));
    }

    private void ToRefundment(int payWay) {
        String method = "";
        PayParam payParam = new PayParam();
        //构造方法的字符格式这里如果小数不足2位,会以0补足.
        DecimalFormat decimalFormat = new DecimalFormat(".00");
        if (payWay == Constants.ALIPAY) {
            method = MethodConstants.RUNalipayREFUND;
        } else if (payWay == Constants.WEIXIN) {
            method = MethodConstants.RUNWXREFUND;
            payParam.orderPrice=decimalFormat.format(mNoodleTradeModel.total_price);
        }
        Log.e(TAG, "initView: " + method);

        payParam.shopCode = MethodConstants.SHOPCODE;
        payParam.money = decimalFormat.format(mNoodleTradeModel.total_price);
        payParam.orderNo = PreferenceUtil.config().getStringValue("orderNo");
        mPaySuccessPresenter.refundment(method, JsonHelper.getGson().toJson(payParam));

        Log.e(TAG, "initView: " + payParam.orderNo);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPaySuccessPresenter != null) {
            mPaySuccessPresenter.detavh();
            mPaySuccessPresenter = null;
        }
        if (placeOrderPresenter != null) {
            placeOrderPresenter.detavh();
            placeOrderPresenter = null;
        }
        if (mAddOrderItemPresenter != null){
            mAddOrderItemPresenter.detavh();
            mAddOrderItemPresenter = null;
        }
        cannelTimer();
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showNetError(String error) {

    }

    @Override
    public void refundmentSuccess() {

    }

    @Override
    public void buckleDbSuccess() {}

    @Override
    public void placeOrderSuccess(PlaceOrderModel strMsg) {

        mNoodleTradeModel.take_meal_No = strMsg.GUID;
        mNoodleTradeModel.isSetBillStatusByTakeNumber = false;
        mNoodleTradeModel.ggid = strMsg.GUID;
        Timber.e("订单号："+mNoodleTradeModel.order_No+",取餐号:"+mNoodleTradeModel.take_meal_No);

        //下单完成后加菜
        ToAddOrderItem();
    }

    @Override
    public void addOrderItemSuccess(AddOrderItemModel addOrderItemModel) {}

    private void ToAddOrderItem() {
        AddOrderItemParam param = ParamObtainUtil.getAddOrderBrineParam(mNoodleTradeModel);
        mAddOrderItemPresenter = new AddOrderItemPresenter();
        mAddOrderItemPresenter.attachView(this);
        Timber.e("加菜传的参数:"+JsonHelper.getGson().toJson(param));
        mAddOrderItemPresenter.addOrderItem(MethodConstants.ADDORDERITEM,JsonHelper.getGson().toJson(param));
    }

    private class MyCountDownTimer extends CountDownTimer {

        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            if (countTime != null) {
                countTime.setText(getString(R.string.pay_success_confirm, millisUntilFinished / 1000));
            }
        }

        @Override
        public void onFinish() {
            startNext(PaySuccessActivity.this, NoodlesMakeActivity.class, mNoodleTradeModel);
        }
    }

    private void startTimer() {
        if (timer == null) {
            timer = new MyCountDownTimer(Constants.pay_COUNT_DOWN_TIME, 1000);
        }
        timer.start();
    }

    private void cannelTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (mPaySuccessPresenter != null) {
            mPaySuccessPresenter.detavh();
            mPaySuccessPresenter = null;
        }
    }
}
