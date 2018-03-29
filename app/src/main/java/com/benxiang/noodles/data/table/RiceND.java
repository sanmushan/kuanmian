package com.benxiang.noodles.data.table;

import com.litesuits.orm.db.annotation.Column;
import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.annotation.Table;
import com.litesuits.orm.db.enums.AssignType;

/**
 * Created by 刘圣如 on 2017/8/24.
 */
@Table("RiceND")
public class RiceND {
    public static final String COL_ID = "_id";
    public static final String COL_RICE_ND = "noodle_no";
    public static final String COL_RICE_ND_TYPE = "noodle_type";
    public static final String COL_RICE_ND_STATUS = "noodle_status";
    /*  public static final String COL_CSYS_X = "csys_x";
        public static final String COL_CSYS_Y = "csys_y";
        public static final String COL_CSYS_Z = "csys_z";*/
    public static final String COL_RICE_TOTAL_NUM = "total_num";
    public static final String COL_RICE_ND_DESC = "noodle_desc";
    public static final String COL_RICE_ND_TYPE_DESC = "noodle_type_desc";
    public static final String COL_STRAT_BIT = "strat_bit";

    @PrimaryKey(AssignType.AUTO_INCREMENT)
    @Column(COL_ID)
    public long id;

    @Column(COL_RICE_ND)
    public int noodleNo;

    @Column(COL_RICE_ND_DESC)
    public String noodleNoDesc;

    @Column(COL_RICE_ND_TYPE)
    public int noodleType;

    @Column(COL_RICE_ND_TYPE_DESC)
    public String noodleTypeDesc;

    @Column(COL_RICE_ND_STATUS)
    public int noodleStatus;

    /*    @Column(COL_CSYS_X)
        public int csyaX;

        @Column(COL_CSYS_Y)
        public int csyaY;

        @Column(COL_CSYS_Z)
        public int csyaZ;*/
    @Column(COL_RICE_TOTAL_NUM)
    public int totalNum;
    @Column(COL_STRAT_BIT)
    public int stratBit;


    @Override
    public String toString() {
        return "RiceND{" +
                "id=" + id +
                ",noodleNo=" + noodleNo +
                ",noodleDesc=" + noodleNoDesc +
                ",noodleType=" + noodleType +
                ",noodleTypeDesc=" + noodleTypeDesc +
                ",noodleStatus=" + noodleStatus +
                ",totalNum" + totalNum +
                "}";

    }
}

