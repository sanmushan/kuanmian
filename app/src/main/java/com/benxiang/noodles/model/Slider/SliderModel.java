package com.benxiang.noodles.model.Slider;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by 刘圣如 on 2017/11/11.
 */

public class SliderModel {

    @SerializedName("Result")
    public String Result;
    @SerializedName("ResultMsg")
    public String ResultMsg;
    @SerializedName("AppBaseInfo")
    public List<AppBaseInfo> AppBaseInfo;

    public static class AppBaseInfo {
        @SerializedName("IsPlaySoundEffect")
        public boolean IsPlaySoundEffect;
        @SerializedName("IsPlayBackgroundMusic")
        public boolean IsPlayBackgroundMusic;
        @SerializedName("BackgroundMusicAddrs")
        public String BackgroundMusicAddrs;
        @SerializedName("TopAdViewIsAutoStart")
        public boolean TopAdViewIsAutoStart;
        @SerializedName("TopAdViewSpeed")
        public int TopAdViewSpeed;
        @SerializedName("ADAddrs")
        public String ADAddrs;
    }
}
