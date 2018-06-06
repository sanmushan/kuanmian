package com.benxiang.noodles.serialport.data.constant;

/**
 * 修改物料配置
 *
 * @author admin
 * @date 2017/10/21
 */

public class PreferenceKey {

    public static int TYPE_A_WATER_DEFAULT = 90;
    public static int TYPE_B_WATER_DEFAULT = 90;
    public static int TYPE_C_WATER_DEFAULT = 90;

    public static int TYPE_A_HEAT_DEFAULT = 150;
    public static int TYPE_B_HEAT_DEFAULT = 150;
    public static int TYPE_C_HEAT_DEFAULT = 150;

//TODO LIN 修改物料 70代表7秒

    public static int OIL_DEFAULT = 70;
    /**
     * 卤水  30
     */
    public static int BRINE_DEFAULT = 30;
    /**
     * 醋
     */
    public static int VINEGAR_DEFAULT = 54;
    /**
     * 加热时长
     */
    public static int HEAT_SIZE_DEFAULT = 150;
    public static int BRINE_A_DEFAULT = 25;


    public static String TYPE_A_WATER = "type_a_water";
    public static String TYPE_A_OIL = "type_a_oil";
    public static String TYPE_A_BRINE = "type_a_brine";
    public static String TYPE_A_VINEGAR = "type_a_vinegar";
    public static String TYPE_A_HEAT = "type_a_heat";
    public static String TYPE_A_HEAT_SIZE = "type_a_heat_size";

    public static String TYPE_B_WATER = "type_b_water";
    public static String TYPE_B_OIL = "type_b_oil";
    public static String TYPE_B_BRINE = "type_b_brine";
    public static String TYPE_B_VINEGAR = "type_b_vinegar";
    public static String TYPE_B_HEAT = "type_b_heat";
    public static String TYPE_B_HEAT_SIZE = "type_b_heat_size";

    public static String TYPE_C_WATER = "type_c_water";
    public static String TYPE_C_OIL = "type_c_oil";
    public static String TYPE_C_BRINE = "type_c_brine";
    public static String TYPE_C_VINEGAR = "type_c_vinegar";
    public static String TYPE_C_HEAT = "type_c_heat";
    public static String TYPE_C_HEAT_SIZE = "type_c_heat_size";

    //判断固态和液态出错后可以继续做5碗
    public static int ERROR_COUNT_DEFAULT = 0;
    public static int ERROR_COUNT_AllOW = 3;

    public static String ERROR_COUNT = "error_count";

    /**
     * 判断下一个页面是否继续显示面已做完的数据
     */
    public static String DISPLAY = "display";

    /**
     * 每种品类弹簧的最大数
     */
    public static String ONE_PLIES_MAX = "one_plies_max";
    public static String TWO_PLIES_MAX = "two_plies_max";
    public static String THREE_PLIES_MAX = "three_plies_max";
    public static String FOUR_PLIES_MAX = "four_plies_max";

    /**
     * 每种品类对应的物品编号
     */
    public static String ONE_CATEGORY_NO = "one_category_no";
    public static String TWO_CATEGORY_NO = "two_category_no";
    public static String THREE_CATEGORY_NO = "three_category_no";
    public static String FOUR_CATEGORY_NO = "four_category_no";

    public static String FIRST_PRIZE_CATEGORY = "first_prize_category";
    public static String SECOND_PRIZE_CATEGORY = "second_prize_category";
    public static String THIRD_PRIZE_CATEGORY = "third_prize_category";
}
