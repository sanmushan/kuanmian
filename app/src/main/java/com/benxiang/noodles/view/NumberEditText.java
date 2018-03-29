package com.benxiang.noodles.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.InputType;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import com.benxiang.noodles.R;

/**
 * Created by 刘圣如 on 2017/8/30.
 */

public class NumberEditText extends EditText {
    //画布
    private Canvas mCanvas;
    //画笔
    private Paint mPaint;
    //一个密码所占的宽度
    private int mPasswordItemWith;
    //密码的个数默认是6个
    private int mPasswordNumber = 6;
    // 背景边框颜色
    private int mBgColor = Color.parseColor("#d1d2d6");
    // 背景边框大小
    private int mBgSize = 1;
    // 背景边框圆角大小
    private int mBgCorner = 0;
    // 分割线的颜色
    private int mDivisionLineColor = mBgColor;
    // 分割线的大小
    private int mDivisionLineSize = 1;
    //绘制文字的坐标
    private int mTextX = 45;
    private int mTextY = 88;
    private String myString;
    private char[] stringTextChar = new char[6];

    public NumberEditText(Context context) {
        super(context, null);
    }

    public NumberEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
        initAttributeSet(context, attrs);
        // 设置输入模式是密码
        setInputType(EditorInfo.TYPE_TEXT_VARIATION_PASSWORD);
        // 不显示光标
        setCursorVisible(false);
    }

    private void initAttributeSet(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.NumberEditText);
        // 获取大小
        mDivisionLineSize = (int) array.getDimension(R.styleable.NumberEditText_divisionLineSize, mDivisionLineSize);
        mBgSize = (int) array.getDimension(R.styleable.NumberEditText_bgSize, mBgSize);
        mBgCorner = (int) array.getDimension(R.styleable.NumberEditText_bgCorner, 0);
        // 获取颜色
        mBgColor = array.getColor(R.styleable.NumberEditText_bgColor, mBgColor);
        mDivisionLineColor = array.getColor(R.styleable.NumberEditText_divisionLineColor, mDivisionLineColor);
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mCanvas = canvas;
//        super.onDraw(canvas);
        int passwordWidth = getWidth() - (mPasswordNumber - 1) * mDivisionLineSize;
        mPasswordItemWith = passwordWidth / mPasswordNumber;
        // 绘制背景
        drawBg(canvas);
        // 绘制分割线
        drawDivisionLine(canvas);
        // 绘制密码
        drawPassword(canvas);
    }

    /**
     * 绘制隐藏的密码
     */
    private void drawPassword(Canvas canvas) {
        stringTextChar = getText().toString().toCharArray();
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setTextSize(60);
        for (int i = 0; i < getText().toString().length(); i++) {
            canvas.drawText(String.valueOf(stringTextChar[i]), mTextX + 114 * i, mTextY, mPaint);
        }
    }

    /**
     * 绘制背景
     */
    private void drawBg(Canvas canvas) {
        mPaint.setColor(mBgColor);
        // 设置画笔为空心
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mBgSize);
        RectF rectF = new RectF(mBgSize, mBgSize, getWidth() - mBgSize, getHeight() - mBgSize);
        // 如果没有设置圆角，就画矩形
        if (mBgCorner == 0) {
            canvas.drawRect(rectF, mPaint);
        } else {
            // 如果有设置圆角就画圆矩形
            canvas.drawRoundRect(rectF, mBgCorner, mBgCorner, mPaint);
        }

    }

    /**
     * 绘制文字显示位置
     */
    private void drawDivisionLine(Canvas canvas) {
        mPaint.setStrokeWidth(mDivisionLineSize);
        mPaint.setColor(mDivisionLineColor);
        for (int i = 0; i < mPasswordNumber - 1; i++) {
            int startX = (i + 1) * mDivisionLineSize + (i + 1) * mPasswordItemWith + mBgSize;
            canvas.drawLine(startX, mBgSize, startX, getHeight() - mBgSize, mPaint);
        }
    }

    /**
     * dip 转 px
     */
    private int dip2px(int dip) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dip, getResources().getDisplayMetrics());
    }

    public void addPassword(String number) {
        number = getText().toString().trim() + number;
        if (number.length() > mPasswordNumber) {
            return;
        }
        setText(number);
        invalidate();
    }

    /**
     * 删除最后一位密码
     */
    public void deleteLastPassword() {
        Log.e("啦啦", "deleteLastPassword: ");
        String currentText = getText().toString().trim();
        if (TextUtils.isEmpty(currentText)) {
            return;
        }
        currentText = currentText.substring(0, currentText.length() - 1);
        setText(currentText);
        invalidate();
    }

    public void disableShowSoftInput() {
        if (android.os.Build.VERSION.SDK_INT <= 10) {
            setInputType(InputType.TYPE_NULL);
        } else {
            setShowSoftInputOnFocus(false);
        }
    }
}