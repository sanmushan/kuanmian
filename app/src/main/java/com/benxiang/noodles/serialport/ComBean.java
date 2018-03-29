package com.benxiang.noodles.serialport;


import java.text.SimpleDateFormat;


public class ComBean {
    public byte[] bRec = null;
    public String sRecTime = "";
    public String sComPort = "";
    public int iSize = 0;
    public int makeOrRecycleNoodles = 0;

    public ComBean(String sPort, byte[] buffer, int size) {
        sComPort = sPort;
        iSize = size;
        bRec = new byte[size];
        for (int i = 0; i < size; i++) {
            bRec[i] = buffer[i];
        }
        SimpleDateFormat sDateFormat = new SimpleDateFormat("hh:mm:ss");
        sRecTime = sDateFormat.format(new java.util.Date());
    }
}