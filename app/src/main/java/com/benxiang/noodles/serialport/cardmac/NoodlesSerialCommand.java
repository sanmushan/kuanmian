package com.benxiang.noodles.serialport.cardmac;


import com.benxiang.noodles.contants.DbTypeContants;
import com.benxiang.noodles.contants.NoodleNameConstants;
import com.benxiang.noodles.data.DBNoodleHelper;
import com.benxiang.noodles.data.noodle.NoodleEventData;
import com.benxiang.noodles.data.table.RiceND;
import com.benxiang.noodles.serialport.data.sp.FormulaPreferenceConfig;
import com.benxiang.noodles.utils.ByteUtil;
import com.benxiang.noodles.utils.CRC16;
import com.benxiang.noodles.utils.FunctionCodeUtil;
import com.benxiang.noodles.utils.MyFunc;

import java.util.ArrayList;

import timber.log.Timber;

public class NoodlesSerialCommand {

    public static final byte[] beltTest = new byte[]{(byte) 0xF2, 0x02, 0x01, 0x01};
    public static final byte[] mifeng = new byte[]{(byte) 0xF1, 0x45, 0x00, 0x13, 0x0A, 0x01, (byte) 0xEE, 0x50};
    public static final byte[] mifengtest = new byte[]{(byte) 0xF1, 0x45, 0x00, 0x13, 0x0A, 0x01};

    //----------------------------------------------模组2----------------------------------------------
    //功能码02
    public static final byte[] CHECK_BELT = new byte[]{(byte) 0xF2, 0x02, 0x00, 0x01, 0x00, 0x01, (byte) 0xFC, (byte) 0xC9};
    public static final byte[] CHECK_BOX = new byte[]{(byte) 0xF2, 0x02, 0x00, 0x05, 0x00, 0x01, (byte) 0xBD, 0x08};

    //功能码45
    public static final byte[] SEND_TO_SOUP_AREA = new byte[]{(byte) 0xF2, 0x45, 0x00, 0x02, 0x0A, 0x01, (byte) 0xBE, 0x66};//皮带正转
    public static final byte[] ADD_SOUP = new byte[]{(byte) 0xF2, 0x45, 0x00, 0x04, 0x0A, 0x01, (byte) 0x5E, 0x67};
    public static final byte[] SEND_TO_HEAT_AREA = getFourFiveCommand(2);//皮带正转
    public static final byte[] START_HEAT = new byte[]{(byte) 0xF2, 0x45, 0x00, 0x04, 0x0A, 0x01, 0x5E, 0x67};
    public static final byte[] SEND_TO_RECYLE = getFourFiveCommand(2);//皮带正转
    public static final byte[] CLOSE_DOOR = new byte[]{(byte) 0xF2, 0x45, 0x00, 0x3D, 0x0A, 0x01, (byte) 0x8E, 0x6A};
    public static final byte[] SEND_TO_DOOR = getFourFiveCommand(2);//皮带正转
    public static final byte[] SUPPLY_PACKAGE = new byte[]{(byte) 0xF1, 0x45, 0x00, 0x34, 0x0A, 0x01, 0x5E, 0x5B};
    public static final byte[] OPEN_DOOR = new byte[]{(byte) 0xF2, 0x45, 0x00, 0x3C, 0x0A, 0x01, (byte) 0xDF, (byte) 0xAA};

    public static final byte[] BELT_INVERSION = new byte[]{(byte) 0xF2, 0x45, 0x00, 0x03, 0x0A, 0x01, (byte) 0xEF, (byte) 0xA6};//皮带反转
    public static final byte[] PUSH_BOWL = new byte[]{(byte) 0xF2, 0x45, 0x00, 0x3E, 0x0A, 0x01, 0x7E, 0x6A};//推碗回收

    public static final byte[] A_SOUP_A_HEAT = new byte[]{(byte) 0xF2, 0x45, 0x00, 0x04, 0x0A, 0x01, (byte) 0x5E, 0x67};
    public static final byte[] A_SOUP_B_HEAT = getFourFiveCommand(5);
    public static final byte[] A_SOUP_C_HEAT = getFourFiveCommand(6);
    public static final byte[] B_SOUP_A_HEAT = getFourFiveCommand(7);
    public static final byte[] B_SOUP_B_HEAT = getFourFiveCommand(8);
    public static final byte[] B_SOUP_C_HEAT = getFourFiveCommand(9);
    public static final byte[] C_SOUP_A_HEAT = getFourFiveCommand(10);
    public static final byte[] C_SOUP_B_HEAT = getFourFiveCommand(11);
    public static final byte[] C_SOUP_C_HEAT = getFourFiveCommand(12);
    public static final byte[] A_SOUP_A_HEAT_VINEGAR = getFourFiveCommand(13);
    public static final byte[] A_SOUP_B_HEAT_VINEGAR = getFourFiveCommand(14);
    public static final byte[] A_SOUP_C_HEAT_VINEGAR = getFourFiveCommand(15);
    public static final byte[] B_SOUP_A_HEAT_VINEGAR = getFourFiveCommand(16);
    public static final byte[] B_SOUP_B_HEAT_VINEGAR = getFourFiveCommand(17);
    public static final byte[] B_SOUP_C_HEAT_VINEGAR = getFourFiveCommand(18);
    public static final byte[] C_SOUP_A_HEAT_VINEGAR = getFourFiveCommand(19);
    public static final byte[] C_SOUP_B_HEAT_VINEGAR = getFourFiveCommand(20);
    public static final byte[] C_SOUP_C_HEAT_VINEGAR = getFourFiveCommand(21);
    public static final byte[] OPEN_STEAM = getFourFiveCommand(64);
    public static final byte[] BOTTLED_WATER = getFourFiveCommand(63);


    //功能码0x10=>16(十进制)                                                         起始地址     寄存器个数   字节数 写入值                                                                             校验码
    //水         油            卤水         醋           加热         加热大小
    public static final byte[] TEST_A_A = new byte[]{(byte) 0xF2, 0x10, 0x00, 0x1A, 0x00, 0x06, 0x0C, 0x00, 0x5A, 0x00, 0x46, 0x00, 0x12, 0x00, 0x00, 0x00, 0x5A, 0x00, (byte) 0x96, 0x1E, 0x2B};
    public static final byte[] A_A_FORMULA = FunctionCodeUtil.getOneZeroCommand(26, FormulaPreferenceConfig.getTypeAWater(), FormulaPreferenceConfig.getTypeAOil(), FormulaPreferenceConfig.getTypeABrine(), 0,
            FormulaPreferenceConfig.getTypeBHeat(), FormulaPreferenceConfig.getTypeBHeatSize());
    public static final byte[] A_B_FORMULA = FunctionCodeUtil.getOneZeroCommand(32, FormulaPreferenceConfig.getTypeAWater(), FormulaPreferenceConfig.getTypeAOil(), FormulaPreferenceConfig.getTypeABrine(), 0,
            FormulaPreferenceConfig.getTypeBHeat(), FormulaPreferenceConfig.getTypeBHeatSize());
    public static final byte[] A_C_FORMULA = FunctionCodeUtil.getOneZeroCommand(38, FormulaPreferenceConfig.getTypeAWater(), FormulaPreferenceConfig.getTypeAOil(), FormulaPreferenceConfig.getTypeABrine(), 0,
            FormulaPreferenceConfig.getTypeCHeat(), FormulaPreferenceConfig.getTypeCHeatSize());
    public static final byte[] B_A_FORMULA = FunctionCodeUtil.getOneZeroCommand(44, FormulaPreferenceConfig.getTypeBWater(), FormulaPreferenceConfig.getTypeBOil(), FormulaPreferenceConfig.getTypeBBrine(), 0,
            FormulaPreferenceConfig.getTypeAHeat(), FormulaPreferenceConfig.getTypeAHeatSize());
    public static final byte[] B_B_FORMULA = FunctionCodeUtil.getOneZeroCommand(50, FormulaPreferenceConfig.getTypeBWater(), FormulaPreferenceConfig.getTypeBOil(), FormulaPreferenceConfig.getTypeBBrine(), 0,
            FormulaPreferenceConfig.getTypeBHeat(), FormulaPreferenceConfig.getTypeBHeatSize());
    public static final byte[] B_C_FORMULA = FunctionCodeUtil.getOneZeroCommand(56, FormulaPreferenceConfig.getTypeBWater(), FormulaPreferenceConfig.getTypeBOil(), FormulaPreferenceConfig.getTypeBBrine(), 0,
            FormulaPreferenceConfig.getTypeCHeat(), FormulaPreferenceConfig.getTypeCHeatSize());
    public static final byte[] C_A_FORMULA = FunctionCodeUtil.getOneZeroCommand(62, FormulaPreferenceConfig.getTypeCWater(), FormulaPreferenceConfig.getTypeCOil(), FormulaPreferenceConfig.getTypeCBrine(), 0,
            FormulaPreferenceConfig.getTypeAHeat(), FormulaPreferenceConfig.getTypeAHeatSize());
    public static final byte[] C_B_FORMULA = FunctionCodeUtil.getOneZeroCommand(68, FormulaPreferenceConfig.getTypeCWater(), FormulaPreferenceConfig.getTypeCOil(), FormulaPreferenceConfig.getTypeCBrine(), 0,
            FormulaPreferenceConfig.getTypeBHeat(), FormulaPreferenceConfig.getTypeBHeatSize());
    public static final byte[] C_C_FORMULA = FunctionCodeUtil.getOneZeroCommand(74, FormulaPreferenceConfig.getTypeCWater(), FormulaPreferenceConfig.getTypeCOil(), FormulaPreferenceConfig.getTypeCBrine(), 0,
            FormulaPreferenceConfig.getTypeCHeat(), FormulaPreferenceConfig.getTypeCHeatSize());
    public static final byte[] A_A_FORMULA_VINEGAR = FunctionCodeUtil.getOneZeroCommand(80, FormulaPreferenceConfig.getTypeAWater(), FormulaPreferenceConfig.getTypeAOil(), FormulaPreferenceConfig.getTypeABrine(), FormulaPreferenceConfig.getTypeAVinegar(),
            FormulaPreferenceConfig.getTypeBHeat(), FormulaPreferenceConfig.getTypeBHeatSize());
    public static final byte[] A_B_FORMULA_VINEGAR = FunctionCodeUtil.getOneZeroCommand(86, FormulaPreferenceConfig.getTypeAWater(), FormulaPreferenceConfig.getTypeAOil(), FormulaPreferenceConfig.getTypeABrine(), FormulaPreferenceConfig.getTypeAVinegar(),
            FormulaPreferenceConfig.getTypeBHeat(), FormulaPreferenceConfig.getTypeBHeatSize());
    public static final byte[] A_C_FORMULA_VINEGAR = FunctionCodeUtil.getOneZeroCommand(92, FormulaPreferenceConfig.getTypeAWater(), FormulaPreferenceConfig.getTypeAOil(), FormulaPreferenceConfig.getTypeABrine(), FormulaPreferenceConfig.getTypeAVinegar(),
            FormulaPreferenceConfig.getTypeCHeat(), FormulaPreferenceConfig.getTypeCHeatSize());
    public static final byte[] B_A_FORMULA_VINEGAR = FunctionCodeUtil.getOneZeroCommand(98, FormulaPreferenceConfig.getTypeBWater(), FormulaPreferenceConfig.getTypeBOil(), FormulaPreferenceConfig.getTypeBBrine(), FormulaPreferenceConfig.getTypeBVinegar(),
            FormulaPreferenceConfig.getTypeAHeat(), FormulaPreferenceConfig.getTypeAHeatSize());
    public static final byte[] B_B_FORMULA_VINEGAR = FunctionCodeUtil.getOneZeroCommand(104, FormulaPreferenceConfig.getTypeBWater(), FormulaPreferenceConfig.getTypeBOil(), FormulaPreferenceConfig.getTypeBBrine(), FormulaPreferenceConfig.getTypeBVinegar(),
            FormulaPreferenceConfig.getTypeBHeat(), FormulaPreferenceConfig.getTypeBHeatSize());
    public static final byte[] B_C_FORMULA_VINEGAR = FunctionCodeUtil.getOneZeroCommand(110, FormulaPreferenceConfig.getTypeBWater(), FormulaPreferenceConfig.getTypeBOil(), FormulaPreferenceConfig.getTypeBBrine(), FormulaPreferenceConfig.getTypeBVinegar(),
            FormulaPreferenceConfig.getTypeCHeat(), FormulaPreferenceConfig.getTypeCHeatSize());
    public static final byte[] C_A_FORMULA_VINEGAR = FunctionCodeUtil.getOneZeroCommand(116, FormulaPreferenceConfig.getTypeCWater(), FormulaPreferenceConfig.getTypeCOil(), FormulaPreferenceConfig.getTypeCBrine(), FormulaPreferenceConfig.getTypeCVinegar(),
            FormulaPreferenceConfig.getTypeAHeat(), FormulaPreferenceConfig.getTypeAHeatSize());
    public static final byte[] C_B_FORMULA_VINEGAR = FunctionCodeUtil.getOneZeroCommand(122, FormulaPreferenceConfig.getTypeCWater(), FormulaPreferenceConfig.getTypeCOil(), FormulaPreferenceConfig.getTypeCBrine(), FormulaPreferenceConfig.getTypeCVinegar(),
            FormulaPreferenceConfig.getTypeBHeat(), FormulaPreferenceConfig.getTypeBHeatSize());
    public static final byte[] C_C_FORMULA_VINEGAR = FunctionCodeUtil.getOneZeroCommand(128, FormulaPreferenceConfig.getTypeCWater(), FormulaPreferenceConfig.getTypeCOil(), FormulaPreferenceConfig.getTypeCBrine(), FormulaPreferenceConfig.getTypeCVinegar(),
            FormulaPreferenceConfig.getTypeCHeat(), FormulaPreferenceConfig.getTypeCHeatSize());

    //功能码0x03
    public static final byte[] CODE_THREE_A_A = new byte[]{(byte) 0xF2, 0x03, 0x00, 0x1A, 0x00, 0x06, (byte) 0xF0, (byte) 0xCC};

    //功能码0x04
    public static final byte[] READ_A_A = new byte[]{(byte) 0xF2, 0x04, 0x00, 0x1A, 0x00, 0x06, 0x45, 0x0C};
    public static final byte[] READ_A_B = new byte[]{(byte) 0xF2, 0x04, 0x00, 0x20, 0x00, 0x06, 0x65, 0x01};

    //----------------------------------------------模组1----------------------------------------------

    //功能码02
    public static final byte[] CHECK_NOODLES = new byte[]{(byte) 0xF2, 0x02, 0x00, 0x01, 0x00, 0x01, (byte) 0xFC, (byte) 0xC9};

    //功能码45
    public static final byte[] CHOICE_NOODLES = new byte[]{(byte) 0xF1, 0x45, 0x00, 0x13, 0x0A, 0x01, (byte) 0xEE, 0x50};//19号米粉
    public static final byte[] CHOICE_NOODLES_DYNAMIC = new byte[]{(byte) 0xF1, 0x45, 0x00, 0x13, 0x0A, 0x01, (byte) 0xEE, 0x50};

    //10进制的米粉号转化为16进制的米粉号
    public static byte[] getChoiceNoodles(int numberMoodles) {
        byte[] addressAndFunction = new byte[]{(byte) 0xF1, 0x45};

        String byteArrToHex = MyFunc.ByteArrToHexNoSpace(intToBytes(numberMoodles));
        String subArr = byteArrToHex.substring(4);
//        Timber.e("subArr="+subArr);
        byte[] getByteArr = MyFunc.HexToByteArr(subArr);

        byte[] featureCode = new byte[]{0x0A, 0x01};//19号米粉

        byte[] combineByteArray = ByteUtil.combineByteArray(ByteUtil.combineByteArray(addressAndFunction, getByteArr), featureCode);
        byte[] crcCode = CRC16.crc16Checkout(combineByteArray);
        return ByteUtil.combineByteArray(combineByteArray, crcCode);
    }


    public static byte[] getFourFiveCommand(int sportCode) {
        byte[] addressAndFunction = new byte[]{(byte) 0xF2, 0x45};

        String byteArrToHex = MyFunc.ByteArrToHexNoSpace(intToBytes(sportCode));
        String subArr = byteArrToHex.substring(4);
        byte[] getByteArr = MyFunc.HexToByteArr(subArr);

        byte[] featureCode = new byte[]{0x0A, 0x01};//19号米粉

        byte[] combineByteArray = ByteUtil.combineByteArray(ByteUtil.combineByteArray(addressAndFunction, getByteArr), featureCode);
        byte[] crcCode = CRC16.crc16Checkout(combineByteArray);
        return ByteUtil.combineByteArray(combineByteArray, crcCode);
    }

    //由选择的具体面的类型和抽到的奖品获得对应的取货通道
    public static ArrayList<byte[]> getChoiceChannel(NoodleEventData noodleEventData) {
        String noodleState = noodleEventData.noodle_state;
        //33 香辣包  55 卤鸡蛋  44 香辣鸡腿
        ArrayList<byte[]> list = new ArrayList<byte[]>();
        switch (noodleState) {
            case NoodleNameConstants.QINGTANG:
                break;
            case NoodleNameConstants.CHONGQING:
                getPackageFormula(DbTypeContants.SUANLABAO, list);
                break;
            case NoodleNameConstants.LUDAN:
                getPackageFormula(DbTypeContants.SUANLABAO, list);
                getPackageFormula(DbTypeContants.LUJIDANG, list);
                break;
            case NoodleNameConstants.HAOHUA://鸡腿
                getPackageFormula(DbTypeContants.SUANLABAO, list);
                getPackageFormula(DbTypeContants.SUANLAJITUI, list);
//                getPackageFormula(55,list);
                break;
            case NoodleNameConstants.SUANLA:
                getPackageFormula(DbTypeContants.SUANLABAO, list);
                break;
                //新鲜面（加辣包）
            case NoodleNameConstants.FRESH:
                getPackageFormula(DbTypeContants.SUANLABAO, list);
                break;
            default:
                break;
        }
        //抽奖时使用，奖品为卤蛋和榨菜 ----------------------------------------------------------------
        if (!noodleEventData.spoilName.isEmpty()) {
            String spoilName = noodleEventData.spoilName.trim();
            switch (spoilName) {
                case NoodleNameConstants.SPOIL_THANKS:
                    break;
                case NoodleNameConstants.SPOIL_LUDAN:
                    getPackageFormula(DbTypeContants.LUJIDANG, list);
                    break;
                case NoodleNameConstants.SPOIL_ZHACAI:
                    getPackageFormula(DbTypeContants.SUANLAJITUI, list);
                    break;
                case NoodleNameConstants.SPOIL_FIRST_PRIZE:
                    getPackageFormula(FormulaPreferenceConfig.getFirstPrizeCategory(), list);
                    break;
                case NoodleNameConstants.SPOIL_SECOND_PRIZE:
                    getPackageFormula(FormulaPreferenceConfig.getSecondPrizeCategory(), list);
                    break;
                case NoodleNameConstants.SPOIL_THIRD_PRIZE:
                    getPackageFormula(FormulaPreferenceConfig.getThirdPrizeCategory(), list);
                    break;
                default:
                    break;
            }
        }
        return list;
    }

    //获取掉料包的指令数组
    private static ArrayList<byte[]> getPackageFormula(int noodleStatus, ArrayList<byte[]> list) {
        ArrayList<RiceND> riceNDs = DBNoodleHelper.querynoodleStatusNoolde(noodleStatus);
        if (riceNDs.isEmpty()) {
            Timber.e("库存noodleStatus=" + noodleStatus);
            return null;
        }
        RiceND riceND = riceNDs.get(0);
        if (riceND.totalNum > 0) {
            int noodleNo = riceND.noodleNo;
            byte[] choiceChannel = getChoiceNoodles(noodleNo);
            list.add(choiceChannel);

            DBNoodleHelper.upateNoodleNum(noodleNo, riceND.totalNum - 1);

        } else {
            Timber.e("库存totalNum=" + riceND.totalNum);
        }
        return list;
    }

    /* int -> byte[] */
    private static byte[] intToBytes(int num) {
        byte[] b = new byte[4];
        for (int i = 0; i < 4; i++) {
            b[i] = (byte) (num >>> (24 - i * 8));
        }

        return b;
    }
}
