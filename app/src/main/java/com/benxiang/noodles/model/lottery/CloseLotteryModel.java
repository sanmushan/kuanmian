package com.benxiang.noodles.model.lottery;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ZeQiang Fang on 2017/11/27.
 */

public class CloseLotteryModel implements Parcelable {

    @SerializedName("spoilName")
    public String spoilName;
    @SerializedName("spoilNo")
    public String spoilNo;
    //用于加菜,单个物品的数量
    public int count=0;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.spoilName);
        dest.writeString(this.spoilNo);
        dest.writeInt(this.count);
    }

    public CloseLotteryModel() {
    }

    protected CloseLotteryModel(Parcel in) {
        this.spoilName = in.readString();
        this.spoilNo = in.readString();
        this.count = in.readInt();
    }

    public static final Creator<CloseLotteryModel> CREATOR = new Creator<CloseLotteryModel>() {
        @Override
        public CloseLotteryModel createFromParcel(Parcel source) {
            return new CloseLotteryModel(source);
        }

        @Override
        public CloseLotteryModel[] newArray(int size) {
            return new CloseLotteryModel[size];
        }
    };
}
