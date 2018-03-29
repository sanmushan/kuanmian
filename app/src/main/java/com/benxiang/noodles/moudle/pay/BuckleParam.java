package com.benxiang.noodles.moudle.pay;

import java.util.List;

/**
 * Created by 刘圣如 on 2017/9/30.
 */

public class BuckleParam {

    public String LID;
    public String BillNo;
    public List<BuckleData> Detail;

    public static class BuckleData {
        public String MenuItemCode;
        public String LID;
        public String Standard;
        public int Quantity;
        public int SellingPrice;
        public int SellingMoney;
    }
}
