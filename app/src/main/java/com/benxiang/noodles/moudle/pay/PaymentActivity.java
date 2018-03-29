package com.benxiang.noodles.moudle.pay;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.benxiang.noodles.R;
import com.benxiang.noodles.base.SetListActivity;
import com.benxiang.noodles.contants.Constants;
import com.benxiang.noodles.contants.MethodConstants;
import com.benxiang.noodles.data.pay.PayType;
import com.benxiang.noodles.model.ListModle;
import com.benxiang.noodles.model.NoodleTradeModel;
import com.benxiang.noodles.utils.DataEncrypt;
import com.benxiang.noodles.utils.JsonHelper;
import com.benxiang.noodles.utils.PreferenceUtil;
import com.benxiang.noodles.utils.QRCodeUtil;
import com.benxiang.noodles.widget.ErrorDialogFragment;
import com.blankj.utilcode.util.DeviceUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import timber.log.Timber;

/**
 * Created by 刘圣如 on 2017/9/1.
 */

public class PaymentActivity extends SetListActivity implements PayView {

    @BindView(R.id.pay_total)
    TextView tvTotal;
    @BindView(R.id.pay_code_total)
    TextView payQrCode;
    @BindView(R.id.ll_pay_way)
    LinearLayout llPayWay;
    @BindView(R.id.ll_pay_qrcode)
    LinearLayout llPayQrcode;

    @BindView(R.id.iv_qr_code)
    ImageView ivQRCode;
    @BindView(R.id.view_scan_line)
    View viewScanLine;
    @BindView(R.id.layout_qr_code)
    FrameLayout layoutQrCode;
    private CommonAdapter<ListModle> mAdapter;
    private List<ListModle> goodsNewsBeanList = new ArrayList<>();
    private NoodleTradeModel mNoodleTradeModel;
    private PayPresenter mPayPresenter;
    private int payWay;

    private String orderNo = "";

    @Override
    public int getContentViewID() {
        return R.layout.activity_payment;
    }

    @Override
    protected void afterContentViewSet() {
        registerMainHandler();
        initView();
        initOrderNo();
        super.afterContentViewSet();
        setEnableCountdown(true);
        SetCountDownOnClick(false);
    }

    @Override
    protected void onStart() {
        super.onStart();
        speakText("请选择您的支付方式");

    }

    private void initOrderNo() {
        if (DeviceUtils.getMacAddress()!=null) {
            orderNo = DeviceUtils.getMacAddress().replace(":", "").toUpperCase() + DataEncrypt.dataFormatString();
        }
        else {
            orderNo = DataEncrypt.dataFormatString();
        }
        PreferenceUtil.config().setStringValue("orderNo", orderNo);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (animator != null && animator.isRunning()) {
            animator.cancel();
        }
    }

    private void initView() {
        mNoodleTradeModel = getIntent().getParcelableExtra("noodle");
        mPayPresenter = new PayPresenter();
        mPayPresenter.attachView(this);
        initDtas(mNoodleTradeModel);
        mAdapter = new CommonAdapter<ListModle>(this, R.layout.item_recycle_goods_pay, goodsNewsBeanList) {

            @Override
            protected void convert(final ViewHolder holder, ListModle goodsNewsBean, int position) {
                final int[] goods_num = {goodsNewsBean.goods_num};
                holder.setText(R.id.tv_select_goods_name, goodsNewsBean.goods_name);
                holder.setText(R.id.tv_select_goods_price, getString(R.string.money_desc, goodsNewsBean.goods_prive*goods_num[0]));
                holder.setText(R.id.tv_select_goods_num, "" + goods_num[0]);
            }
        };
        setAdapter(mAdapter, true);
        tvTotal.setText(getString(R.string.payment_total, mNoodleTradeModel.total_num, mNoodleTradeModel.total_price));
        payQrCode.setText(getString(R.string.payment_total, mNoodleTradeModel.total_num, mNoodleTradeModel.total_price));
    }

    private void initDtas(NoodleTradeModel mNoodleTradeModel) {
        if (mNoodleTradeModel != null) {
            goodsNewsBeanList = mNoodleTradeModel.listModles;
        }
     /*   for (int i = 0; i < mNoodleTradeModel.listModles.size(); i++) {
            GoodsNewsBean bean = new GoodsNewsBean();
            bean.goods_name = mNoodleTradeModel.listModles.get(i).goods_name;
            bean.goods_price = mNoodleTradeModel.listModles.get(i).goods_prive;
            bean.sum = mNoodleTradeModel.listModles.get(i).goods_num;
            goodsNewsBeanList.add(bean);
        }*/
    }

    public void srartPay(View view) {
        if (PreferenceUtil.config().getBooleanValue(Constants.MACHINE_EXCEPTION)) {
            showWarningDialog("确定", "机器维修中，请稍候再来点餐...", new ErrorDialogFragment.OnErrorClickListener() {
                @Override
                public void onClick(ErrorDialogFragment dialog) {
                    dialog.dismiss();
                    showLoadingDialog();
                    getMainHandler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            killLoading();
                            finish();
                        }
                    }, 2000);
                }
            });
            return;
        }
        changell();
        switch (view.getId()) {
            case R.id.btn_alipay:
                payWay = Constants.ALIPAY;
                initPayQrCode(payWay);
                break;
            case R.id.btn_weixinpay:
                payWay = Constants.WEIXIN;
                initPayQrCode(payWay);
                break;
        }
    }

    ObjectAnimator animator;

    private void startScanAnimation() {
        int width = layoutQrCode.getWidth();
        width = (int) (width * 0.9);
        Log.e("", "startScanAnimation: " + width);
        viewScanLine.setVisibility(View.VISIBLE);
        animator = ObjectAnimator.ofFloat(viewScanLine, "translationY", 0, 270);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setRepeatMode(ValueAnimator.RESTART);
        animator.setDuration(3000);
        animator.start();
    }

    private void changell() {
        llPayQrcode.setVisibility(View.VISIBLE);
        llPayWay.setVisibility(View.GONE);
//        startScanAnimation();
    }

    private void initPayQrCode(int payWay) {
        String method = "";
        PayParam payParam = new PayParam();
        Timber.e("金额="+mNoodleTradeModel.total_price);
        DecimalFormat decimalFormat=new DecimalFormat(".00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
        payParam.money = decimalFormat.format(mNoodleTradeModel.total_price);
//        payParam.money = 0.01+"";
        payParam.orderNo = orderNo;
        Log.e("订单号1", "getQrCode: " + payParam.orderNo+payParam.money);
//        payParam.shopCode = MethodConstants.SHOPCODE;
        payParam.shopCode = MethodConstants.SHOPCODE;
        Timber.e(payParam.toString());
        switch (payWay) {
            case Constants.ALIPAY:
                method = "GetAlipayRSA";
                break;
            case Constants.WEIXIN:
                method = "GetWxPayRSA";
                break;
        }
        mPayPresenter.getQR(method, JsonHelper.getGson().toJson(payParam));

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showNetError(String error) {
        showError(error);
    }


    @Override
    public void getQrCode(String data) {
        Bitmap bitmap = QRCodeUtil.createQRImage(data, 280, 280, null);
        ivQRCode.setImageBitmap(bitmap);
        startScanAnimation();
        String method = "";
        switch (payWay) {
            case Constants.ALIPAY:
                method = "AOrderQuery";
                PreferenceUtil.config().setStringValue("payType", MethodConstants.RUNalipayREFUND);
                mNoodleTradeModel.pay_type = PayType.ALI_PAY_TYPE;
                break;
            case Constants.WEIXIN:
                method = "WxOrderQuery";
                PreferenceUtil.config().setStringValue("payType", MethodConstants.RUNWXREFUND);
                mNoodleTradeModel.pay_type = PayType.WEIXIN_PAY_TYPE;
                break;
        }
        PayStatusParam payParam = new PayStatusParam();
        payParam.orderNo = orderNo;
        Log.e("订单号2", "getQrCode: " + payParam.orderNo);
        payParam.shopCode = MethodConstants.SHOPCODE;
        mPayPresenter.payStatus(method, JsonHelper.getGson().toJson(payParam));
    }

    @Override
    public void paySuccess() {
//        mNoodleTradeModel.ggid = orderNo;
        mNoodleTradeModel.pay_time = DataEncrypt.dataFormat();
        mNoodleTradeModel.order_No = orderNo;
        getMainHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startRefundment(PaymentActivity.this, PaySuccessActivity.class, mNoodleTradeModel,payWay);
            }
        }, 1000);
//        startNext(PaymentActivity.this, PaySuccessActivity.class,mNoodleTradeModel);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPayPresenter != null) {
            mPayPresenter.detavh();
            mPayPresenter = null;
        }

    }

    @Override
    protected void StartBanner() {
        finish();
    }
}
