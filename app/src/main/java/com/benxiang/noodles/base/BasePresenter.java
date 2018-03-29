package com.benxiang.noodles.base;

/**
 * Created by 刘圣如 on 2017/9/6.
 */

public interface BasePresenter<V extends BaseView> {
    void attachView(V view);

    void detavh();

}
