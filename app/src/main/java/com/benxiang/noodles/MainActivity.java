package com.benxiang.noodles;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.benxiang.noodles.base.BaseActivity;
import com.benxiang.noodles.contants.Constants;
import com.benxiang.noodles.contants.DbTypeContants;
import com.benxiang.noodles.contants.MethodConstants;
import com.benxiang.noodles.model.NoodleTradeModel;
import com.benxiang.noodles.model.information.InformationModle;
import com.benxiang.noodles.model.information.InformationParam;
import com.benxiang.noodles.model.information.InformationPresenter;
import com.benxiang.noodles.model.information.InformationView;
import com.benxiang.noodles.model.remote.CommonModel;
import com.benxiang.noodles.moudle.config.MenageActivity;
import com.benxiang.noodles.moudle.makemeal.OrderMealNumActivity;
import com.benxiang.noodles.moudle.selectnoodles.SelectNoodlesActivity;
import com.benxiang.noodles.utils.ActivityManager;
import com.benxiang.noodles.utils.JsonHelper;
import com.benxiang.noodles.utils.NoodleDataUtil;
import com.benxiang.noodles.utils.PreferenceUtil;
import com.benxiang.noodles.widget.ErrorDialogFragment;
import com.benxiang.noodles.widget.SecretDialogFragment;

import butterknife.BindView;
import timber.log.Timber;

public class MainActivity extends BaseActivity implements SecretDialogFragment.CallBack, InformationView {

    @BindView(R.id.tv_hide)
    TextView tvHide;
    private Object data;
    private int buttonId;
    private InformationPresenter mInformationPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getContentViewID() {
        return R.layout.activity_main;
    }

    @Override
    protected void afterContentViewSet() {
        registerMainHandler();
        setTvCountdownVisible(false);
        initView();
        //获取物品信息
        mInformationPresenter = new InformationPresenter();
        mInformationPresenter.attachView(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mInformationPresenter != null) {
            mInformationPresenter.detavh();
            mInformationPresenter = null;
        }
        //退出app时，清空sp缓存
        ActivityManager.AppExit(MainActivity.this);
    }

    private void initView() {
        tvHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stratManagement();
            }
        });
    }

    private void startNextActivity() {
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
        } else {
            switch (buttonId) {
                case R.id.btn_order:
                    startOrder();
                    break;
                case R.id.btn_take_meal:
                    startTameMeal();
                    break;
                default:
            }
        }
    }


    long lastTime;
    int chickTotal;

    private void stratManagement() {
        long startTime = System.currentTimeMillis();
        if (startTime - lastTime < 500) {
            chickTotal++;
        } else {
            chickTotal = 0;
        }
        lastTime = startTime;
        if (chickTotal == 7) {
            showSecretDialog();
        }
    }

    private SecretDialogFragment secretDialog;

    private void showSecretDialog() {
        if (secretDialog == null) {
            secretDialog = SecretDialogFragment.newInstance();
        }
        if (!secretDialog.isAdded()) {
            secretDialog.show(getFragmentManager(), "secret");
        }
    }

    @Override
    public void onCheckSuccess() {
        secretDialog.dismiss();
        startNext(MainActivity.this, MenageActivity.class);
    }

    public void select(View view) {
        closeButtonStatu();
        switch (view.getId()) {
            case R.id.btn_order:
                buttonId = R.id.btn_order;
                startNextActivity();
                break;
            case R.id.btn_take_meal:
                buttonId = R.id.btn_take_meal;
                startNextActivity();
                break;
            default:
        }
    }

    private void closeButtonStatu() {

    }

    private void startOrder() {
        InformationParam informationParam = new InformationParam();
        informationParam.shopCode = MethodConstants.SHOPCODE;
        mInformationPresenter.getInformation(MethodConstants.GETMENU, JsonHelper.getGson().toJson(informationParam));
    }

    //输入取餐号界面
    private void startTameMeal() {
        Intent intent = new Intent(MainActivity.this, OrderMealNumActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("riceOrderND", mRiceOrderND);
        intent.putExtras(bundle);
        startActivity(intent);
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
    public void getInformationSuccess(CommonModel<InformationModle> commonModel) {
        Timber.e("获取到的物品的信息:" + JsonHelper.getGson().toJson(commonModel));
        //TODO LIN 获取面食类型，传到对应的fragment中
        Intent intent = new Intent(MainActivity.this, SelectNoodlesActivity.class);
        NoodleTradeModel modelNoodle = NoodleDataUtil.getData(commonModel.strMsg.menuData.get(0), DbTypeContants.MIANTIAO, 1);
        //小类类型的数量
        int noodleSice = commonModel.strMsg.menuData.get(0).menuItemData.size();
        NoodleTradeModel modelrice = NoodleDataUtil.getData(commonModel.strMsg.menuData.get(1), DbTypeContants.MIFEN, noodleSice + 1);

        int freshSice = commonModel.strMsg.menuData.get(1).menuItemData.size();
        NoodleTradeModel modelFresh = NoodleDataUtil.getData(commonModel.strMsg.menuData.get(2), DbTypeContants.FRESH_NOODLES, freshSice + 1);

        Log.e("对象", "startOrder: " + modelNoodle.listModles.size());
        Bundle bundle = new Bundle();
        bundle.putParcelable("noodle", modelNoodle);
        bundle.putParcelable("rice", modelrice);

        bundle.putParcelable("fresh", modelFresh);

        bundle.putParcelable("riceOrderND", mRiceOrderND);
        intent.putExtras(bundle);
        startActivity(intent);
    }


}