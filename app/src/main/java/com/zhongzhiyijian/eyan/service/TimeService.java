package com.zhongzhiyijian.eyan.service;

import android.app.Service;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.actions.ibluz.factory.IBluzDevice;
import com.actions.ibluz.manager.BluzManager;
import com.actions.ibluz.manager.BluzManagerData;
import com.orhanobut.logger.Logger;
import com.zhongzhiyijian.eyan.base.BaseApplication;
import com.zhongzhiyijian.eyan.base.Constants;
import com.zhongzhiyijian.eyan.util.LogUtil;
import com.zhongzhiyijian.eyan.util.MsgUtil;

/**
 * Created by Administrator on 2017/8/26.
 */

public class TimeService extends Service implements Constants{
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    private BaseApplication app;

    public IBluzDevice mBluzDevice;
    public BluzManager mBluzManager;
    private int keySend;
    private int keyAnswer;
    private TimerThread timerThread;

    private byte[] resultCode;

    private Context mContext;

    private boolean isServiceAlive;

    private int xinCount = 3;

    //广播相关
    private InnerReceiver receiver;
    private IntentFilter filter;


    private boolean isFirstXinTiao;

    @Override
    public void onCreate() {
        app = (BaseApplication) getApplication();
        mContext = getApplicationContext();
        mBluzDevice = app.getBluzConnector();
        mBluzDevice.setOnConnectionListener(mOnConnectionListener);
        isFirstXinTiao = true;

        timerThread = new TimerThread();

        //查询类命令反馈
        byte ba = (byte) Integer.parseInt("F5", 16);
        //设置命令反馈
        byte bb = (byte) Integer.parseInt("F1", 16);
        //心跳反馈
        byte bc = (byte) Integer.parseInt("F2", 16);
        //芯片主动发送
        byte bd = (byte) Integer.parseInt("F3", 16);
        //复位反馈
        byte be = (byte) Integer.parseInt("FC", 16);
        resultCode = new byte[]{ba,bb,bc,bd,be};

        initReceiver();
    }

    private void initReceiver() {
        receiver = new InnerReceiver();
        filter = new IntentFilter();
        filter.addAction(SEND_MSG_TO_DEVICE);
        registerReceiver(receiver,filter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

//        Logger.e("运行心跳线程");

        isServiceAlive = true;

        if (!timerThread.isInterrupted()){
            if(!timerThread.isAlive()){
                timerThread.start();
            }
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isServiceAlive = false;
        unregisterReceiver(receiver);
    }

    private IBluzDevice.OnConnectionListener mOnConnectionListener = new IBluzDevice.OnConnectionListener() {
        @Override
        public void onConnected(BluetoothDevice device) {
            Logger.e("设备连接成功，创建BluzManager对象并开启心跳");
            keySend = BluzManager.buildKey(BluzManagerData.CommandType.QUE, 0x81);
            keyAnswer = BluzManager.buildKey(BluzManagerData.CommandType.ANS, 0x81);
            mBluzManager = new BluzManager(mContext, mBluzDevice, new BluzManagerData.OnManagerReadyListener() {
                @Override
                public void onReady() {
                    // BluzManager接口调用请写于此
                    mBluzManager.setOnCustomCommandListener(new BluzManagerData.OnCustomCommandListener() {
                        @Override
                        public void onReady(int what, int arg1, int arg2, byte[] arg3) {
                            if (what == keyAnswer){
                                String result = printHexString(arg3);

//                                Logger.e("收到蓝牙反馈>>> result = " + result);

                                if(arg3[4] == resultCode[0]){
                                    checkQuaryResult(arg3);
                                }else if(arg3[4] == resultCode[1]){
                                    //设置后主动发起查询，忽略设置返回值
//                                    Logger.e("蓝牙反馈>>>设置命令反馈" + result);
                                }else if(arg3[4] == resultCode[2]){
                                    xinCount = 0;
                                    xintiao(arg3);
                                }else if(arg3[4] == resultCode[3]){
                                    changeTypeOrError(arg3);
                                }else if(arg3[4] == resultCode[4]){
                                    checkRecet(arg3);
                                }

                            }
                        }
                    });
                }
            });

        }


        @Override
        public void onDisconnected(BluetoothDevice device) {
            mBluzManager.release();
            mBluzManager = null;
        }
    };

    /**
     * 查询命令反馈处理
     * @param arg3
     */
    private void checkQuaryResult(byte[] arg3) {
        int code = arg3[5];
        if(code == 1){
            Logger.e("设备ID返回值==>");
        }else if(code == 2){
            Logger.e("工作模式查询返回值==>"+printHexString(arg3));
            String type = Integer.toHexString(arg3[6] & 0xFF);

            String qiangduStr = Integer.toHexString(arg3[7] & 0xFF);
            qiangduStr = Integer.valueOf(qiangduStr,16).toString();
            int qiangdu = Integer.parseInt(qiangduStr);

            String time1Str = Integer.toHexString(arg3[8] & 0xFF);
            time1Str = Integer.valueOf(time1Str,16).toString();
            int time1 = Integer.parseInt(time1Str);
            String time2Str = Integer.toHexString(arg3[9] & 0xFF);
            time2Str = Integer.valueOf(time2Str,16).toString();
            int time2 = Integer.parseInt(time2Str);

            int time = time1*256+time2;
            Logger.e("物理切换工作状态===>状态=" + type + "强度=" + qiangdu + "time=" + time);

            Intent it = new Intent();
            it.setAction(WORK_TYPE_CHANGED);
            it.putExtra("type",type);
            it.putExtra("qiangdu",qiangdu);
            it.putExtra("time",time);
            sendBroadcast(it);
        }else if(code == 4){
            Logger.e("电压查询返回值==>");
        }
    }

    /**
     * 检测复位是否成功
     * @param arg3
     */
    private void checkRecet(byte[] arg3) {
        if(arg3[3] == 85){
            Logger.e("蓝牙反馈>>>复位成功");
        }else{
            Logger.e("蓝牙反馈>>>复位失败");
        }
    }

    /**
     * 心跳反馈
     * @param arg3
     */
    private void xintiao(byte[] arg3) {
        if(arg3[3] == 0){
            LogUtil.showLog("蓝牙反馈>>>心跳反馈 电极连接：不通====高压连接 ：不通");
            app.workStatus = WORK_STATUS_DIANJI_OFF_GAOYA_OFF;
        }else if(arg3[3] == 1){
            LogUtil.showLog("蓝牙反馈>>>心跳反馈 电极连接：通====高压连接 ：不通");
            app.workStatus = WORK_STATUS_DIANJI_ON_GAOYA_OFF;
        }else if(arg3[3] == 2){
            LogUtil.showLog("蓝牙反馈>>>心跳反馈 电极连接：不通====高压连接 ：通");
            app.workStatus = WORK_STATUS_DIANJI_OFF_GAOYA_ON;
        }else if(arg3[3] == 3){
            LogUtil.showLog("蓝牙反馈>>>心跳反馈 电极连接：通====高压连接 ：通");
            app.workStatus = WORK_STATUS_DIANJI_ON_GAOYA_ON;
        }
        if(isFirstXinTiao){
            isFirstXinTiao = false;
            //连接成功后发送复位命令
            mBluzManager.sendCustomCommand(keySend, 0, 0, MsgUtil.getResetByte());
        }
    }


    /**
     * 工作模式手动切换或错误代码
     * @param result
     */
    private void changeTypeOrError(byte[] result) {
        if(result[5] == 1){
            //错误代码
            int code = result[6];
            Logger.e("错误代码===>" + code);
        }else if(result[5] == 2){
            //工作状态改变
            Logger.e("物理切换工作状态===>" + printHexString(result));
            String type = Integer.toHexString(result[6] & 0xFF);

            String qiangduStr = Integer.toHexString(result[7] & 0xFF);
            qiangduStr = Integer.valueOf(qiangduStr,16).toString();
            int qiangdu = Integer.parseInt(qiangduStr);

            String time1Str = Integer.toHexString(result[8] & 0xFF);
            time1Str = Integer.valueOf(time1Str,16).toString();
            int time1 = Integer.parseInt(time1Str);
            String time2Str = Integer.toHexString(result[9] & 0xFF);
            time2Str = Integer.valueOf(time2Str,16).toString();
            int time2 = Integer.parseInt(time2Str);

            int time = time1*256+time2;
            Logger.e("物理切换工作状态===>状态=" + type + "强度=" + qiangdu + "time=" + time);

            Intent it = new Intent();
            it.setAction(WORK_TYPE_CHANGED);
            it.putExtra("type",type);
            it.putExtra("qiangdu",qiangdu);
            it.putExtra("time",time);
            sendBroadcast(it);
        }
    }


    class TimerThread extends Thread {
        @Override
        public void run() {
            while (isServiceAlive){
                if(xinCount >= 3){
                   app.isConnect = false;
                    Logger.e("3次未有心跳反馈，按摩板连接异常，退出操作界面");
                    Intent it = new Intent();
                    it.setAction(XINTIAO_DISCONNECTED);
                    sendBroadcast(it);
                }else{
                    app.isConnect = true;
                }
                try {
                    sleep(1000);
                    if(mBluzManager != null){
                        mBluzManager.sendCustomCommand(keySend, 0, 0, MsgUtil.getXinTiao());
//                        Logger.e("发送心跳命令");
                    }
                    xinCount++;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String printHexString(byte[] b) {
        String result = "";
        for (int i = 0; i < b.length; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            result += hex.toUpperCase()+" ";
        }
        return result;
    }


    private class InnerReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals(SEND_MSG_TO_DEVICE)){
                byte[] bs = intent.getByteArrayExtra("byte");
                Logger.e("收到广播，发送蓝牙命令"+printHexString(bs));
                if(mBluzManager != null){
                    mBluzManager.sendCustomCommand(keySend, 0, 0, bs);
                    try {
                        Thread.sleep(300);
                        mBluzManager.sendCustomCommand(keySend, 0, 0, MsgUtil.getCurWorkType());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }



}
