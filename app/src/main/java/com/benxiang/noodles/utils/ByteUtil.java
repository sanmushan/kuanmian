package com.benxiang.noodles.utils;

/**
 * Created by Tairong Chan on 2017/1/17.
 * Connect:
 */

public class ByteUtil {

    public static boolean isByteEqual(byte a, byte b) {
        if (byte2int(a) == byte2int(b)) {
            return true;
        }
        return false;
    }

    public static int byte2int(byte data) {
        return data & 0xff;
    }

    public static byte[] combineByteArray(byte[] a, byte[] b) {
        byte[] combine = new byte[a.length + b.length];
        System.arraycopy(a, 0, combine, 0, a.length);
        System.arraycopy(b, 0, combine, a.length, b.length);
        return combine;
    }

    public static int add(byte a, byte b) {
        return byte2int(a) + byte2int(b);
    }
}
