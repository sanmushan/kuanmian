package com.benxiang.noodles.moudle.makemeal;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.benxiang.noodles.MainActivity;
import com.benxiang.noodles.R;
import com.benxiang.noodles.base.BaseActivity;
import com.benxiang.noodles.contants.Constants;
import com.benxiang.noodles.contants.MethodConstants;
import com.benxiang.noodles.model.ListModle;
import com.benxiang.noodles.model.NoodleTradeModel;
import com.benxiang.noodles.model.remote.OrderNumQueryModel;
import com.benxiang.noodles.moudle.makenoodle.NoodlesMakeActivity;
import com.benxiang.noodles.utils.JsonHelper;
import com.benxiang.noodles.utils.NoodleDataUtil;
import com.benxiang.noodles.utils.NoodleTradeFieldUtil;
import com.benxiang.noodles.utils.PreferenceUtil;
import com.benxiang.noodles.utils.ShotrNoUtil;
import com.benxiang.noodles.view.CustomerKeyboard;
import com.benxiang.noodles.view.NumberEditText;
import com.benxiang.noodles.widget.ErrorDialogFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import timber.log.Timber;

/**
 * Created by 刘圣如 on 2017/8/30.
 * 根据取餐号获取订单信息:GetBillHisByTakeNumber
 */

public class OrderMealNumActivity extends BaseActivity implements OrderMealNumView {

    private static final String TAG = "OrderMealNumActivity";
    @BindView(R.id.soft_keyboard)
    LinearLayout llKeyboard;
    @BindView(R.id.ed_password)
    NumberEditText edpassword;
    @BindView(R.id.ll_query_error)
    LinearLayout llQueryError;
    @BindView(R.id.ll_query_success)
    LinearLayout llQuerySuccess;
    //自定义控件
    private String passwordString = "";
    private CustomerKeyboard customerKeyboard;
    private boolean isRigth = true;
    private OrderMealNumPresenter mOrderMealNumPresenter;

    private NoodleTradeModel mNoodleTradeModel;
    private List<ListModle> listModles = new ArrayList<>();

    @Override
    public int getContentViewID() {
        return R.layout.activity_order_meal_num;
    }

    @Override
    protected void afterContentViewSet() {
        registerMainHandler();
        initView();
        setEnableCountdown(true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        edpassword.setText("");
        llQueryError.setVisibility(View.GONE);
        llQuerySuccess.setVisibility(View.GONE);
        speakOpening("请输入取餐号");
    }

    @Override
    protected void onStop() {
        super.onStop();
//        unDisposable();
        hideLoadingDialog();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mOrderMealNumPresenter != null) {
            mOrderMealNumPresenter.detavh();
            mOrderMealNumPresenter = null;
        }
    }

    private void initView() {
        mOrderMealNumPresenter = new OrderMealNumPresenter();
        mOrderMealNumPresenter.attachView(this);
        mNoodleTradeModel = new NoodleTradeModel();
        passwordString = "";
        edpassword.clearFocus();
        edpassword.disableShowSoftInput();
        customerKeyboard = new CustomerKeyboard(this);
        llKeyboard.addView(customerKeyboard.getInput());
        customerKeyboard.setKeyboardListener(new CustomerKeyboard.KeyboardListener() {
            @Override
            public void keyText(final String string) {
                passwordString = passwordString + string;
                Log.e(TAG, "keyText: " + string);
                if (string == "") {
                    edpassword.deleteLastPassword();
                } else {
                    edpassword.addPassword(string);
                }
            }
        });
    }

    //点击确定后查询订单号
    public void startQuery(View view) {
        if (edpasswordHint()) {
            queryOrderNo();
        }
    }

    private void startMake() {
            hintVisibility();
            showLoadingDialog();
            getMainHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Timber.e("几份食物 " + mNoodleTradeModel.listModles.size());
                    hideLoadingDialog();
                    Intent intent;
                    if (mNoodleTradeModel.hasEnoughIngredients){
                        intent = new Intent(OrderMealNumActivity.this, NoodlesMakeActivity.class);
                        //不抽奖，直接将抽奖信息设置为谢谢惠顾
                        NoodleTradeFieldUtil.setListModlesWithLottery(mNoodleTradeModel);
                        //写做面的数据到数据库中，开始做面
                        NoodleDataUtil.getOrderToDB(ShotrNoUtil.getShotrNo(true), mNoodleTradeModel);
                    }else {
                        intent = new Intent(OrderMealNumActivity.this, OrderQueryActiviity.class);
                    }
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("noodle", mNoodleTradeModel);
                    bundle.putParcelable("riceOrderND",mRiceOrderND);
                    intent.putExtra("test", 1);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }, 1000);
    }

    private void queryOrderNo() {
        OrderMealNumParam orderMealNumParam = new OrderMealNumParam();
        orderMealNumParam.shopCode = PreferenceUtil.config().getMacNo(Constants.MAC_NO);
        orderMealNumParam.TakeNumber = edpassword.getText().toString().trim();
        Timber.e("根据取餐号获取订单状态的接口参数:"+JsonHelper.getGson().toJson(orderMealNumParam));
        mOrderMealNumPresenter.orderQuery(MethodConstants.ORDER_NUM_IF, JsonHelper.getGson().toJson(orderMealNumParam));
    }

    private boolean edpasswordHint() {
        if (edpassword.getText().toString().length() < 6) {
            showError("请输入6位取餐号");
            return false;
        }
        return true;

    }

    private void hintVisibility() {
        if (isRigth) {
            llQuerySuccess.setVisibility(View.VISIBLE);
            llQueryError.setVisibility(View.GONE);
        } else {
            llQueryError.setVisibility(View.VISIBLE);
            llQuerySuccess.setVisibility(View.GONE);
        }
    }

    /*   public NoodleTradeModel getDatas() {

           return mNoodleTradeModel;
       }
   */
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
    public void queryMealNoSuccess(OrderNumQueryModel strMsg) {
        listModles.clear();
        Timber.e("查询成功");
    /*    mNoodleTradeModel.total_num = strMsg.billHis.BillItemCount;
        mNoodleTradeModel.total_price = (float) strMsg.billHis.OrderTotal;
        mNoodleTradeModel.take_meal_No = edpassword.getText().toString();
        mNoodleTradeModel.order_No = strMsg.billHis.BillNo;
        mNoodleTradeModel.pay_time = strMsg.billHis.BillDateTime;
        mNoodleTradeModel.pay_type = Integer.parseInt(strMsg.billHis.PayType);
        List<OrderNumQueryModel.BillHis.BillItems> billItemsList = strMsg.billHis.billItemsList;
        for (int i = 0; i < billItemsList.size(); i++) {
            ListModle listModle = new ListModle();
            listModle.goods_num = billItemsList.get(i).ItemCount;
            listModle.goods_name = billItemsList.get(i).ItemName;
            listModle.goods_prive = (float) billItemsList.get(i).ItemPrice;
            listModle.stock = true;
            listModles.add(listModle);
        }*/
        mNoodleTradeModel= NoodleDataUtil.getNoodleTradeModel(strMsg,edpassword.getText().toString().trim());
        startMake();
    }

    @Override
    public void queryMealNoFaile() {
        showCommonErrorDialog("返回首页", "网络异常，获取订单失败", new ErrorDialogFragment.OnErrorClickListener() {
            @Override
            public void onClick(ErrorDialogFragment dialog) {
                startNext(OrderMealNumActivity.this, MainActivity.class);
            }
        });
    }


}
