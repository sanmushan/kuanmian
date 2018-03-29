package com.benxiang.noodles.moudle.makemeal;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.benxiang.noodles.AppApplication;
import com.benxiang.noodles.BuildConfig;
import com.benxiang.noodles.R;
import com.benxiang.noodles.base.SetListActivity;
import com.benxiang.noodles.contants.Constants;
import com.benxiang.noodles.contants.MethodConstants;
import com.benxiang.noodles.contants.OrderStatus;
import com.benxiang.noodles.model.ListModle;
import com.benxiang.noodles.model.NoodleTradeModel;
import com.benxiang.noodles.moudle.makenoodle.NoodlesMakeActivity;
import com.benxiang.noodles.moudle.pay.PayParam;
import com.benxiang.noodles.utils.JsonHelper;
import com.benxiang.noodles.utils.NoodleDataUtil;
import com.benxiang.noodles.utils.NoodleTradeFieldUtil;
import com.benxiang.noodles.utils.ParamObtainUtil;
import com.benxiang.noodles.utils.ShotrNoUtil;
import com.benxiang.noodles.widget.ErrorDialogFragment;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import timber.log.Timber;

/**
 * Created by 刘圣如 on 2017/9/4.
 */

public class OrderQueryActiviity extends SetListActivity implements QrderQueryView {
    private static final String TAG = "OrderQueryActiviity";
    @BindView(R.id.tv_meal_number)
    TextView tvMealNo;
    @BindView(R.id.tv_order_amount)
    TextView tvOrderAmount;
    @BindView(R.id.tv_order_number)
    TextView tvOrderNo;
    @BindView(R.id.tv_order_pay_data)
    TextView tvPayData;
    private CommonAdapter<ListModle> mAdapter;
    private OrderQueryPresenter mOrderQueryPresenter;

    private NoodleTradeModel mNoodleTradeModel;
    private List<ListModle> listModles = new ArrayList<>();
    private List<ListModle> toBackListModles;
    private boolean isMake = false;
    @Override
    public int getContentViewID() {
        return R.layout.activity_order_query_success;
    }

    @Override
    protected void afterContentViewSet() {
        registerMainHandler();
        initView();
        super.afterContentViewSet();
        setEnableCountdown(true);
    }

    private void initView() {
        mNoodleTradeModel = getIntent().getParcelableExtra("noodle");
        mOrderQueryPresenter = new OrderQueryPresenter();
        mOrderQueryPresenter.attachView(this);
        initDtas(mNoodleTradeModel);
        Log.e(TAG, "initView: " + mNoodleTradeModel.total_num);
        if (mNoodleTradeModel != null) {
            tvMealNo.setText(getString(R.string.meal_number, mNoodleTradeModel.take_meal_No));
            tvOrderNo.setText(getString(R.string.order_number, mNoodleTradeModel.order_No));
            tvOrderAmount.setText(getString(R.string.order_amount, mNoodleTradeModel.total_price));
            tvPayData.setText(getString(R.string.order_pay_data, mNoodleTradeModel.pay_time));
        }

        //适配器
        mAdapter = new CommonAdapter<ListModle>(AppApplication.getAppContext(), R.layout.item_recycle_goods_selected, listModles) {

            @Override
            protected void convert(final ViewHolder holder, ListModle listModle, int position) {
                String stockStr = listModle.stock?"":"(售罄)";
                final int[] goods_num = {1};
                holder.setText(R.id.tv_select_goods_name, listModle.goods_name+stockStr);
                holder.setText(R.id.tv_select_goods_price, getString(R.string.money_desc, listModle.goods_prive));
                holder.setText(R.id.tv_select_goods_num, "" + goods_num[0]);
                holder.setBackgroundRes(R.id.goods_selected_icon, R.drawable.in_stock);
                holder.setVisible(R.id.line_no_good, !listModle.stock);
                holder.setVisible(R.id.tv_select_no_goods_hint, !listModle.stock);
            }
        };
        setAdapter(mAdapter, true);
    }

    private void initDtas(NoodleTradeModel mNoodleTradeModel) {
        if (mNoodleTradeModel != null) {
            listModles = mNoodleTradeModel.listModles;
            Log.e(TAG, "initDtas: " + listModles.size());
        }
    }

    public void startNext(View view) {
        switch (view.getId()) {
            case R.id.btn_drawback:
                //全部退款
                startDrawback();
                break;
            case R.id.btn_start_make:
                //开始制作
                startmaking();
                break;
        }
    }

    private void startDrawback() {
        if (mNoodleTradeModel == null || listModles == null) {
            return;
        }
        RetreatFood(mNoodleTradeModel.listModles);
        skipHandle();
    }

    private void startmaking() {
        //可制作列表
        final List<ListModle> makeListModles = NoodleDataUtil.changeListModles(mNoodleTradeModel.listModles, true);
        //不能制作列表
        toBackListModles = NoodleDataUtil.changeListModles(mNoodleTradeModel.listModles, false);

        Timber.e("面条数量" + listModles.size());
        if (mNoodleTradeModel.listModles.size() < 1) {
            returnBanner();
        } else {
            getMainHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    isMake = true;
                    if (toBackListModles.size()>0) {
                        showWarningDialog("确定", getString(R.string.part_understock), true, true, new ErrorDialogFragment.OnErrorClickListener() {
                            @Override
                            public void onClick(ErrorDialogFragment dialog) {
                                mNoodleTradeModel.listModles = makeListModles;
                                //不抽奖，直接将抽奖信息设置为谢谢惠顾
                                NoodleTradeFieldUtil.setListModlesWithLottery(mNoodleTradeModel);
                                RetreatFood(toBackListModles);
                            }
                        });
                    }else {
                        mNoodleTradeModel.listModles = makeListModles;
                        //不抽奖，直接将抽奖信息设置为谢谢惠顾
                        NoodleTradeFieldUtil.setListModlesWithLottery(mNoodleTradeModel);
                        Log.e(TAG, "可做面的数量 "+makeListModles.size() );
                        if (BuildConfig.IS_MANY_POWL){
                            //写做面的数据到数据库中，开始做面
                            NoodleDataUtil.getOrderToDB(ShotrNoUtil.getShotrNo(true), mNoodleTradeModel);
                        }
                        startNext(OrderQueryActiviity.this, NoodlesMakeActivity.class, mNoodleTradeModel);
                    }
                }
            }, 3000);
        }

    }

    /* public void RetreatFood(List<ListModle> listModles) {
         RetreatFoodParam retreatFoodParam = ParamObtainUtil.getRetreadFood(mNoodleTradeModel, listModles);
         mOrderQueryPresenter.RetreatFood(MethodConstants.BACKORDERITEM, JsonHelper.getGson().toJson(retreatFoodParam));
     }*/
    public void RetreatFood(List<ListModle> listModles) {
        int payType = 0;
        String method = "";
        if (mNoodleTradeModel.pay_type==2){
            payType = Constants.WEIXIN;
            method = MethodConstants.RUNWXREFUND;
        }else {
            payType = Constants.ALIPAY;
            method = MethodConstants.RUNalipayREFUND;
        }
        PayParam payParam = ParamObtainUtil.getReturnPayParam(mNoodleTradeModel, listModles,payType);
        mOrderQueryPresenter.refundment(method, JsonHelper.getGson().toJson(payParam));
    }

    private void returnBanner() {
        showWarningDialog("确定",getString(R.string.understock),true,false);
        getMainHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                killAllErrorDialogs();

            }
        }, 5000);
    }

    public void skipHandle() {
        getMainHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                killAllErrorDialogs();

            }
        }, 2000);
        showLoadingDialog();
        getMainHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                StartBanner();
            }
        }, 3000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        listModles = null;
        if (mAdapter != null) {
            mAdapter = null;
        }
        if (mOrderQueryPresenter != null) {
            mOrderQueryPresenter.detavh();
            mOrderQueryPresenter = null;
        }
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
    public void RetreatFoodSuccess() {
        if (!isMake) {
            showWarningDialog("确定", getString(R.string.return_back));
            mOrderQueryPresenter.setOrderNoStatus(MethodConstants.ORDER_STATUS, JsonHelper.getGson().toJson(ParamObtainUtil.
                    getOrderNoStatusParam(OrderStatus.ORDER_CANCEL, mNoodleTradeModel.take_meal_No)));
        }
        else {
            showWarningDialog("确定", getString(R.string.return_back_kk));
//            skipHandle();
            getMainHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    killAllErrorDialogs();
                    showLoadingDialog();
                    startNext(OrderQueryActiviity.this, NoodlesMakeActivity.class, mNoodleTradeModel);
                }
            }, 2000);

        }
    }

    @Override
    public void setOrderNoStatusSuccess() {
//        StartBanner();
    }

    @Override
    public void refundmentSuccess() {
        if (!isMake) {
            showWarningDialog("确定", getString(R.string.return_back));
            mOrderQueryPresenter.setOrderNoStatus(MethodConstants.ORDER_STATUS, JsonHelper.getGson().toJson(ParamObtainUtil.
                    getOrderNoStatusParam(OrderStatus.ORDER_CANCEL, mNoodleTradeModel.take_meal_No)));
        }
        else {
            showWarningDialog("确定", getString(R.string.return_back_kk));
//            skipHandle();
            getMainHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    killAllErrorDialogs();
                    showLoadingDialog();
                    if (BuildConfig.IS_MANY_POWL){
                        //写做面的数据到数据库中，开始做面
                        NoodleDataUtil.getOrderToDB(ShotrNoUtil.getShotrNo(true), mNoodleTradeModel);
                    }
                    startNext(OrderQueryActiviity.this, NoodlesMakeActivity.class, mNoodleTradeModel);
                }
            }, 2000);

        }
    }

    @Override
    public void refundmentFaile() {
        showWarningDialog("确定",getString(R.string.return_back_faile));
    }
}
