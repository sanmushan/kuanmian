
package com.benxiang.noodles.rx;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * Created by Tairong Chan on 2017/3/22.
 * Connect:
 */

public class RxRetry implements Function<Observable<? extends Throwable>, ObservableSource<?>> {

  private int retries;
  private long retryDelay;
  private TimeUnit timeUnit;
  private int retryCount;

  public RxRetry(int retries, long retryDelay, TimeUnit timeUnit) {
    this.retries = retries;
    this.retryDelay = retryDelay;
    this.timeUnit = timeUnit;
  }

  @Override
  public ObservableSource<?> apply(@NonNull Observable<? extends Throwable> observable)
      throws Exception {
    return observable.flatMap(new Function<Throwable, ObservableSource<?>>() {
      @Override
      public ObservableSource<?> apply(@NonNull Throwable throwable) throws Exception {
        if (++retryCount <= retries) {
          return Observable.timer(retryDelay, timeUnit);
        }
        return Observable.error(throwable);
      }
    });
  }
}
