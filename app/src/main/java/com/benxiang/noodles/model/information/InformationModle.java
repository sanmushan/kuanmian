package com.benxiang.noodles.model.information;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by 刘圣如 on 2017/9/30.
 */

public class InformationModle {

    @SerializedName("Result")
    public String Result;
    @SerializedName("ResultMsg")
    public String ResultMsg;
    @SerializedName("MenuType")
    public List<MenuData> menuData;

    public class MenuData {

        @SerializedName("GUID")
        public String GUID;

        @SerializedName("MenuTypeCode")
        public String MenuTypeCode;
        @SerializedName("SubjectCode")
        public String SubjectCode;
        @SerializedName("SubjectName")
        public String SubjectName;
        @SerializedName("MenuTypeName")
        public String MenuTypeName;
        @SerializedName("DeptCode")
        public String DeptCode;

        @SerializedName("MenuItemArray")
        public List<MenuItemData> menuItemData;

    }

    public static class MenuItemData {

        @SerializedName("MenuTypeCode")
        public String MenuTypeCode;
        @SerializedName("MenuItemCode")
        public String MenuItemCode;
        @SerializedName("MenuItemCName")
        public String MenuItemCName;
        @SerializedName("MenuItemEName")
        public String MenuItemEName;
        @SerializedName("MenuItemPrice")
        public double MenuItemPrice;
        @SerializedName("ImagePath")
        public String ImagePath;
        @SerializedName("SmallImagePath")
        public String SmallImagePath;
        @SerializedName("BigImagePath")
        public String BigImagePath;
        @SerializedName("IsDiscount")
        public int IsDiscount;

        @SerializedName("DishStatus")
        public int DishStatus;

    }
}
