package com.benxiang.noodles.utils;

import android.util.Log;

import com.benxiang.noodles.contants.NoodleNameConstants;
import com.benxiang.noodles.data.noodle.NoodleEventData;
import com.benxiang.noodles.data.table.RiceOrderND;
import com.benxiang.noodles.serialport.cardmac.NoodlesSerialCommand;
import com.benxiang.noodles.serialport.data.constant.NoodlesType;
import com.benxiang.noodles.serialport.data.sp.PreferenceConfig;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * Created by admin on 2017/9/13.
 */

public class CalNoodlesNum {


    //由米粉号得到对应米粉类型的最大米粉号
    public static int getNumberNoodlesMax(int numberNoodles) {
        int mNumberNoodles = 0;
        if (numberNoodles >= PreferenceConfig.getTypeAMin() && numberNoodles <= PreferenceConfig.getTypeAMax()) {
            mNumberNoodles = PreferenceConfig.getTypeAMax();
        } else if (numberNoodles >= PreferenceConfig.getTypeBMin() && numberNoodles <= PreferenceConfig.getTypeBMax()) {
            mNumberNoodles = PreferenceConfig.getTypeBMax();
        } else if (numberNoodles >= PreferenceConfig.getTypeCMin() && numberNoodles <= PreferenceConfig.getTypeCMax()) {
            mNumberNoodles = PreferenceConfig.getTypeCMax();
        }
        return mNumberNoodles;
    }

    //TODO 如何判断米粉类型？？？？？
    //由米粉类型得到对应的最小米粉号
    public static int getNumberNoodlesMin(NoodlesType noodlesType) {
        int mNumberNoodles = 0;
        if (noodlesType == NoodlesType.TYPE_A) {
            mNumberNoodles = PreferenceConfig.getTypeAMin();
        } else if (noodlesType == NoodlesType.TYPE_B) {
            mNumberNoodles = PreferenceConfig.getTypeBMin();
        } else if (noodlesType == NoodlesType.TYPE_C) {
            mNumberNoodles = PreferenceConfig.getTypeCMin();
        }
        return mNumberNoodles;
    }

    //由ArrayList类型转化为数组
    public static int[] arrayListToArr(List<Integer> noodlesTypeArr) {
        int[] NumberNoodlesMinArr = new int[noodlesTypeArr.size()];
        for (int i = 0; i < noodlesTypeArr.size(); i++) {
            NumberNoodlesMinArr[i] = noodlesTypeArr.get(i);
        }
        return NumberNoodlesMinArr;
    }


    //由NoodleEventData数组对象转化为米粉号数组
    public static int[] getNoodleNos(List<RiceOrderND> mRiceOrderNDArr) {
        List<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < mRiceOrderNDArr.size(); i++) {
            list.add(mRiceOrderNDArr.get(i).noodleNo);
        }
        return arrayListToArr(list);
    }

    //由NoodleEventData数组对象转化为米粉号数组
    public static int[] getNoodleNoArr(List<NoodleEventData> noodleEventDataArr) {
        List<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < noodleEventDataArr.size(); i++) {
            list.add(noodleEventDataArr.get(i).noodle_no);
        }
        return arrayListToArr(list);
    }


    public static List<String> getNoodleStates(List<RiceOrderND> mRiceOrderNDArr) {
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < mRiceOrderNDArr.size(); i++) {
            list.add(mRiceOrderNDArr.get(i).noodleState);
        }
        return list;
    }

    public static List<String> getNoodleStateArr(List<NoodleEventData> noodleEventDataArr) {
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < noodleEventDataArr.size(); i++) {
            list.add(noodleEventDataArr.get(i).noodle_state);
        }
        return list;
    }

    //由米粉类型数组得到对应的最小米粉号数组
    public static int[] getNumberNoodlesMinArr(ArrayList<NoodlesType> noodlesTypeArr) {
        int[] NumberNoodlesMinArr = new int[noodlesTypeArr.size()];
        for (int i = 0; i < noodlesTypeArr.size(); i++) {
            int numberNoodlesMin = getNumberNoodlesMin(noodlesTypeArr.get(i));
            NumberNoodlesMinArr[i] = numberNoodlesMin;
        }
        return NumberNoodlesMinArr;
    }

    //由米粉号得到对应米粉类型
    public static NoodlesType getNumberNoodlesType(int numberNoodles) {
        if (numberNoodles >= PreferenceConfig.getTypeAMin() && numberNoodles <= PreferenceConfig.getTypeAMax()) {
            return NoodlesType.TYPE_A;
        } else if (numberNoodles >= PreferenceConfig.getTypeBMin() && numberNoodles <= PreferenceConfig.getTypeBMax()) {
            return NoodlesType.TYPE_B;
        } else {
            return NoodlesType.TYPE_C;
        }
    }

    //由米粉号和米粉名得到对应米粉的加热加汤命令---------------------------------------------------------添加加醋判断------------------------------------------------------------------------------
    public static byte[] getSoupOrHeatCommand(int numberNoodles, String noodlesName) {
        byte[] command = null;
        if (noodlesName.equals(NoodleNameConstants.QINGTANG)) {
            command = getSoupOrHeatCommand(numberNoodles, false);
        } else if (noodlesName.equals(NoodleNameConstants.CHONGQING)) {
            command = getSoupOrHeatCommand(numberNoodles, false);
        } else if (noodlesName.equals(NoodleNameConstants.LUDAN)) {
            command = getSoupOrHeatCommand(numberNoodles, false);
        } else if (noodlesName.equals(NoodleNameConstants.HAOHUA)) {
            command = getSoupOrHeatCommand(numberNoodles, false);
        } else if (noodlesName.equals(NoodleNameConstants.FRESH)) {
            command = getSoupOrHeatCommand(numberNoodles, false);
        }else if (noodlesName.equals(NoodleNameConstants.SUANLA)) {//noodlesName.equals(NoodleNameConstants.SUANLA)
            command = getSoupOrHeatCommand(numberNoodles, true);
        }
        return command;
    }

    //由米粉号得到对应米粉的加热加汤命令
    public static byte[] getSoupOrHeatCommand(int numberNoodles, boolean haveVinegar) {
        byte[] command = null;
        NoodlesType noodlesType = getNumberNoodlesType(numberNoodles);
        int hasVinear = haveVinegar ? 1 : 0;
        switch (hasVinear) {
            case 1:
                if (noodlesType == NoodlesType.TYPE_A) {
                    command = NoodlesSerialCommand.A_SOUP_A_HEAT_VINEGAR;
                } else if (noodlesType == NoodlesType.TYPE_B) {
                    command = NoodlesSerialCommand.B_SOUP_B_HEAT_VINEGAR;
                } else {
                    command = NoodlesSerialCommand.C_SOUP_C_HEAT_VINEGAR;
                }
                break;
            case 0:
                if (noodlesType == NoodlesType.TYPE_A) {
                    command = NoodlesSerialCommand.A_SOUP_A_HEAT;
                } else if (noodlesType == NoodlesType.TYPE_B) {
                    command = NoodlesSerialCommand.B_SOUP_B_HEAT;
                } else {
                    command = NoodlesSerialCommand.C_SOUP_C_HEAT;
                }
                break;
                default:
        }
        return command;
    }

    //由米粉号和米粉名得到对应米粉的加热加汤命令
    public static byte[] getSoupOrHeatCommand(int numberNoodlesAddHeat, int numberNoodlesAddSoup, String noodlesName) {
        byte[] command = null;
        if (noodlesName.equals(NoodleNameConstants.QINGTANG)) {
            command = getSoupOrHeatCommand(numberNoodlesAddHeat, numberNoodlesAddSoup, false);
        } else if (noodlesName.equals(NoodleNameConstants.CHONGQING)) {
            command = getSoupOrHeatCommand(numberNoodlesAddHeat, numberNoodlesAddSoup, false);
        } else if (noodlesName.equals(NoodleNameConstants.LUDAN)) {
            command = getSoupOrHeatCommand(numberNoodlesAddHeat, numberNoodlesAddSoup, false);
        } else if (noodlesName.equals(NoodleNameConstants.HAOHUA)) {
            command = getSoupOrHeatCommand(numberNoodlesAddHeat, numberNoodlesAddSoup, false);
        }else if (noodlesName.equals(NoodleNameConstants.FRESH)) {
            command = getSoupOrHeatCommand(numberNoodlesAddHeat, numberNoodlesAddSoup, false);
        } else if (noodlesName.equals(NoodleNameConstants.SUANLA)) {
            command = getSoupOrHeatCommand(numberNoodlesAddHeat, numberNoodlesAddSoup, true);
        }
        return command;
    }

    //由米粉号得到对应米粉的加热加汤命令
    public static byte[] getSoupOrHeatCommand(int numberNoodlesAddHeat, int numberNoodlesAddSoup, boolean haveVinegar) {
        byte[] command = null;
        NoodlesType noodlesTypeAddHeat = getNumberNoodlesType(numberNoodlesAddHeat);
        NoodlesType noodlesTypeAddSoup = getNumberNoodlesType(numberNoodlesAddSoup);
        int hasVinear = haveVinegar ? 1 : 0;
        Timber.e("numberNoodlesAddHeat=" + numberNoodlesAddHeat + ",numberNoodlesAddSoup=" + numberNoodlesAddSoup + ",haveVinegar=" + haveVinegar);
        Log.e("linbino","CalNoodlesNum.class ----- numberNoodlesAddHeat=" + numberNoodlesAddHeat + ",numberNoodlesAddSoup=" + numberNoodlesAddSoup + ",haveVinegar=" + haveVinegar);
        switch (hasVinear) {
            case 1:
                if (noodlesTypeAddSoup == NoodlesType.TYPE_A && noodlesTypeAddHeat == NoodlesType.TYPE_A) {
                    command = NoodlesSerialCommand.A_SOUP_A_HEAT_VINEGAR;
                } else if (noodlesTypeAddSoup == NoodlesType.TYPE_A && noodlesTypeAddHeat == NoodlesType.TYPE_B) {
                    command = NoodlesSerialCommand.A_SOUP_B_HEAT_VINEGAR;
                } else if (noodlesTypeAddSoup == NoodlesType.TYPE_A && noodlesTypeAddHeat == NoodlesType.TYPE_C) {
                    command = NoodlesSerialCommand.A_SOUP_C_HEAT_VINEGAR;
                } else if (noodlesTypeAddSoup == NoodlesType.TYPE_B && noodlesTypeAddHeat == NoodlesType.TYPE_A) {
                    command = NoodlesSerialCommand.B_SOUP_A_HEAT_VINEGAR;
                } else if (noodlesTypeAddSoup == NoodlesType.TYPE_B && noodlesTypeAddHeat == NoodlesType.TYPE_B) {
                    command = NoodlesSerialCommand.B_SOUP_B_HEAT_VINEGAR;
                } else if (noodlesTypeAddSoup == NoodlesType.TYPE_B && noodlesTypeAddHeat == NoodlesType.TYPE_C) {
                    command = NoodlesSerialCommand.B_SOUP_C_HEAT_VINEGAR;
                } else if (noodlesTypeAddSoup == NoodlesType.TYPE_C && noodlesTypeAddHeat == NoodlesType.TYPE_A) {
                    command = NoodlesSerialCommand.C_SOUP_A_HEAT_VINEGAR;
                } else if (noodlesTypeAddSoup == NoodlesType.TYPE_C && noodlesTypeAddHeat == NoodlesType.TYPE_B) {
                    command = NoodlesSerialCommand.C_SOUP_B_HEAT_VINEGAR;
                } else if (noodlesTypeAddSoup == NoodlesType.TYPE_C && noodlesTypeAddHeat == NoodlesType.TYPE_C) {
                    command = NoodlesSerialCommand.C_SOUP_C_HEAT_VINEGAR;
                }
                break;
            case 0:
                if (noodlesTypeAddSoup == NoodlesType.TYPE_A && noodlesTypeAddHeat == NoodlesType.TYPE_A) {
                    command = NoodlesSerialCommand.A_SOUP_A_HEAT;
                } else if (noodlesTypeAddSoup == NoodlesType.TYPE_A && noodlesTypeAddHeat == NoodlesType.TYPE_B) {
                    command = NoodlesSerialCommand.A_SOUP_B_HEAT;
                } else if (noodlesTypeAddSoup == NoodlesType.TYPE_A && noodlesTypeAddHeat == NoodlesType.TYPE_C) {
                    command = NoodlesSerialCommand.A_SOUP_C_HEAT;
                } else if (noodlesTypeAddSoup == NoodlesType.TYPE_B && noodlesTypeAddHeat == NoodlesType.TYPE_A) {
                    command = NoodlesSerialCommand.B_SOUP_A_HEAT;
                } else if (noodlesTypeAddSoup == NoodlesType.TYPE_B && noodlesTypeAddHeat == NoodlesType.TYPE_B) {
                    command = NoodlesSerialCommand.B_SOUP_B_HEAT;
                } else if (noodlesTypeAddSoup == NoodlesType.TYPE_B && noodlesTypeAddHeat == NoodlesType.TYPE_C) {
                    command = NoodlesSerialCommand.B_SOUP_C_HEAT;
                } else if (noodlesTypeAddSoup == NoodlesType.TYPE_C && noodlesTypeAddHeat == NoodlesType.TYPE_A) {
                    command = NoodlesSerialCommand.C_SOUP_A_HEAT;
                } else if (noodlesTypeAddSoup == NoodlesType.TYPE_C && noodlesTypeAddHeat == NoodlesType.TYPE_B) {
                    command = NoodlesSerialCommand.C_SOUP_B_HEAT;
                } else if (noodlesTypeAddSoup == NoodlesType.TYPE_C && noodlesTypeAddHeat == NoodlesType.TYPE_C) {
                    command = NoodlesSerialCommand.C_SOUP_C_HEAT;
                }
                break;

            default:
                break;
        }

        return command;
    }


}
