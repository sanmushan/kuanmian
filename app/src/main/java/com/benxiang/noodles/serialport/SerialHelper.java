package com.benxiang.noodles.serialport;


import com.benxiang.noodles.utils.MyFunc;
import com.benxiang.noodles.utils.RxCountDown;
import com.blankj.utilcode.util.LogUtils;

import org.cnbleu.serialport.SerialPort;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidParameterException;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import timber.log.Timber;

/**
 *
 *
 *
 */
public abstract class SerialHelper {

    private SerialPort mSerialPort;
    private OutputStream mOutputStream;
    private InputStream mInputStream;
    private ReadThread mReadThread;
    private SendThread mSendThread;
    private String sPort = "/dev/s3c2410_serial0";
    private int iBaudRate = 9600;
    private boolean _isOpen = false;
    private byte[] _bLoopData = new byte[]{0x30};
    private int iDelay = 50;
    //Rx相关
    private CompositeDisposable compositeDisposables;
    private volatile boolean isReceivedSussess = false;
    private byte[] mBOutArray;
    private int errorTime = 0;

    public void startDownTime() {
        unDisposable();
        //被观察者发送格式化后的时间
        if (!isReceivedSussess) {
            Disposable disposable = RxCountDown.countdown(20)
                    .subscribe(new Consumer<Integer>() {
                        @Override
                        public void accept(@NonNull Integer s) throws Exception {
                            if (s.equals(10)){
                                if (!isReceivedSussess){
                                    logStep(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>已经10S没返回数据");
                                }
                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(@NonNull Throwable throwable) throws Exception {
//                            Log.e(TAG, "accept: " + throwable);
                        }
                    }, new Action() {
                        @Override
                        public void run() throws Exception {
                            if (!isReceivedSussess){
                                logStep(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>没返回数据，再次发送数据");
                                errorTime++;
                                if (errorTime <= 3){
                                    send(mBOutArray);
                                }else {
                                    errorTime = 0;
                                    onOverTime();
                                }
                            }
                        }
                    });
            addSubscribe(disposable);
        }
    }

    protected void addSubscribe(Disposable disposable) {
        if (compositeDisposables == null) {
            compositeDisposables = new CompositeDisposable();
        }
        compositeDisposables.add(disposable);
    }

    protected void unDisposable() {
        if (compositeDisposables != null) {
            compositeDisposables.clear();
            compositeDisposables = null;
        }
    }
    //----------------------------------------------------

    /**
     * 4 种构造方法 ： 参数均可通过 setPort() 和 setBaudRate() 修改
     * 1.		（无参数） ：							默认设备，默认波特率
     * 2.		（串口） ：								自选设备，默认波特率
     * 3/4.		（串口，波特率【 int / String 】） :		自选设备，自选波特率
     */
    public SerialHelper(String sPort, int iBaudRate) {
        this.sPort = sPort;
        this.iBaudRate = iBaudRate;
    }

    public SerialHelper() {
        this("/dev/s3c2410_serial0", 9600);
    }

    public SerialHelper(String sPort) {
        this(sPort, 9600);
    }

    public SerialHelper(String sPort, String sBaudRate) {
        this(sPort, Integer.parseInt(sBaudRate));
    }
    //----------------------------------------------------

    /**
     * 打开指定串口，并开启读写线程
     *
     * @throws SecurityException
     * @throws IOException
     * @throws InvalidParameterException
     */
    public void open() throws SecurityException, IOException, InvalidParameterException {
        mSerialPort = new SerialPort(new File(sPort), iBaudRate);
        mOutputStream = mSerialPort.getOutputStream();
        mInputStream = mSerialPort.getInputStream();
        mReadThread = new ReadThread();
        mReadThread.start();
        mSendThread = new SendThread();
        mSendThread.setSuspendFlag();
        mSendThread.start();
        _isOpen = true;
    }
    //----------------------------------------------------

    /**
     * 关闭串口，关闭读线程
     */
    public void close() {
        if (mReadThread != null)
            mReadThread.interrupt();
        if (mSerialPort != null) {
            mSerialPort.close();
            mSerialPort = null;
        }
        _isOpen = false;
    }
    //----------------------------------------------------

    /**
     * 发送数组数据
     *
     * @param bOutArray
     */
    public void send(byte[] bOutArray) {
        send(bOutArray, 200);
    }

    public void send(byte[] bOutArray, int delay) {
        mBOutArray = bOutArray;
        try {
            if (delay > 0) {
                Thread.sleep(delay);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            logStep("发送 >>>>>>>> " + MyFunc.ByteArrToHex(bOutArray));
            unDisposable();
            isReceivedSussess =false;
            startDownTime();
            mOutputStream.write(bOutArray);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送 16 进制 数据
     *
     * @param sHex
     */
    public void sendHex(String sHex) {
        byte[] bOutArray = MyFunc.HexToByteArr(sHex);
        send(bOutArray);
    }

    /**
     * 发送文本数据
     *
     * @param sTxt
     */
    public void sendTxt(String sTxt) {
        byte[] bOutArray = sTxt.getBytes();
        send(bOutArray);
    }
    //----------------------------------------------------

    /**
     * 接收 子线程
     */
    private class ReadThread extends Thread {
        @Override
        public void run() {
            super.run();
            while (!isInterrupted()) {
                    try {
                        if (mInputStream == null) return;
                        byte[] buffer = new byte[512];
                        int size = mInputStream.read(buffer);
                        if (size > 0) {
                            ComBean ComRecData = new ComBean(sPort, buffer, size);
                            onDataReceived(ComRecData);
                            //接收成功，取消当前倒计时
                            isReceivedSussess = true;
                            unDisposable();
                        }
                        try {
                            // 370 板子, 速率 1000 是可以接受的
//                            Thread.sleep(1000);
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                        return;
                    }
            }
        }
    }
    //----------------------------------------------------

    /**
     * 发送 子线程
     */
    private class SendThread extends Thread {
        public boolean suspendFlag = true;// // 控制 发送 线程的执行与否

        @Override
        public void run() {
            super.run();
            while (!isInterrupted()) {
                synchronized (this) {
                    while (suspendFlag) {
                        try {
                            wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                }
                send(getbLoopData());
                try {
                    Thread.sleep(iDelay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        //线程暂停
        public void setSuspendFlag() {
            this.suspendFlag = true;
        }

        //唤醒进程
        public synchronized void setResume() {
            this.suspendFlag = false;
            notify();
        }
    }
    //----------------------------------------------------

    /**
     * 波特率的获取与设置
     *
     * @return
     */
    public int getBaudRate() {
        return iBaudRate;
    }

    public boolean setBaudRate(int iBaud) {
        if (_isOpen) {
            return false;
        } else {
            iBaudRate = iBaud;
            return true;
        }
    }

    public boolean setBaudRate(String sBaud) {
        int iBaud = Integer.parseInt(sBaud);
        return setBaudRate(iBaud);
    }
    //----------------------------------------------------

    /**
     * 串口路径的获取与设置
     *
     * @return
     */
    public String getPort() {
        return sPort;
    }

    public boolean setPort(String sPort) {
        if (_isOpen) {
            return false;
        } else {
            this.sPort = sPort;
            return true;
        }
    }

    //----------------------------------------------------
    public boolean isOpen() {
        return _isOpen;
    }

    //----------------------------------------------------
    public byte[] getbLoopData() {
        return _bLoopData;
    }

    //----------------------------------------------------
    public void setbLoopData(byte[] bLoopData) {
        this._bLoopData = bLoopData;
    }

    //----------------------------------------------------
    public void setTxtLoopData(String sTxt) {
        this._bLoopData = sTxt.getBytes();
    }

    //----------------------------------------------------
    public void setHexLoopData(String sHex) {
        this._bLoopData = MyFunc.HexToByteArr(sHex);
    }
    //----------------------------------------------------

    /**
     * 设置接收延迟
     */
    public int getiDelay() {
        return iDelay;
    }

    public void setiDelay(int iDelay) {
        this.iDelay = iDelay;
    }

    //----------------------------------------------------
    public void startSend() {
        if (mSendThread != null) {
            mSendThread.setResume();
        }
    }

    //----------------------------------------------------
    public void stopSend() {
        if (mSendThread != null) {
            mSendThread.setSuspendFlag();
        }
    }

    //----------------------------------------------------
    protected abstract void onDataReceived(ComBean ComRecData);

    protected abstract void onOverTime();

    private void logStep(String msg) {
        Timber.e(msg);
        LogUtils.file(msg);
    }
}