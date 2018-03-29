package com.benxiang.noodles.contants;

/**
 * Created by 刘圣如 on 2017/10/16.
 */

public class DbTypeContants {
    //各物品有库存的标志
    public static final int MIANTIAO = 11;
    public static final int MIFEN = 22;
    public static final int SUANLABAO = 31;
    public static final int SUANLAJITUI = 32;
    public static final int LUJIDANG = 33;
    public static final int FOUR_CATEGORY=34;
    public static final int PRODUCT_STATUS = 99;
    public static final int BRINE_STATUS=41;


    //各物品的分类
    public static final int NOODLE_TYPE_NO = 1;
    public static final int RICE_TYPE_NO = 2;
    public static final int SUANLABAO_TYPE_NO = 3;
    public static final int SUANLAJITUI_TYPE_NO = 4;
    public static final int LUJIDANG_TYPE_NO = 5;
    public static final int FOUR_CATEGORY_TYPE_NO = 6;
    public static final int BRINE_TYPE_NO = 7;

    //管道类型
    public static final String NOODLE_TYPE = "A";
    public static final String RICE_TYPE = "B";
    public static final String SUANLABAO_TYPE = "C";
    public static final String SUANLAJITUI_TYPE = "D";
    public static final String LUJIDANG_TYPE = "E";
    public static final String FOUR_CATEGORY_TYPE = "F";
    public static final String BRINE_TYPE = "G";

    //物品名称
    //面
    public static final String QINGTANG_MAIN = "清汤小面";
    public static final String CHONGQING_MAIN = "重庆小面";
    public static final String LUDAN_MAIN = "卤蛋小面";
    public static final String JITUI_MAIN = "鸡腿小面";
    //米粉.
    public static final String QINGTANG_FENG = "清汤薯粉";
    public static final String SUANLA_FENG = "酸辣薯粉";
    public static final String LUDAN_FENG = "卤蛋薯粉";
    public static final String JITUI_FENG = "鸡腿薯粉";

    //数据库的各个物品的数量设置

    public static final int RICE_PLIE = 6;
    public static final int NOODLE_PLIE = 5;
    public static final int SUANLA_BAO_PLIE_NO = 35;
    public static final int JIUI_PLIE_NO = 20;
    public static final int LUDAN_PLIE_NO = 10;
    //面条和米粉的起始位置
    public static final int RICE_START_NO = 19;
//    public static final int NOODLE_START_NO = 10;
    public static final int NOODLE_START_NO = 4;
    //机器缺少的层数
    public static final int LACK_PILES = 1;


    //各个物品最大数量
    public static final int RICE_MAX = 72;
//    public static final int RICE_Piles_MAX = 12;
    /**
     *  放面食最大层数  LIN
     */
    //每一层12碗
//    public static final int RICE_Piles_MAX = 12;
    //机器有多少层
//    public static final int RICE_PLIES_MAX = 11;
    //多少条通道
//    public static final int CHARGE_PLIES_MAX = 6;

    //每层数量
    public static final int RICE_Piles_MAX = 12;

    public static final int NOODLE_MAX = 60;
    public static final int SUANLA_BAO_MAX = 70;
    public static final int JITUI_MAX = 40;
    public static final int LUDAN_MAX = 20;
    //科学城的机器的层数
    public static final int RICE_PLIES_MAX = 11;
    public static final int CHARGE_PLIES_MAX = 6;


    /*
    方工
    public static final int RICE_Piles_MAX = 11;
    public static final int NOODLE_MAX = 60;
    public static final int SUANLA_BAO_MAX = 70;
    public static final int JITUI_MAX = 40;
    public static final int LUDAN_MAX = 20;
    public static final int RICE_PLIES_MAX = 11;
    public static final int CHARGE_PLIES_MAX = 6;
*/
}
