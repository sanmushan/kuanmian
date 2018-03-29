package com.benxiang.noodles.utils;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * Created by admin on 2017/10/21.
 */

public class FunctionCodeUtil {

//    //功能码0x10=>16(十进制)                                              起始地址     寄存器个数   字节数 写入值                                                                             校验码
//                                                                                                      //水         油            卤水         醋           加热         加热大小
//    public static final byte[] TEST_A_A = new byte[]{(byte) 0xF2, 0x10, 0x00, 0x1A, 0x00, 0x06, 0x0C, 0x00, 0x5A , 0x00, 0x46 , 0x00, 0x12 , 0x00, 0x00 , 0x00, 0x5A, 0x00,(byte) 0x96, 0x1E, 0x2B};

    public static byte[] getOneZeroCommand(int startAddress,int water,int oil,int brine,int vinegar,int heat,int heatSize){
        byte[] addressAndFunction = new byte[]{(byte) 0xF2, 0x10};
        byte[] registerAndByteNum = new byte[]{0x00, 0x06,0x0C};

        List<byte[]> list = new ArrayList<>();
        list.add(addressAndFunction);
        list.add(getSubBytes(startAddress));
        list.add(registerAndByteNum);

        list.add(getSubBytes(water));
        list.add(getSubBytes(oil));
        list.add(getSubBytes(brine));
        list.add(getSubBytes(vinegar));
        list.add(getSubBytes(heat));
        list.add(getSubBytes(heatSize));

        byte[] combineByteArray = list.get(0);
        for (int i=1; i<list.size(); i++){
            combineByteArray = ByteUtil.combineByteArray(combineByteArray,list.get(i));
        }
        byte[] crcCode = CRC16.crc16Checkout(combineByteArray);
//        Timber.e("combineByteArray="+MyFunc.ByteArrToHex(ByteUtil.combineByteArray(combineByteArray,crcCode)));
        list =null;
        return ByteUtil.combineByteArray(combineByteArray,crcCode);
    }

    //获得10功能码命令
    private static byte[] getSubBytes(int num){
        String byteArrToHex = MyFunc.ByteArrToHexNoSpace(intToBytes(num));
        String subArr = byteArrToHex.substring(4);
//        Timber.e("subArr="+subArr);
        byte[] getByteArr = MyFunc.HexToByteArr(subArr);
        return getByteArr;
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
