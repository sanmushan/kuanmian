package com.benxiang.noodles.moudle.config;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.benxiang.noodles.R;
import com.benxiang.noodles.base.BaseActivity;
import com.benxiang.noodles.contants.Constants;
import com.benxiang.noodles.contants.MethodConstants;
import com.benxiang.noodles.model.remote.MerchantModel;
import com.benxiang.noodles.model.remote.NullParam;
import com.benxiang.noodles.utils.JsonHelper;
import com.benxiang.noodles.utils.PreferenceUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by 刘圣如 on 2017/9/5.
 */

public class SettingActivity extends BaseActivity implements SettingView {
    private static final String TAG = "SettingActivity";
    private SettingPresenter settinPresenter;
    @BindView(R.id.id_spinner)
    Spinner spinner;
    @BindView(R.id.tv_lat)
    TextView tvlat;
    @BindView(R.id.tv_long)
    TextView tvlong;
    @BindView(R.id.ed_mac_no)
    EditText tvmchno;
    @BindView(R.id.ed_address)
    EditText address;
    ArrayAdapter<String> adapter;
    List<String> nameLists;
    private String macAddress;
    private String latitude;
    private String longitude;

    private int spinnerId;

    @Override
    public int getContentViewID() {
        return R.layout.activity_setting;
    }

    @Override
    protected void afterContentViewSet() {
        Log.e(TAG, "afterContentViewSet: ");
//        LocationUtil.initLocation().getLocation();
        registerMainHandler();
        initView();

    }

    //初始化数据
    private void initView() {
        settinPresenter = new SettingPresenter();
        settinPresenter.attachView(this);
        NullParam nullParam = new NullParam();
        settinPresenter.loadSpanner(MethodConstants.GETMERCHANT,JsonHelper.getGson().toJson(nullParam));
        latitude = PreferenceUtil.config().getFloatValue(Constants.LOCATIONLAT) + "";
        longitude = PreferenceUtil.config().getFloatValue(Constants.LOCATIONLONG) + "";
        tvlat.setText(getString(R.string.lat, latitude));
        tvlong.setText(getString(R.string.longt, longitude));
//        macAddress = DeviceUtils.getMacAddress().replace(":", "").toUpperCase();
       /* if (macAddress != null) {
            PreferenceUtil.config().setStringValue(Constants.MAC_NO, macAddress);
        }*/
//        tvmchno.setText(getString(R.string.mch_no, macAddress));
        spinnerId = PreferenceUtil.config().getIntValue(Constants.SPINNER_ID);


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
    public void showNetError(String error) {

    }

    //显示门店信息
    @Override
    public void showSpannerInfo(MerchantModel merchantModel) {
        nameLists = getNameList(merchantModel.merchantDatas);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, nameLists);
        spinner.setAdapter(adapter);
        spinner.setSelection(spinnerId, true);
        adapter.notifyDataSetChanged();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                PreferenceUtil.config().setIntValue(Constants.SPINNER_ID, position);
                Log.e(TAG, "onItemSelected: " + position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        Log.e(TAG, "showSpannerInfo: " + merchantModel.ResultMsg);
    }

    private List<String> getNameList(List<MerchantModel.MerchantData> datas) {
        List<String> list = new ArrayList<>();
        for (MerchantModel.MerchantData data : datas) {
            list.add(data.merchant_name);
        }
        return list;
    }

    @Override
    public void regiterSuccess() {
        hideLoading();
        getMainHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                StartBanner();
                startNext(SettingActivity.this, SettingTestActivity.class);
            }
        }, 1000);
    }

    public void addConfirm(View view) {
        String deviceAddress = address.getText().toString().trim();
        macAddress = tvmchno.getText().toString().trim();
        if (TextUtils.isEmpty(deviceAddress)||TextUtils.isEmpty(macAddress)) {
            Toast.makeText(this, "不能为空", Toast.LENGTH_SHORT).show();
        } else {
            PreferenceUtil.config().setStringValue(Constants.MAC_NO, macAddress);
            MechanicalParam mechanicalParam = new MechanicalParam();
            mechanicalParam.mechanical_num=macAddress;
            mechanicalParam.merchant_id=spinnerId+"";
            mechanicalParam.is_unusual=1+"";
            mechanicalParam.location=deviceAddress;
            mechanicalParam.longitude=longitude;
            mechanicalParam.latitude=latitude;
            settinPresenter.redigterMchNo(MethodConstants.MECHANICAL, JsonHelper.getGson().toJson(mechanicalParam));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (settinPresenter != null) {
            settinPresenter.detavh();
            settinPresenter = null;
        }
    }
}
