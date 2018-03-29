package com.benxiang.noodles.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.benxiang.noodles.model.lottery.CloseLotteryModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 刘圣如 on 2017/9/8.
 */


public class ListModle implements Parcelable {
    //小类名称，如：鸡腿,清汤小面
    public String goods_name;
    //一个小类的数量，如:三碗清汤小面
    public int goods_num;
    //物品编号，面从1开始，用于RecycleView的逻辑判断
    public int goods_no;
    //价格goods_price
    public float goods_prive;
    //数据库是否有库存
    public boolean stock;
    //面或粉
    public int riceType;
    //口味，如：酸辣口味，清汤口味
    public String riceTaste;
    //小类编号，如：1，表示清汤面或清汤粉
    public int rice_recipe_Type;
    //小类编号
    public String productId;
    //该小类在网上订的时间
    public String itemTime;
    //每一个小类的图片
    public int ig_url;
    //每一个小类的大图
    public String ImagePath;
    //每一个小类的小图
    public String SmallImagePath;
    //抽取到的奖品
    public List<CloseLotteryModel> closeLotteryModels;

    public ListModle() {

    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.goods_name);
        dest.writeInt(this.goods_num);
        dest.writeInt(this.goods_no);
        dest.writeFloat(this.goods_prive);
        dest.writeByte(this.stock ? (byte) 1 : (byte) 0);
        dest.writeInt(this.riceType);
        dest.writeString(this.riceTaste);
        dest.writeInt(this.rice_recipe_Type);
        dest.writeString(this.productId);
        dest.writeString(this.itemTime);
        dest.writeInt(this.ig_url);
        dest.writeString(this.ImagePath);
        dest.writeString(this.SmallImagePath);
        dest.writeTypedList(this.closeLotteryModels);
    }

    protected ListModle(Parcel in) {
        this.goods_name = in.readString();
        this.goods_num = in.readInt();
        this.goods_no = in.readInt();
        this.goods_prive = in.readFloat();
        this.stock = in.readByte() != 0;
        this.riceType = in.readInt();
        this.riceTaste = in.readString();
        this.rice_recipe_Type = in.readInt();
        this.productId = in.readString();
        this.itemTime = in.readString();
        this.ig_url = in.readInt();
        this.ImagePath = in.readString();
        this.SmallImagePath = in.readString();
        this.closeLotteryModels = in.createTypedArrayList(CloseLotteryModel.CREATOR);
    }

    public static final Creator<ListModle> CREATOR = new Creator<ListModle>() {
        @Override
        public ListModle createFromParcel(Parcel source) {
            return new ListModle(source);
        }

        @Override
        public ListModle[] newArray(int size) {
            return new ListModle[size];
        }
    };
}
