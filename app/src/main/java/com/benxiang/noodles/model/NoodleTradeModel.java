package com.benxiang.noodles.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by 刘圣如 on 2017/9/6.
 */

public class NoodleTradeModel implements Parcelable {
    //订的总数
    public int total_num;
    //总价格
    public float total_price;
    //取餐号，用于退菜
    public String take_meal_No;
    //订单号，由Mac地址和时间生成
    public String order_No;
    //支付时间
    public String pay_time;
    //支付类型，如：2：微信，6：支付宝
    public int pay_type;
    //点的米粉的数量
    public int rice_No;
    //点得面的数量
    public int noodle_No;
    //物品的唯一:guid
    public String ggid;
    public List<ListModle> listModles;
    //取餐时判断是否食材足够
    public boolean hasEnoughIngredients = false;
    //设置订单状态通过取餐号或GUID
    public boolean isSetBillStatusByTakeNumber = true;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.total_num);
        dest.writeFloat(this.total_price);
        dest.writeString(this.take_meal_No);
        dest.writeString(this.order_No);
        dest.writeString(this.pay_time);
        dest.writeInt(this.pay_type);
        dest.writeInt(this.rice_No);
        dest.writeInt(this.noodle_No);
        dest.writeString(this.ggid);
        dest.writeTypedList(this.listModles);
        dest.writeByte(this.hasEnoughIngredients ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isSetBillStatusByTakeNumber ? (byte) 1 : (byte) 0);
    }

    public NoodleTradeModel() {
    }

    protected NoodleTradeModel(Parcel in) {
        this.total_num = in.readInt();
        this.total_price = in.readFloat();
        this.take_meal_No = in.readString();
        this.order_No = in.readString();
        this.pay_time = in.readString();
        this.pay_type = in.readInt();
        this.rice_No = in.readInt();
        this.noodle_No = in.readInt();
        this.ggid = in.readString();
        this.listModles = in.createTypedArrayList(ListModle.CREATOR);
        this.hasEnoughIngredients = in.readByte() != 0;
        this.isSetBillStatusByTakeNumber = in.readByte() != 0;
    }

    public static final Creator<NoodleTradeModel> CREATOR = new Creator<NoodleTradeModel>() {
        @Override
        public NoodleTradeModel createFromParcel(Parcel source) {
            return new NoodleTradeModel(source);
        }

        @Override
        public NoodleTradeModel[] newArray(int size) {
            return new NoodleTradeModel[size];
        }
    };
}
