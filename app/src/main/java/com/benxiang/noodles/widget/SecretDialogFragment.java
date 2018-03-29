package com.benxiang.noodles.widget;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.app.DialogFragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.benxiang.noodles.R;

/**
 * Created by 刘圣如 on 2017/10/19.
 */

public class SecretDialogFragment extends DialogFragment {
    private CallBack callBack;

    public static SecretDialogFragment newInstance() {
        SecretDialogFragment secretDialogFragment = new SecretDialogFragment();
        return secretDialogFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callBack = (CallBack) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callBack = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_secret, null, false);
        Button confirm = (Button) view.findViewById(R.id.btn_confirm_pwd);
        Button cancle = (Button) view.findViewById(R.id.btn_cancel);
        final TextView etPwd = (TextView) view.findViewById(R.id.et_pwd);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SecretDialogFragment.this.checkPwd(etPwd);
            }
        });
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        return alertDialog;
    }

    private void checkPwd(TextView etPwd) {
        String pwd = etPwd.getText().toString();
        if (!TextUtils.isEmpty(pwd)) {
            if (pwd.equals("12301230")) {
                if (callBack != null) {
                    callBack.onCheckSuccess();
                }
            } else {
                etPwd.setText("");
            }
        }
    }



    public interface CallBack {
        void onCheckSuccess();
    }
}
