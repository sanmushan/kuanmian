package com.benxiang.noodles.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.benxiang.noodles.R;
import com.benxiang.noodles.contants.Constants;
import com.benxiang.noodles.contants.MethodConstants;
import com.benxiang.noodles.contants.OrderStatus;
import com.benxiang.noodles.data.DBNoodleHelper;
import com.benxiang.noodles.data.table.RiceOrderND;
import com.benxiang.noodles.moudle.makemeal.OrderNoStatusIDParam;
import com.benxiang.noodles.moudle.makemeal.OrderNoStatusParam;
import com.benxiang.noodles.moudle.makenoodle.ExceptionParam;
import com.benxiang.noodles.moudle.makenoodle.UplaodExView;
import com.benxiang.noodles.moudle.makenoodle.UploadExPresenter;
import com.benxiang.noodles.moudle.pay.PayParam;
import com.benxiang.noodles.serialport.bean.MakeNoodlesEvent;
import com.benxiang.noodles.utils.DataEncrypt;
import com.benxiang.noodles.utils.ErrorCodeUtil;
import com.benxiang.noodles.utils.JsonHelper;
import com.benxiang.noodles.utils.NoodleDataUtil;
import com.benxiang.noodles.utils.ParamObtainUtil;
import com.benxiang.noodles.utils.PreferenceUtil;
import com.benxiang.noodles.widget.ErrorDialogFragment;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * Created by 刘圣如 on 2017/12/4.
 */

public abstract class BaseActivity extends BaseRefundAct implements UplaodExView {

    private UploadExPresenter mUploadExPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    //初始化数据
    private void initData() {
        mUploadExPresenter = new UploadExPresenter();
        mUploadExPresenter.attachView(this);
    }


    //退款==》一单一单退
    private void toRefundment() {
        List<RiceOrderND> unDoneList = getUnDoneList();
        if (unDoneList.size() <= 0){
            Timber.e("没有要退款的,已经退完了");
            return;
        }

        String method = "";
        int payType = unDoneList.get(0).payType;
        method = (payType == Constants.ALIPAY) ? MethodConstants.RUNalipayREFUND: MethodConstants.RUNWXREFUND;
        Timber.e("退款金额:"+unDoneList.get(0).total_price);

        //根据排队号获得退款的每一单,查询后将这一单删除
        int sortNo = unDoneList.get(0).sortNo;
        mRiceOrderND = unDoneList.get(0);
        ArrayList<RiceOrderND> riceOrderNDs = DBNoodleHelper.queryBySortNo(sortNo);
        //获得这一单中还没做和正在做的数据,用于退款的判断
        ArrayList<RiceOrderND> riceOrder_no_finish = new ArrayList<>();
        for (int i=0;i<riceOrderNDs.size();i++){
            if (riceOrderNDs.get(i).noodleSign == Constants.SIGN_NOT_DO || riceOrderNDs.get(i).noodleSign == Constants.SIGN_DOING){
                riceOrder_no_finish.add(riceOrderNDs.get(i));
            }
        }
        //删除这一单
        DBNoodleHelper.deleteBySortNo(sortNo);

        PayParam payParam = ParamObtainUtil.getReturnPayParamMany(riceOrder_no_finish.get(0).total_price, riceOrder_no_finish, riceOrder_no_finish.get(0).payType);
        mUploadExPresenter.refundment(method, JsonHelper.getGson().toJson(payParam));
        Timber.e("initView: " + payParam.orderNo);
    }

    //获取退款的数据
    private List<RiceOrderND> getUnDoneList() {
        List<RiceOrderND> riceOrder_doing = DBNoodleHelper.queryWithoutNoodle(Constants.SIGN_DOING);
        List<RiceOrderND> riceOrder_not_do = DBNoodleHelper.queryWithoutNoodle(Constants.SIGN_NOT_DO);
        for (int i=0;i<riceOrder_not_do.size();i++){
            riceOrder_doing.add(riceOrder_not_do.get(i));
        }
        Timber.e("getUnDoneList: "+riceOrder_doing.size());
        return riceOrder_doing;
    }

    @Override
    public void UplaodExSuccess() {
        Timber.e(">>>>>>>>>上传异常成功");
    }

    @Override
    public void refundmentExSuccess() {
        dealWithRefundmentResult(getString(R.string.return_back));
        toRefundment();
    }

    @Override
    public void refundmentFaile() {
        dealWithRefundmentResult(getString(R.string.return_back_faile));
    }

    @Override
    public void retreaFoodSuccess() {

    }

    @Override
    public void retreaFoodFaile() {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showNetError(String error) {
        Timber.e(">>>>>>>>>具体错误信息:"+error);
    }

    @Override
    public void setOrderStatusSuccess() {
        setOrderStatusComplete();
    }

    @Override
    public void setOrderStatusFail() {

    }

    @Override
    public void setOrderStatusByIDSuccess() {
        if (mRiceOrderND.isSetBillStatusByTakeNumber){
            setOrderStatusComplete();
        }else {

        }
    }

    @Override
    public void setOrderStatusByIDFail() {

    }

    private void dealWithRefundmentResult(String data) {
        if (mRiceOrderND.isSetBillStatusByTakeNumber){
            OrderNoStatusParam orderNoStatusParam = ParamObtainUtil.getOrderNoStatusParam(OrderStatus.ORDER_CANCEL, mRiceOrderND.take_meal_No);
            mUploadExPresenter.setOrderNoStatus(MethodConstants.ORDER_STATUS, JsonHelper.getGson().toJson(orderNoStatusParam));
        }else {
            OrderNoStatusIDParam orderNoStatusParam = ParamObtainUtil.getOrderNoStatusIDParam(OrderStatus.ORDER_CANCEL, mRiceOrderND.take_meal_No);
            mUploadExPresenter.setOrderNoStatusByGuid(MethodConstants.ORDER_STATUS_ID, JsonHelper.getGson().toJson(orderNoStatusParam));
        }

        showWarningDialog("确定", data, new ErrorDialogFragment.OnErrorClickListener() {
            @Override
            public void onClick(ErrorDialogFragment dialog) {
                showLoadingDialog();
                getMainHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        killLoading();
                        StartBanner();
                    }
                }, 3000);
            }
        });
    }

    //接收数据
    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onCardEvent(final MakeNoodlesEvent makeNoodlesEvent) {
        if (makeNoodlesEvent == null)
            return;
        boolean isError = makeNoodlesEvent.isError;
        boolean isAllFinish = makeNoodlesEvent.isAllFinish;
        if (isError){
            Timber.e("错误码是"+makeNoodlesEvent.errorCode+",具体错误信息为:"+ ErrorCodeUtil.parse(makeNoodlesEvent.errorCode));
            dealwithError(makeNoodlesEvent.errorCode);
            return;
        }
        if (isAllFinish){
            Timber.e("面已全部完成,最后一碗面的米粉号是:"+makeNoodlesEvent.riceOrderND.noodleNo+",米粉类型是:"+makeNoodlesEvent.riceOrderND.noodleState);
            showTitle(makeNoodlesEvent.riceOrderND);
            setOrderStatusComplete();
        }else {
            Timber.e("有一碗面已完成,米粉号是:"+makeNoodlesEvent.riceOrderND.noodleNo+",米粉类型是:"+makeNoodlesEvent.riceOrderND.noodleState);
            showTitle(makeNoodlesEvent.riceOrderND);
        }
    }

    //机器异常统一处理==>上传异常，弹框，取消弹框，退款（设置订单状态为取消）
    private void dealwithError(String errorCode){
        uploadMacException(ErrorCodeUtil.parse(errorCode));
        List<RiceOrderND> riceOrder_not_do = DBNoodleHelper.queryWithoutNoodle(Constants.SIGN_NOT_DO);
        NoodleDataUtil.addRiceDbMany(riceOrder_not_do);
        showError("机器出错，退款中...");
        getMainHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                killAllErrorDialogs();
                toRefundment();
            }
        }, 5000);
    }

    //提交异常
    private void uploadMacException(String errorData) {
        ExceptionParam exceptionParam = new ExceptionParam();
            exceptionParam.mechanical_num = MethodConstants.SHOPCODE;
        Timber.e("机器编码：" + exceptionParam.mechanical_num);
        exceptionParam.abnormal_level = "1";
        exceptionParam.abnormal_type = "机器异常";
        exceptionParam.abnormal_detail = "机器异常" + errorData;
        exceptionParam.time = DataEncrypt.dataFormatString();
        exceptionParam.merchant_id = "1";
        exceptionParam.remark = "机器异常,请赶紧解决";
        exceptionParam.LIDCode = MethodConstants.SHOPCODE;
        Timber.e("上传异常的参数"+JsonHelper.getGson().toJson(exceptionParam));
        mUploadExPresenter.uploadException(MethodConstants.UPDATEMCHNO, JsonHelper.getGson().toJson(exceptionParam));
        //设置用户不可继续点餐
        PreferenceUtil.config().setBooleanValue(Constants.MACHINE_EXCEPTION, true);
    }

    //设置订单状态为全部完成
    private void setOrderStatusComplete() {
        List<RiceOrderND> riceOrder_has_done = DBNoodleHelper.queryWithoutNoodle(Constants.SIGN_HAS_DONE);
        if (riceOrder_has_done.size()<=0){
            Timber.e("订单状态为全部完成");
            return;
        }
        RiceOrderND riceOrderND = riceOrder_has_done.get(0);
        int sortNo = riceOrderND.sortNo;
        DBNoodleHelper.deleteBySortNo(sortNo);

        if (riceOrderND.isSetBillStatusByTakeNumber){
            OrderNoStatusParam orderNoStatusParam = ParamObtainUtil.getOrderNoStatusParam(OrderStatus.ORDER_COMPLETE, riceOrderND.take_meal_No);
            mUploadExPresenter.setOrderNoStatus(MethodConstants.ORDER_STATUS, JsonHelper.getGson().toJson(orderNoStatusParam));
        }else {
            OrderNoStatusIDParam orderNoStatusParam = ParamObtainUtil.getOrderNoStatusIDParam(OrderStatus.ORDER_COMPLETE, riceOrderND.take_meal_No);
            mUploadExPresenter.setOrderNoStatusByGuid(MethodConstants.ORDER_STATUS_ID,JsonHelper.getGson().toJson(orderNoStatusParam));
        }

    }

}
