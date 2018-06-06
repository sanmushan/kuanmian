package com.benxiang.noodles.moudle.makenoodle;

import android.text.Html;
import android.util.Log;
import android.widget.TextView;

import com.benxiang.noodles.BuildConfig;
import com.benxiang.noodles.R;
import com.benxiang.noodles.base.BaseActivity;
import com.benxiang.noodles.contants.Constants;
import com.benxiang.noodles.contants.DbTypeContants;
import com.benxiang.noodles.contants.MethodConstants;
import com.benxiang.noodles.contants.OrderStatus;
import com.benxiang.noodles.data.DBNoodleHelper;
import com.benxiang.noodles.data.noodle.NoodleEventData;
import com.benxiang.noodles.data.table.RiceND;
import com.benxiang.noodles.entrance.MakeNoodlesUtil;
import com.benxiang.noodles.entrance.ReadReceiveCardUtil;
import com.benxiang.noodles.entrance.RecycleNoodlesUtil;
import com.benxiang.noodles.entrance.SeasoningPackageUtil;
import com.benxiang.noodles.model.ListModle;
import com.benxiang.noodles.model.NoodleTradeModel;
import com.benxiang.noodles.model.lottery.CloseLotteryModel;
import com.benxiang.noodles.moudle.pay.PayParam;
import com.benxiang.noodles.serialport.bean.ErrorEvent;
import com.benxiang.noodles.utils.DataEncrypt;
import com.benxiang.noodles.utils.ErrorCodeUtil;
import com.benxiang.noodles.utils.JsonHelper;
import com.benxiang.noodles.utils.ParamObtainUtil;
import com.benxiang.noodles.utils.PreferenceUtil;
import com.benxiang.noodles.utils.ShotrNoUtil;
import com.benxiang.noodles.widget.ErrorDialogFragment;
import com.blankj.utilcode.util.DeviceUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import timber.log.Timber;

/**
 * Created by 刘圣如 on 2017/9/5.
 */

public class NoodlesMakeActivity extends BaseActivity implements UplaodExView {

    private static final String TAG = "NoodlesMakeActivity";
    @BindView(R.id.tv_noodle_make)
    TextView tv_noodle_make;

    private ReadReceiveCardUtil mReadReceiveCardUtil;//一单一碗的做面流程
    private SeasoningPackageUtil mSeasoningPackageUtil;//掉料包
    private MakeNoodlesUtil mMakeNoodlesUtil;//一单多碗的做面流程
    //    private ListModle listModle;
    private List<ListModle> listModles;
    private NoodleTradeModel noodleTradeModel;
//    private List<Integer> numberList = new ArrayList<>();
    private List<NoodleEventData> noodleEventDatas = new ArrayList<>();//作为传给做面接口的参数，包括做多少碗，取哪个号，是否加醋
    private RecycleNoodlesUtil recycleNoodlesUtil;//面没拿走会回收
    //判断是否全部做面完成
    private boolean isAllCompelte = false;
    //
    private int noodleNum = 0;

    //支付类型
    private int payWay;

    //完成的份数
    private int doneNo = 0;
    private UploadExPresenter mUploadExPresenter;

    @Override
    public int getContentViewID() {
        return R.layout.activity_noodles_make_wait;
    }

    @Override
    protected void afterContentViewSet() {
        initData();
        setTvCountdownVisible(false);
        registerMainHandler();
        tv_noodle_make.setText(Html.fromHtml(getString(R.string.noodles_make_waits, ShotrNoUtil.getShotrNo(false))));
    }

    //初始化数据
    private void initData() {
        mUploadExPresenter = new UploadExPresenter();
        mUploadExPresenter.attachView(this);
        noodleTradeModel = getIntent().getParcelableExtra("noodle");
        payWay = getIntent().getIntExtra("payType", 0);
        if (noodleTradeModel == null) {
            return;
        }
        listModles = noodleTradeModel.listModles;
//        listModles = NoodleDataUtil.getlistModles(noodleTradeModel.listModles);
        Timber.e("listModles几份面" + listModles.size());
//        Log.e("米粉的号数 ", "initData: " + listModle.goods_no);
    }

    //开始做面，语音提示多少号开始制作
    @Override
    protected void onStart() {
        super.onStart();
        speakText(ShotrNoUtil.getShotrNo(false) + "号已加入制作列表，请稍候");
        getMainHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                showLoading();
                StartBanner();
            }
        }, 8*1000);

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ErrorEvent event) {
        showError(event.getErrorText());
    }

    //一碗多面操作
    private void initMakeNoodels() {
        if (mMakeNoodlesUtil == null) {
            mMakeNoodlesUtil = new MakeNoodlesUtil() {

                @Override
                protected void dealWithError(String info) {
//                    NoodleDataUtil.addRiceDb(noodleEventDatas);
                    dealWithErrorCommand(info);
                }

                @Override
                protected void onFinish(final NoodleEventData noodleEventData) {
                    Timber.e("制作米粉完成,请取粉" + noodleEventData.noodle_no+"kouwei"+noodleEventData.noodle_state);
                    speakText(ShotrNoUtil.getShotrNo(false) + "号" + ShotrNoUtil.getShotrNo(false) + "号" +
                            ShotrNoUtil.getShotrNo(false) + "号" + noodleEventData.noodle_name +
                            "制作完成,请在一分钟内从取餐口取走");
                    showWarningDialog("确定", (getString(R.string.noodles_make_competle, ShotrNoUtil.getShotrNo(false)
                            , noodleEventData.noodle_name)), true,false);
                    getMainHandler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            killAllErrorDialogs();
                        }
                    }, 8000);

                    makinngNoodleDownTime();
                    doneNo++;
                }

                @Override
                protected void onAllFinish(final NoodleEventData noodleEventData) {
                    Timber.e("制作米粉完成,请取粉" + noodleEventData.noodle_no+"kouwei"+noodleEventData.noodle_state);
                    speakText(ShotrNoUtil.getShotrNo(false) + "号"+ ShotrNoUtil.getShotrNo(false) + "号" +
                            ShotrNoUtil.getShotrNo(false) + "号"  + noodleEventData.noodle_name
                            + "制作完成,请在一分钟内从取餐口取走" + + ShotrNoUtil.getShotrNo(false) + "号"
                            + ShotrNoUtil.getShotrNo(false) + "号" +ShotrNoUtil.getShotrNo(false) + "号已全部制作完成");
//                    speakText(ShotrNoUtil.getShotrNo(false) + "号已全部制作完成");
                    showWarningDialog("确定", (getString(R.string.noodles_make_competle, ShotrNoUtil.getShotrNo(false)
                            , noodleEventData.noodle_name)), true,false);
                    getMainHandler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            killAllErrorDialogs();
                            tv_noodle_make.setText(Html.fromHtml(getString(R.string.noodles_make_competle_all, ShotrNoUtil.getShotrNo(false))));

                        }
                    }, 8000);

                    Timber.e("制作米粉全部完成");
                    isAllCompelte = true;
                    doneNo++;
                    makinngNoodleDownTime();
                }

                @Override
                protected void tooMuchError() {
                    dealWithErrorCommand("66");
                    Timber.e("多次检查皮带出错");
                }

                @Override
                protected void dropPackage(NoodleEventData noodleEventData) {
                    mSeasoningPackageUtil.startDropPackage(noodleEventData);
                }

                @Override
                protected void choiceNoodles(int noodlesNo) {
                    mSeasoningPackageUtil.startChoiceNoodles(noodlesNo);
                }
            };
        }
        //扣库存
        takeRiceDbChange();
        mMakeNoodlesUtil.setNoodleEventDatas(noodleEventDatas);
        mMakeNoodlesUtil.startCheckBelt();
    }


    //设置倒计时时间，并开始倒计时
    private void makinngNoodleDownTime() {
        if (BuildConfig.DEBUG) {
            setEnableCountdown(true, 20);
        } else {
            setEnableCountdown(true, Constants.COUNT_DOWN_WAIT_TIME);
        }
        startDownTime();
        recycleNoodlesUtil.startCheckBox();
    }

    //回收过程
    private void initRecycleNoodles() {
//        recycle();
        if (recycleNoodlesUtil == null) {
            recycleNoodlesUtil = new RecycleNoodlesUtil() {
                @Override
                protected void dealWithError(String info) {
                    dealWithErrorCommand(info);
                    Timber.e(info);
                }

                @Override
                protected void onFinish() {
                    Timber.e("倒计时结束");
                    //第一碗完成后继续执行第二碗的相应指令
                    unDisposable();
                    mMakeNoodlesUtil.sendToDoor();
                    if (isAllCompelte) {
                        mUploadExPresenter.setOrderNoStatus(MethodConstants.ORDER_STATUS, JsonHelper.getGson().toJson(ParamObtainUtil.
                                getOrderNoStatusParam(OrderStatus.ORDER_COMPLETE, noodleTradeModel.take_meal_No)));
                        StartBanner();
                    }
                }
            };
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        recycle();
    }

    private void recycle() {
        if (mReadReceiveCardUtil != null) {
            mReadReceiveCardUtil.recycle();
            mReadReceiveCardUtil = null;
        }
        if (recycleNoodlesUtil != null) {
            recycleNoodlesUtil.recycle();
            recycleNoodlesUtil = null;
        }

        if (mSeasoningPackageUtil != null) {
            mSeasoningPackageUtil.recycle();
            mSeasoningPackageUtil = null;
        }

        if (mUploadExPresenter != null) {
            mUploadExPresenter.detavh();
            mUploadExPresenter = null;
        }

    }


    //获取传给下位机的数据(1)
    private void takeRiceDbChange() {
        getnumberList();
    }

    //获取传给下位机的数据(2)
    public void getnumberList() {
        for (int i = 0; i < listModles.size(); i++) {
            int num = listModles.get(i).goods_num;
            Timber.e("数量" + listModles.get(i).goods_num);
            for (int j = 0; j < num; j++) {
                switch (listModles.get(i).riceType) {
                    case 1:
                        dataChange(DbTypeContants.MIANTIAO, listModles.get(i),listModles.get(i).closeLotteryModels.get(j));
                        break;
                    case 2:
                        dataChange(DbTypeContants.MIFEN, listModles.get(i), listModles.get(i).closeLotteryModels.get(j));
                        break;
                        //新鲜面
                    case 8:
                        dataChange(DbTypeContants.FRESH_NOODLES, listModles.get(i), listModles.get(i).closeLotteryModels.get(j));
                        break;
                        default:

                }
            }
            Timber.e("做面的数量" + noodleEventDatas.size());
        }
    }

    //获取传给下位机的数据(3)
    private void dataChange(int miantiaoNo, ListModle listModle, CloseLotteryModel closeLotteryModel) {
        RiceND riceND = new RiceND();
        if (DBNoodleHelper.querynoodleStatusNoolde(miantiaoNo).size()>0) {
            riceND = DBNoodleHelper.querynoodleStatusNoolde(miantiaoNo).get(0);
        }
        NoodleEventData noodleEventData = new NoodleEventData();
        noodleEventData.noodle_no = riceND.noodleNo;
        noodleEventData.noodle_state = listModle.riceTaste;
        noodleEventData.noodle_name = listModle.goods_name;
        noodleEventData.spoilName = closeLotteryModel.spoilName;
        noodleEventDatas.add(noodleEventData);
        DBNoodleHelper.upateNoodleNum(riceND.noodleNo, riceND.totalNum - 1);
    }

    @Override
    protected void countdownOver() {
        Timber.e("倒计时结束,不循环检测，直接回收");
        recycleNoodlesUtil.setDirectRecycle(true);
    }

    //退款
    private void ToRefundment(int payWay) {
        String method = "";
        if (payWay == Constants.ALIPAY) {
            method = MethodConstants.RUNalipayREFUND;
        } else if (payWay == Constants.WEIXIN) {
            method = MethodConstants.RUNWXREFUND;
        }
        Timber.e("退款金额:"+noodleTradeModel.total_price);
        PayParam payParam = ParamObtainUtil.getReturnPayParam(noodleTradeModel, getUnDoneList(), payWay);
        mUploadExPresenter.refundment(method, JsonHelper.getGson().toJson(payParam));
        Log.e(TAG, "initView: " + payParam.orderNo);
    }

    //提交异常
    public void uploadMacException(String errorData) {
        ExceptionParam exceptionParam = new ExceptionParam();
        if (DeviceUtils.getMacAddress() != null) {
            exceptionParam.mechanical_num = DeviceUtils.getMacAddress().replace(":", "").toUpperCase();
        } else {
            exceptionParam.mechanical_num = MethodConstants.SHOPCODE;
        }
        Timber.e("机器编码：" + exceptionParam.mechanical_num);
        exceptionParam.abnormal_level = "1";
        exceptionParam.abnormal_type = "机器异常";
        exceptionParam.abnormal_detail = "机器异常" + errorData;
        exceptionParam.time = DataEncrypt.dataFormatString();
        exceptionParam.merchant_id = "1";
        exceptionParam.remark = "机器异常,请赶紧解决";
        Timber.e(exceptionParam.toString());
        mUploadExPresenter.uploadException(MethodConstants.UPDATEMCHNO, JsonHelper.getGson().toJson(exceptionParam));
        if (!BuildConfig.IS_MANY_POWL){
            PreferenceUtil.config().setBooleanValue(Constants.MACHINE_EXCEPTION, true);
        }
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showNetError(String error) {}

    @Override
    public void UplaodExSuccess() {
        PreferenceUtil.config().setBooleanValue(Constants.MACHINE_EXCEPTION, true);
    }

    @Override
    public void refundmentExSuccess() {
        dealWithRefundmentResult(getString(R.string.return_back));
    }

    @Override
    public void refundmentFaile() {
        dealWithRefundmentResult(getString(R.string.return_back_faile));
    }

    @Override
    public void retreaFoodSuccess() {
        dealWithRefundmentResult(getString(R.string.return_back));
    }

    @Override
    public void retreaFoodFaile() {
        dealWithRefundmentResult(getString(R.string.return_back_faile));
    }

    @Override
    public void setOrderStatusFail() {}

    @Override
    public void setOrderStatusSuccess() {
    }

    //做面出错后设置订单状态，弹框提示，在3秒后取消弹框
    private void dealWithRefundmentResult(String data) {
        mUploadExPresenter.setOrderNoStatus(MethodConstants.ORDER_STATUS, JsonHelper.getGson().toJson(ParamObtainUtil.
                getOrderNoStatusParam(OrderStatus.ORDER_CANCEL, noodleTradeModel.take_meal_No)));
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

    private void initDropSeasoningPackage() {
        mSeasoningPackageUtil = new SeasoningPackageUtil() {
            @Override
            protected void dealWithError(String errorCode) {
                dealWithErrorCommand(errorCode);

            }

            @Override
            protected void onFinish() {
                mMakeNoodlesUtil.openDoor();
            }

            @Override
            protected void onChoiceNoodlesFinish() {
                mMakeNoodlesUtil.setIsChoiceFinish(true);
            }
        };
    }

    //机器异常统一处理==>上传异常，弹框，取消弹框，退款（设置订单状态为取消）
    private void dealWithErrorCommand(String error) {
        uploadMacException(ErrorCodeUtil.parse(error));
        showError("机器出错，退款中...");
        getMainHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                killAllErrorDialogs();
//                retreaFood();
                ToRefundment(noodleTradeModel.pay_type);
            }
        }, 2000);
    }



    public List<ListModle> getUnDoneList() {
        List<ListModle> unDoneList = new ArrayList<>();
        for (int i = doneNo; i <listModles.size(); i++) {
            int num = listModles.get(i).goods_num;
            for (int j=0;j<num;j++) {
                ListModle listModle = listModles.get(i);
                unDoneList.add(listModle);
            }
        }
        Log.e(TAG, "getUnDoneList: "+unDoneList.size());
        return unDoneList;
    }
}
