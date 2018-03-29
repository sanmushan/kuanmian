package com.benxiang.noodles.data.table;

import android.os.Parcel;
import android.os.Parcelable;

import com.litesuits.orm.db.annotation.Column;
import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.annotation.Table;
import com.litesuits.orm.db.enums.AssignType;

/**
 * Created by 刘圣如 on 2017/10/14.
 */
@Table("RiceOrderND")
public class RiceOrderND implements Parcelable {
    public static final String COL_ID = "_id";
    public static final String COL_RICE_ND = "noodle_no";
    public static final String COL_RICE_NAME = "noodle_name";
    public static final String COL_RICE_STATE = "noodle_state";
    //排序号
    public static final String COL_SORT_NO = "sort_no";
    //0表示没做，1表示正在做，2表示已做完
    public static final String COL_RICE_SIGN = "noodle_sign";
    //当前这一碗是否被选中，若没有选中，则不继续多单多碗
    public boolean isSelected = false;
    //抽中的奖品
    public static final String COL_SPOIL_NAME = "spoil_name";

    //支付类型
    public static final String COL_PAY_TYPE = "pay_type";
    //订单号
    public static final String COL_ORDER_NO = "order_no";
    //取餐号
    public static final String COL_TAKE_MEAL_NO = "take_meal_No";
    //单价
    public static final String COL_GOODS_PRICE = "goodPrice";
    //总价格
    public static final String COL_TOTAL_PRICE = "total_price";

    public static final String COL_IS_SET_BILLSTATUS_BY_TAKENUMBER = "isSetBillStatusByTakeNumber";

    @Column(COL_ID)

    @PrimaryKey(AssignType.AUTO_INCREMENT)
    public long id;

    @Column(COL_RICE_ND)
    public int noodleNo;

    @Column(COL_RICE_NAME)
    public String noodleName;

    @Column(COL_RICE_STATE)
    public String noodleState;

    @Column(COL_SORT_NO)
    public int sortNo;

    @Column(COL_RICE_SIGN)
    public int noodleSign;

    @Column(COL_SPOIL_NAME)
    public String spoilName;

    //-----------------------------------------------------新增，用于多单多碗的退款--------------------------------------------------------
    @Column(COL_PAY_TYPE)
    public int payType;

    @Column(COL_ORDER_NO)
    public String order_No;

    @Column(COL_GOODS_PRICE)
    public float goodsPrice;

    @Column(COL_TOTAL_PRICE)
    public float total_price;

    @Column(COL_TAKE_MEAL_NO)
    public String take_meal_No;

    @Column(COL_IS_SET_BILLSTATUS_BY_TAKENUMBER)
    public boolean isSetBillStatusByTakeNumber = true;

    //ParceLable自动生成

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.isSelected ? (byte) 1 : (byte) 0);
        dest.writeLong(this.id);
        dest.writeInt(this.noodleNo);
        dest.writeString(this.noodleName);
        dest.writeString(this.noodleState);
        dest.writeInt(this.sortNo);
        dest.writeInt(this.noodleSign);
        dest.writeString(this.spoilName);
        dest.writeInt(this.payType);
        dest.writeString(this.order_No);
        dest.writeFloat(this.goodsPrice);
        dest.writeFloat(this.total_price);
        dest.writeString(this.take_meal_No);
        dest.writeByte(this.isSetBillStatusByTakeNumber ? (byte) 1 : (byte) 0);
    }

    public RiceOrderND() {
    }

    protected RiceOrderND(Parcel in) {
        this.isSelected = in.readByte() != 0;
        this.id = in.readLong();
        this.noodleNo = in.readInt();
        this.noodleName = in.readString();
        this.noodleState = in.readString();
        this.sortNo = in.readInt();
        this.noodleSign = in.readInt();
        this.spoilName = in.readString();
        this.payType = in.readInt();
        this.order_No = in.readString();
        this.goodsPrice = in.readFloat();
        this.total_price = in.readFloat();
        this.take_meal_No = in.readString();
        this.isSetBillStatusByTakeNumber = in.readByte() != 0;
    }

    public static final Creator<RiceOrderND> CREATOR = new Creator<RiceOrderND>() {
        @Override
        public RiceOrderND createFromParcel(Parcel source) {
            return new RiceOrderND(source);
        }

        @Override
        public RiceOrderND[] newArray(int size) {
            return new RiceOrderND[size];
        }
    };
}
