package com.benxiang.noodles.serialport.data.sp;

import com.benxiang.noodles.contants.DbTypeContants;
import com.benxiang.noodles.serialport.data.constant.PreferenceKey;
import com.benxiang.noodles.utils.PreferenceNoodlesUtil;

/**
 * Created by admin on 2017/10/21.
 */

public class FormulaPreferenceConfig {

    //---------------------------------------------------------------------------A---------------------------------------------------------------------------
    public static int getTypeAWater() {
        return PreferenceNoodlesUtil.config().getInt(PreferenceKey.TYPE_A_WATER,PreferenceKey.TYPE_A_WATER_DEFAULT);
    }

    public static void setTypeAWater(int typeAWater) {
        PreferenceNoodlesUtil.config().put(PreferenceKey.TYPE_A_WATER,typeAWater);
    }

    public static int getTypeAOil() {
        return PreferenceNoodlesUtil.config().getInt(PreferenceKey.TYPE_A_OIL,PreferenceKey.OIL_DEFAULT);
    }

    public static void setTypeAOil(int typeAOil) {
        PreferenceNoodlesUtil.config().put(PreferenceKey.TYPE_A_OIL,typeAOil);
    }

    public static int getTypeABrine() {
        return PreferenceNoodlesUtil.config().getInt(PreferenceKey.TYPE_A_BRINE,PreferenceKey.BRINE_A_DEFAULT);
    }

    public static void setTypeABrine(int typeABrine) {
        PreferenceNoodlesUtil.config().put(PreferenceKey.TYPE_A_BRINE,typeABrine);
    }

    public static int getTypeAVinegar() {
        return PreferenceNoodlesUtil.config().getInt(PreferenceKey.TYPE_A_VINEGAR,PreferenceKey.VINEGAR_DEFAULT);
    }

    public static void setTypeAVinegar(int typeAVinegar) {
        PreferenceNoodlesUtil.config().put(PreferenceKey.TYPE_A_BRINE,typeAVinegar);
    }

    public static int getTypeAHeat() {
        return PreferenceNoodlesUtil.config().getInt(PreferenceKey.TYPE_A_HEAT,PreferenceKey.TYPE_A_HEAT_DEFAULT);
    }

    public static void setTypeAHeat(int typeAHeat) {
        PreferenceNoodlesUtil.config().put(PreferenceKey.TYPE_A_HEAT,typeAHeat);
    }

    public static int getTypeAHeatSize() {
        return PreferenceNoodlesUtil.config().getInt(PreferenceKey.TYPE_A_HEAT_SIZE,PreferenceKey.HEAT_SIZE_DEFAULT);
    }

    public static void setTypeAHeatSize(int typeAHeatSize) {
        PreferenceNoodlesUtil.config().put(PreferenceKey.TYPE_A_HEAT_SIZE,typeAHeatSize);
    }
    //---------------------------------------------------------------------------B---------------------------------------------------------------------------
    public static int getTypeBWater() {
        return PreferenceNoodlesUtil.config().getInt(PreferenceKey.TYPE_B_WATER,PreferenceKey.TYPE_B_WATER_DEFAULT);
    }

    public static void setTypeBWater(int typeBWater) {
        PreferenceNoodlesUtil.config().put(PreferenceKey.TYPE_B_WATER,typeBWater);
    }

    public static int getTypeBOil() {
        return PreferenceNoodlesUtil.config().getInt(PreferenceKey.TYPE_B_OIL,PreferenceKey.OIL_DEFAULT);
    }

    public static void setTypeBOil(int typeBOil) {
        PreferenceNoodlesUtil.config().put(PreferenceKey.TYPE_B_OIL,typeBOil);
    }

    public static int getTypeBBrine() {
        return PreferenceNoodlesUtil.config().getInt(PreferenceKey.TYPE_B_BRINE,PreferenceKey.BRINE_DEFAULT);
    }

    public static void setTypeBBrine(int typeBBrine) {
        PreferenceNoodlesUtil.config().put(PreferenceKey.TYPE_B_BRINE,typeBBrine);
    }

    public static int getTypeBVinegar() {
        return PreferenceNoodlesUtil.config().getInt(PreferenceKey.TYPE_B_VINEGAR,PreferenceKey.VINEGAR_DEFAULT);
    }

    public static void setTypeBVinegar(int typeBVinegar) {
        PreferenceNoodlesUtil.config().put(PreferenceKey.TYPE_B_BRINE,typeBVinegar);
    }

    public static int getTypeBHeat() {
        return PreferenceNoodlesUtil.config().getInt(PreferenceKey.TYPE_B_HEAT,PreferenceKey.TYPE_B_HEAT_DEFAULT);
    }

    public static void setTypeBHeat(int typeBHeat) {
        PreferenceNoodlesUtil.config().put(PreferenceKey.TYPE_B_HEAT,typeBHeat);
    }

    public static int getTypeBHeatSize() {
        return PreferenceNoodlesUtil.config().getInt(PreferenceKey.TYPE_B_HEAT_SIZE,PreferenceKey.HEAT_SIZE_DEFAULT);
    }

    public static void setTypeBHeatSize(int typeBHeatSize) {
        PreferenceNoodlesUtil.config().put(PreferenceKey.TYPE_B_HEAT_SIZE,typeBHeatSize);
    }
    //---------------------------------------------------------------------------C---------------------------------------------------------------------------
    public static int getTypeCWater() {
        return PreferenceNoodlesUtil.config().getInt(PreferenceKey.TYPE_C_WATER,PreferenceKey.TYPE_C_WATER_DEFAULT);
    }

    public static void setTypeCWater(int typeBWater) {
        PreferenceNoodlesUtil.config().put(PreferenceKey.TYPE_C_WATER,typeBWater);
    }

    public static int getTypeCOil() {
        return PreferenceNoodlesUtil.config().getInt(PreferenceKey.TYPE_C_OIL,PreferenceKey.OIL_DEFAULT);
    }

    public static void setTypeCOil(int typeCOil) {
        PreferenceNoodlesUtil.config().put(PreferenceKey.TYPE_B_OIL,typeCOil);
    }

    public static int getTypeCBrine() {
        return PreferenceNoodlesUtil.config().getInt(PreferenceKey.TYPE_C_BRINE,PreferenceKey.BRINE_DEFAULT);
    }

    public static void setTypeCBrine(int typeCBrine) {
        PreferenceNoodlesUtil.config().put(PreferenceKey.TYPE_C_BRINE,typeCBrine);
    }

    public static int getTypeCVinegar() {
        return PreferenceNoodlesUtil.config().getInt(PreferenceKey.TYPE_C_VINEGAR,PreferenceKey.VINEGAR_DEFAULT);
    }

    public static void setTypeCVinegar(int typeCVinegar) {
        PreferenceNoodlesUtil.config().put(PreferenceKey.TYPE_C_BRINE,typeCVinegar);
    }

    public static int getTypeCHeat() {
        return PreferenceNoodlesUtil.config().getInt(PreferenceKey.TYPE_C_HEAT,PreferenceKey.TYPE_C_HEAT_DEFAULT);
    }

    public static void setTypeCHeat(int typeCHeat) {
        PreferenceNoodlesUtil.config().put(PreferenceKey.TYPE_C_HEAT,typeCHeat);
    }

    public static int getTypeCHeatSize() {
        return PreferenceNoodlesUtil.config().getInt(PreferenceKey.TYPE_C_HEAT_SIZE,PreferenceKey.HEAT_SIZE_DEFAULT);
    }

    public static void setTypeCHeatSize(int typeCHeatSize) {
        PreferenceNoodlesUtil.config().put(PreferenceKey.TYPE_C_HEAT_SIZE,typeCHeatSize);
    }

    //---------------------------------------------------------------------------错误次数---------------------------------------------------------------------------
    public static int getErrorCount() {
        return PreferenceNoodlesUtil.config().getInt(PreferenceKey.ERROR_COUNT,PreferenceKey.ERROR_COUNT_DEFAULT);
    }

    public static void setErrorCount(int errorCount) {
        PreferenceNoodlesUtil.config().put(PreferenceKey.ERROR_COUNT,errorCount);
    }

    //---------------------------------------------------------------------------是否继续显示面做完的数据---------------------------------------------------------------------------
    public static boolean isDisplay() {
        return PreferenceNoodlesUtil.config().getBoolean(PreferenceKey.DISPLAY,false);
    }

    public static void setDisplay(boolean isDisplay) {
        PreferenceNoodlesUtil.config().put(PreferenceKey.DISPLAY,isDisplay);
    }

    //---------------------------------------------------------------------------每种品类弹簧的最大数---------------------------------------------------------------------------
    public static int getOnePliesMax() {
        return PreferenceNoodlesUtil.config().getInt(PreferenceKey.ONE_PLIES_MAX, DbTypeContants.SUANLA_BAO_PLIE_NO);
    }

    public static void setOnePliesMax(int onePliesMax) {
        PreferenceNoodlesUtil.config().put(PreferenceKey.ONE_PLIES_MAX,onePliesMax);
    }

    public static int getTwoPliesMax() {
        return PreferenceNoodlesUtil.config().getInt(PreferenceKey.TWO_PLIES_MAX, DbTypeContants.LUDAN_PLIE_NO);
    }

    public static void setTwoPliesMax(int twoPliesMax) {
        PreferenceNoodlesUtil.config().put(PreferenceKey.TWO_PLIES_MAX,twoPliesMax);
    }
    public static int getThreePliesMax() {
        return PreferenceNoodlesUtil.config().getInt(PreferenceKey.THREE_PLIES_MAX, DbTypeContants.JIUI_PLIE_NO);
    }

    public static void setThreePliesMax(int threePliesMax) {
        PreferenceNoodlesUtil.config().put(PreferenceKey.THREE_PLIES_MAX,threePliesMax);
    }
    public static int getFourPliesMax() {
        return PreferenceNoodlesUtil.config().getInt(PreferenceKey.FOUR_PLIES_MAX, DbTypeContants.JIUI_PLIE_NO);
    }

    public static void setFourPliesMax(int fourPliesMax) {
        PreferenceNoodlesUtil.config().put(PreferenceKey.FOUR_PLIES_MAX,fourPliesMax);
    }

    //---------------------------------------------------------------------------每种奖品对应的物品编号---------------------------------------------------------------------------
//    public static String getOneCategoryNo() {
//        return PreferenceNoodlesUtil.config().getString(PreferenceKey.ONE_CATEGORY_NO,"0302");
//    }
//
//    public static void setOneCategoryNo(String oneCategoryNo) {
//        PreferenceNoodlesUtil.config().put(PreferenceKey.ONE_CATEGORY_NO,oneCategoryNo);
//    }

    public static String getFirstPrizeProductCode() {
        return PreferenceNoodlesUtil.config().getString(PreferenceKey.TWO_CATEGORY_NO);
    }

    public static void setFirstPrizeProductCode(String firstPrizeProductCode) {
        PreferenceNoodlesUtil.config().put(PreferenceKey.TWO_CATEGORY_NO,firstPrizeProductCode);
    }

    public static String getSecondPrizeProductCode() {
        return PreferenceNoodlesUtil.config().getString(PreferenceKey.THREE_CATEGORY_NO);
    }

    public static void setSecondPrizeProductCode(String secondPrizeProductCode) {
        PreferenceNoodlesUtil.config().put(PreferenceKey.THREE_CATEGORY_NO,secondPrizeProductCode);
    }

    public static String getThirdPrizeProductCode() {
        return PreferenceNoodlesUtil.config().getString(PreferenceKey.FOUR_CATEGORY_NO);
    }

    public static void setThirdPrizeProductCode(String thirdPrizeProductCode) {
        PreferenceNoodlesUtil.config().put(PreferenceKey.FOUR_CATEGORY_NO,thirdPrizeProductCode);
    }

    //---------------------------------------------------------------------------一等奖，二等奖，三等奖分别对应品类的库存标识---------------------------------------------------------------------------
    public static int getFirstPrizeCategory() {
        return PreferenceNoodlesUtil.config().getInt(PreferenceKey.FIRST_PRIZE_CATEGORY,DbTypeContants.FOUR_CATEGORY);
    }

    public static void setFirstPrizeCategory(String firstPrizeCategory) {
        PreferenceNoodlesUtil.config().put(PreferenceKey.FIRST_PRIZE_CATEGORY,firstPrizeCategory);
    }

    public static int getSecondPrizeCategory() {
        return PreferenceNoodlesUtil.config().getInt(PreferenceKey.SECOND_PRIZE_CATEGORY,DbTypeContants.FOUR_CATEGORY);
    }

    public static void setSecondPrizeCategory(String secondPrizeCategory) {
        PreferenceNoodlesUtil.config().put(PreferenceKey.SECOND_PRIZE_CATEGORY,secondPrizeCategory);
    }

    public static int getThirdPrizeCategory() {
        return PreferenceNoodlesUtil.config().getInt(PreferenceKey.THIRD_PRIZE_CATEGORY,DbTypeContants.FOUR_CATEGORY);
    }

    public static void setThirdPrizeCategory(String thirdPrizeCategory) {
        PreferenceNoodlesUtil.config().put(PreferenceKey.THIRD_PRIZE_CATEGORY,thirdPrizeCategory);
    }



}
