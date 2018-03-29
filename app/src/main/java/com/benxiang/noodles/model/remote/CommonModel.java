package com.benxiang.noodles.model.remote;

import com.google.gson.annotations.SerializedName;

/**
 * Created by 刘圣如 on 2017/9/5.
 */

public class CommonModel<V> {
    /**
     * "status": "1",
     * "reason": "上传成功",
     * "data": ""
     */


    @SerializedName("StrRes")
    public String strRes;
    @SerializedName("StrMsg")
    public V strMsg;
    @SerializedName("Status")
    public int status;
    @SerializedName("ErrorCode")
    public int errorCode;


}
