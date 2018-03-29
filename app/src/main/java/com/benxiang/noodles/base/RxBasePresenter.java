package com.benxiang.noodles.base;

import java.util.logging.Handler;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by 刘圣如 on 2017/9/6.
 */

public class RxBasePresenter<V extends BaseView> implements BasePresenter<V> {
    public V view;
    protected CompositeDisposable mCompositeDisposable;

    protected void addSubscribe(Disposable disposable) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(disposable);
    }

    protected void unSubscride() {
        if (mCompositeDisposable != null) {
            mCompositeDisposable.clear();
            mCompositeDisposable = null;
        }
    }

    @Override
    public void attachView(V view) {
        this.view = view;
    }

    @Override
    public void detavh() {
        unSubscride();
        view = null;
    }
}
