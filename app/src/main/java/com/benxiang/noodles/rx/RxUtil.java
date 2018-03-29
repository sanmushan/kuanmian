package com.benxiang.noodles.rx;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Tairong Chan on 2017/3/21.
 * Connect:
 */

public class RxUtil {

  public static <T> ObservableTransformer<T, T> toMain() {
    return new ObservableTransformer<T, T>() {
      @Override
      public ObservableSource<T> apply(Observable<T> upstream) {
        return upstream.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
      }
    };
  }

  public static <T> ObservableTransformer<T, T> allInIO() {
    return new ObservableTransformer<T, T>() {
      @Override
      public ObservableSource<T> apply(Observable<T> upstream) {
        return upstream.subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io());
      }
    };
  }
}
