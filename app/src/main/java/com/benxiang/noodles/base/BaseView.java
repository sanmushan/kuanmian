package com.benxiang.noodles.base;

/**
 * Created by 刘圣如 on 2017/9/6.
 */

public interface BaseView {
    void showLoading();

    void hideLoading();

    void showNetError(String error);
}
