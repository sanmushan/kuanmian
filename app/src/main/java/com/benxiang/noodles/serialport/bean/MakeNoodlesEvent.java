package com.benxiang.noodles.serialport.bean;

import com.benxiang.noodles.data.table.RiceOrderND;

/**
 * Created by admin on 2017/6/28.
 */

public class MakeNoodlesEvent {

    public boolean isError = false;
    public String errorCode = "0";
    public boolean isAllFinish = false;
    public RiceOrderND riceOrderND = null;

    public MakeNoodlesEvent(boolean isError,String errorCode) {
        this.errorCode = errorCode;
        this.isError = isError;
    }

    public MakeNoodlesEvent(boolean isAllFinish, RiceOrderND riceOrderND) {
        this.isAllFinish = isAllFinish;
        this.riceOrderND = riceOrderND;
    }
}
