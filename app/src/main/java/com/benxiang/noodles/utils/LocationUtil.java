package com.benxiang.noodles.utils;

import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.benxiang.noodles.AppApplication;
import com.benxiang.noodles.contants.Constants;

/**
 * Created by 刘圣如 on 2017/9/6.
 */

public class LocationUtil {
    private static LocationUtil locationUtil;
    private LocationClient mLocationClient;
    private BDLocationListener mBDLocationListener;

    private LocationUtil() {
        mLocationClient = new LocationClient(AppApplication.getAppContext());
        mBDLocationListener = new MyBDLocationListener();
        // 注册监听
        mLocationClient.registerLocationListener(mBDLocationListener);
    }

    public static LocationUtil initLocation() {
        if (locationUtil == null) {
            synchronized (LocationUtil.class) {
                if (locationUtil == null) {
                    locationUtil = new LocationUtil();
                }
            }
        }
        return locationUtil;
    }
    /** 获得所在位置经纬度及详细地址 */
    public void getLocation() {
        // 声明定位参数
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);// 设置定位模式 高精度
        option.setCoorType("bd09ll");// 设置返回定位结果是百度经纬度 默认gcj02
        option.setScanSpan(5000);// 设置发起定位请求的时间间隔 单位ms
        option.setIsNeedAddress(true);// 设置定位结果包含地址信息
        option.setNeedDeviceDirect(true);// 设置定位结果包含手机机头 的方向
        // 设置定位参数
        mLocationClient.setLocOption(option);
        // 启动定位
        mLocationClient.start();

    }
    private class MyBDLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // 非空判断
            if (location != null) {
                // 根据BDLocation 对象获得经纬度以及详细地址信息
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                PreferenceUtil.config().setFloatValue(Constants.LOCATIONLAT, (float)latitude);
                PreferenceUtil.config().setFloatValue(Constants.LOCATIONLONG, (float)longitude);
                String address = location.getAddrStr();
                Log.i("啦啦啦", "address:" + address + " latitude:" + (float)latitude
                        + " longitude:" + (float)longitude + "---");
                if (mLocationClient.isStarted()) {
                    // 获得位置之后停止定位
                    mLocationClient.stop();
                }
            }
        }
    }
    public void onDestroy() {
        // 取消监听函数
        if (mLocationClient != null) {
            mLocationClient.unRegisterLocationListener(mBDLocationListener);
        }
    }
}
