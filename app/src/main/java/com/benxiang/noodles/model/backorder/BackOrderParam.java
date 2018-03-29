package com.benxiang.noodles.model.backorder;

import java.util.List;

/**
 * Created by 刘圣如 on 2017/11/17.
 */

public class BackOrderParam {

    public String LIDCode;
    public String TotalMoney;
    public List<Detail> Details;

    public static class Detail {
        public String ProductCode;
        public String ProductName;
        public String ProductUnit;
        public String ProductStandard;
        public double ProductPrice;
        public int ProductQuantity;
        public double ProductTotalMoney;
    }
}
