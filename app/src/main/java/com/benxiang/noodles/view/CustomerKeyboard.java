package com.benxiang.noodles.view;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.benxiang.noodles.R;
import com.benxiang.noodles.view.model.SoftButtonModel;

/**
 * Created by 刘圣如 on 2017/8/30.
 */

public class CustomerKeyboard {
    private Context mContext;
    private KeyboardListener mKeyboardListener;

    private LinearLayout parentLayout;
    private LinearLayout numberLayout1;
    private LinearLayout numberLayout2;
    private LinearLayout numberLayout3;
    private LinearLayout numberLayout4;
    private int mWidth = 258;
    private int mHeigth = 154;

    public CustomerKeyboard(Context mContext) {
        this.mContext = mContext;
        initLayout();
        initButton();
    }

    public LinearLayout getInput() {
        return parentLayout;
    }

    private void initButton() {
        addButton(new SoftButtonModel(1, "1"), 1);
        addButton(new SoftButtonModel(1, "2"), 1);
        addButton(new SoftButtonModel(1, "3"), 1);
        addButton(new SoftButtonModel(1, "4"), 2);
        addButton(new SoftButtonModel(1, "5"), 2);
        addButton(new SoftButtonModel(1, "6"), 2);
        addButton(new SoftButtonModel(1, "7"), 3);
        addButton(new SoftButtonModel(1, "8"), 3);
        addButton(new SoftButtonModel(1, "9"), 3);
        addButton(new SoftButtonModel(1, "0"), 4);
        addButton(new SoftButtonModel(2, "11"), 4);
        ;

    }

    private void initLayout() {
        parentLayout = createVerticalLinear();
        numberLayout1 = createHorizontalLinear(1);
        numberLayout2 = createHorizontalLinear(2);
        numberLayout3 = createHorizontalLinear(2);
        numberLayout4 = createHorizontalLinear(2);
        parentLayout.addView(numberLayout1);
        parentLayout.addView(numberLayout2);
        parentLayout.addView(numberLayout3);
        parentLayout.addView(numberLayout4);
    }

    private LinearLayout createVerticalLinear() {
        return createLinear(LinearLayout.VERTICAL,1);
    }

    private LinearLayout createHorizontalLinear(int type) {
        return createLinear(LinearLayout.HORIZONTAL,type);
    }

    private LinearLayout createLinear(int horizontal,int type) {
        LinearLayout linearLayout = new LinearLayout(mContext);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if (type==2){
            params.topMargin=27;
        }
        linearLayout.setLayoutParams(params);
        linearLayout.setOrientation(horizontal);
        return linearLayout;
    }

    private void addButton(SoftButtonModel buttonModel, int num) {
        final Button button = new Button(mContext);
        button.setBackgroundResource(R.drawable.keyboard_button);
        button.setTextSize(TypedValue.COMPLEX_UNIT_PX, 70);
        button.setTextColor(Color.argb(255, 51, 51, 51));
        button.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        button.setMinimumWidth(0);
        button.setMinimumHeight(0);
        button.setHeight(mHeigth);
  /*      LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) button.getLayoutParams();
        lp.leftMargin=8;
        button.setLayoutParams(lp);*/
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mKeyboardListener != null) {
                    mKeyboardListener.keyText(button.getText().toString());
                }
            }
        });
        LinearLayout.LayoutParams lp = null;
        switch (buttonModel.type) {
            case 1:
                lp = new LinearLayout.LayoutParams(258, 154);
                button.setText(buttonModel.text);
                button.setWidth(mWidth);
                lp.setMargins(0, 0, 33, 0);
                break;
            case 2:
                lp = new LinearLayout.LayoutParams(532, 154);
                button.setBackgroundResource(R.drawable.keyboard_button_delete);
                button.setHeight(154);
                button.setWidth(532);
                break;
        }
        lp.setMargins(0, 0, 23, 0);
        switch (num) {
            case 1:
                numberLayout1.addView(button,lp);
                break;
            case 2:
                numberLayout2.addView(button,lp);
                break;
            case 3:
                numberLayout3.addView(button,lp);
                break;
            case 4:
                numberLayout4.addView(button,lp);
                break;

        }
    }

    public void setKeyboardListener(KeyboardListener l) {
        this.mKeyboardListener = l;
    }

    public interface KeyboardListener {
        void keyText(String string);
    }

}
