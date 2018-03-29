package com.benxiang.noodles.utils;

/**
 * Created by admin on 2017/10/20.
 */

public class ErrorCodeUtil {

    public static String parse(String errorCode){
        int mErrorCode = Integer.parseInt(errorCode.trim());
        String parseErrorCode = "";
        switch (mErrorCode){
            case 1:
                parseErrorCode = "24V电源故障";
                break;
            case 2:
                parseErrorCode = "传感器电源关闭";
                break;
            case 3:
                parseErrorCode = "A温度传感器断开";
                break;
            case 4:
                parseErrorCode = "B温度传感器断开";
                break;
            case 5:
                parseErrorCode = "C温度传感器断开";
                break;
            case 6:
                parseErrorCode = "D温度传感器断开";
                break;
            case 7:
                parseErrorCode = "X01传感器故障";
                break;
            case 8:
                parseErrorCode = "X02传感器故障";
                break;
            case 9:
                parseErrorCode = "X03传感器故障";
                break;
            case 10:
                parseErrorCode = "X04传感器故障";
                break;
            case 11:
                parseErrorCode = "X05传感器故障";
                break;
            case 12:
                parseErrorCode = "X06传感器故障";
                break;
            case 13:
                parseErrorCode = "X07传感器故障";
                break;
            case 14:
                parseErrorCode = "X08传感器故障";
                break;
            case 15:
                parseErrorCode = "X09传感器故障";
                break;
            case 16:
                parseErrorCode = "X10传感器故障";
                break;
            case 17:
                parseErrorCode = "X11传感器故障";
                break;
            case 18:
                parseErrorCode = "X12传感器故障";
                break;
            case 19:
                parseErrorCode = "X13传感器故障";
                break;
            case 20:
                parseErrorCode = "X14传感器故障";
                break;
            case 21:
                parseErrorCode = "X15传感器故障";
                break;
            case 22:
                parseErrorCode = "X16传感器故障";
                break;
            case 23:
                parseErrorCode = "X17传感器故障";
                break;
            case 24:
                parseErrorCode = "X18传感器故障";
                break;
            case 25:
                parseErrorCode = "X19传感器故障";
                break;
            case 26:
                parseErrorCode = "X20传感器故障";
                break;
            case 27:
                parseErrorCode = "X21传感器故障";
                break;
            case 28:
                parseErrorCode = "X22传感器故障";
                break;
            case 29:
                parseErrorCode = "X23传感器故障";
                break;
            case 30:
                parseErrorCode = "X24传感器故障";
                break;
            case 31:
                parseErrorCode = "X25传感器故障";
                break;
            case 32:
                parseErrorCode = "X25传感器故障";
                break;
            case 33:
                parseErrorCode = "X27传感器故障";
                break;
            case 34:
                parseErrorCode = "X28传感器故障";
                break;
            case 35:
                parseErrorCode = "X29传感器故障";
                break;
            case 36:
                parseErrorCode = "X30传感器故障";
                break;
            case 37:
                parseErrorCode = "X31传感器故障";
                break;
            case 38:
                parseErrorCode = "X32传感器故障";
                break;
            case 39:
                parseErrorCode = "X41传感器故障";
                break;
            case 40:
                parseErrorCode = "X42传感器故障";
                break;
//            case 41:
//                parseErrorCode = "异常停电";
//                break;
//            case 42:
//                parseErrorCode = "作业超时";
//                break;
//            case 43:
//                parseErrorCode = "马达没运动";
//                break;
//            case 44:
//                parseErrorCode = "X01传感器故障";
//                break;
//            case 45:
//                parseErrorCode = "X01传感器故障";
//                break;
//            case 46:
//                parseErrorCode = "X01传感器故障";;
            case 47:
                parseErrorCode = "异常停电";
                break;
            case 48:
                parseErrorCode = "作业超时";
                break;
            case 49:
                parseErrorCode = "马达没运动";
                break;
            case 50:
                parseErrorCode = "无电流";
                break;
            case 51:
                parseErrorCode = "电流过大";
                break;
            case 52:
                parseErrorCode = "无气压力";
                break;
            case 53:
                parseErrorCode = "气压过高";
                break;
            case 54:
                parseErrorCode = "温度过低";
                break;
            case 55:
                parseErrorCode = "温度过高";
                break;
            case 56:
                parseErrorCode = "液位过低";
                break;
            case 57:
                parseErrorCode = "液位过高";
                break;
            case 58:
                parseErrorCode = "缺料（非液体）";
                break;
            case 59:
                parseErrorCode = "缺料预警";
                break;
            case 60:
                parseErrorCode = "通道阻塞";
                break;
            case 61:
                parseErrorCode = "运动没到位";
                break;
            case 62:
                parseErrorCode = "油预警";
                break;
            case 63:
                parseErrorCode = "卤水预警";
                break;
            case 64:
                parseErrorCode = "醋预警";
                break;
            case 65:
                parseErrorCode = "筷子预警";
                break;
            case 500:
                parseErrorCode = "超时异常";
                break;
            case 501:
                parseErrorCode = "多次检查皮带出错";
                break;
            default:
                parseErrorCode = "未知错误异常"+mErrorCode;
                break;
        }
        return parseErrorCode;
    }
}
