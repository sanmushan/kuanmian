package com.benxiang.noodles.moudle.config;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.benxiang.noodles.R;
import com.benxiang.noodles.base.BaseMenageActivity;
import com.benxiang.noodles.contants.Constants;
import com.benxiang.noodles.utils.PreferenceUtil;
import com.benxiang.noodles.widget.ErrorDialogFragment;

import butterknife.BindView;

/**
 * Created by 刘圣如 on 2017/9/21.
 * 第一次安装应用的时候设置库存
 */

public class SettingTestActivity extends BaseMenageActivity {
    private static final String TAG = "SettingTestActivity";

    @BindView(R.id.ed_http_address)
    EditText ed_address;

    @Override
    public int getContentViewID() {
        return R.layout.activity_setting_first;
    }

    @Override
    protected void afterContentViewSet() {
        registerMainHandler();
    }


    public void addToDb(View view) {

        dataToDB();
    }

    @Override
    protected void runToNextPager() {
        getMainHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        hideLoadingDialog();
                        StartBanner();
                    }
                }, 3000);
    }

    public void saveAddress(View view) {
        if (edNullHint(ed_address)) {
            showWarningDialog("确定", "确定要修改后台地址", new ErrorDialogFragment.OnErrorClickListener() {
                @Override
                public void onClick(ErrorDialogFragment dialog) {
                    killAllErrorDialogs();
                    PreferenceUtil.config().setStringValue(Constants.HTTP_ADDRESS,ed_address.getText().toString().trim());
                    Toast.makeText(SettingTestActivity.this, "地址修改成功", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    private boolean edNullHint(EditText editText) {

        String textString = editText.getText().toString().trim();
        if (TextUtils.isEmpty(textString)) {
            showError("不能为空");
            return false;
        }
        return true;
    }

}
