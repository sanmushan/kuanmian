package com.benxiang.noodles.data.table;

import com.litesuits.orm.db.annotation.Column;
import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.annotation.Table;
import com.litesuits.orm.db.enums.AssignType;

/**
 * Created by 刘圣如 on 2017/8/24.
 */
@Table("DropPackageDB")
public class DropPackageDB {
    public static final String COL_ID = "_id";
    public static final String COL_PRODUCT_STATUS = "product_status";
    public static final String COL_PRODUCT_CODE = "product_code";
    public static final String COL_PRODUCT_NAME = "product_name";

    public static final String COL_MENU_ITEM_CODE = "menu_item_code";
    public static final String COL_MENU_ITEM_CNAME = "menu_item_CName";

    public static final String COL_STORE_UNIT = "store_unit";
    public static final String COL_STANDARD = "standard";


    @PrimaryKey(AssignType.AUTO_INCREMENT)
    @Column(COL_ID)
    public long id;

    @Column(COL_PRODUCT_STATUS)
    public String ProductStatus;

    @Column(COL_PRODUCT_CODE)
    public String ProdutCode;

    @Column(COL_PRODUCT_NAME)
    public String ProductName;

    @Column(COL_MENU_ITEM_CODE)
    public String menuItemCode;

    @Column(COL_MENU_ITEM_CNAME)
    public String menuItemCName;

    @Column(COL_STORE_UNIT)
    public String storeUnit;

    @Column(COL_STANDARD)
    public String standard;
}

