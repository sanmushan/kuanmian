package com.benxiang.noodles.moudle.banner;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by LIN on 2018/5/8.
 */

public class BannerModel{

    /**
     * Result : 1
     * ResultMsg : 获取成功
     * AppBaseInfo : [{"IsPlaySoundEffect":true,"IsPlayBackgroundMusic":true,"BackgroundMusicAddrs":"/upload/PosSmall/1.mp3,/upload/PosSmall/2.mp3,/upload/PosSmall/3.mp3","TopAdViewIsAutoStart":true,"TopAdViewSpeed":2000,"ADAddrs":"/upload/PosSmall/1.jpg,/upload/PosSmall/2.jpg,/upload/PosSmall/3.jpg"}]
     */
    @SerializedName("Result")
    public String Result;
    @SerializedName("ResultMsg")
    public String ResultMsg;

    @SerializedName("AppBaseInfo")
    public List<AppBaseInfoBean> AppBaseInfo;


    public static class AppBaseInfoBean {
        /**
         * IsPlaySoundEffect : true
         * IsPlayBackgroundMusic : true
         * BackgroundMusicAddrs : /upload/PosSmall/1.mp3,/upload/PosSmall/2.mp3,/upload/PosSmall/3.mp3
         * TopAdViewIsAutoStart : true
         * TopAdViewSpeed : 2000
         * ADAddrs : /upload/PosSmall/1.jpg,/upload/PosSmall/2.jpg,/upload/PosSmall/3.jpg
         */
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
