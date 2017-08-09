package com.zhongzhiyijian.eyan.util;

import android.content.Context;
import android.text.TextUtils;

import com.orhanobut.logger.Logger;
import com.zhongzhiyijian.eyan.base.Constants;
import com.zhongzhiyijian.eyan.entity.User;
import com.zhongzhiyijian.eyan.entity.UserData;
import com.zhongzhiyijian.eyan.net.Urls;
import com.zhongzhiyijian.eyan.user.UserPersist;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.DbManager;
import org.xutils.common.Callback;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/1/1.
 */

public class DataUtil implements Constants{

    private static DataUtil dataUtil;


    private boolean isTimeRun;

    private UserData curUserData;
    private DbManager dbUtils;

    private String curPattern;
    private int curStrength;

    private SimpleDateFormat sdf;

    private MyThread myThread;

    private int curSid;
    private String curToken;
    private String curEid;

    private DataUtil(){
        dbUtils = x.getDb(XUtil.getDaoConfig());
        sdf = new SimpleDateFormat("dd-HH:mm:ss");
    }

    public static DataUtil getInstance(){
        if(dataUtil == null){
            synchronized (DataUtil.class){
                dataUtil = new DataUtil();
            }
        }
        return dataUtil;
    }

    public void startData(String pattern,int strength,String token,int sid,String eId){
        stopData();
        curPattern = pattern;
        curStrength = strength;
        curSid = sid;
        curToken = token;
        curEid = eId;
        curUserData = new UserData();
        isTimeRun = true;
        curUserData.setUpdate(false);
        curUserData.setPattern(curPattern);
        curUserData.setStrength(curStrength);
        curUserData.setToken(curToken);
        curUserData.setSid(curSid);
        curUserData.setEquipment_id(curEid);
        curUserData.setStart_time(System.currentTimeMillis());
        Logger.e("开始使用记录，模式:" + curUserData.getPattern()
                + ",强度：" + curUserData.getStrength());
        Logger.e(curUserData.toString());
        myThread = null;
        myThread = new MyThread();
        myThread.start();
    }

    public void stopData(){
        if (curUserData != null){
            isTimeRun = false;
            myThread = null;
            new Thread(){
                @Override
                public void run() {
                    try {
                        sleep(1200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
            UpLoadUserData(curUserData);
            Logger.e("停止模式:" + curUserData.getPattern() + ",强度：" + curUserData.getStrength());
            curUserData = null;
        }
    }

    public void stopDataWithOutUpDate(){
        if (curUserData != null){
            isTimeRun = false;
            myThread = null;
            new Thread(){
                @Override
                public void run() {
                    try {
                        sleep(1200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
            Logger.e("异常退出，停止模式:" + curUserData.getPattern() + ",强度：" + curUserData.getStrength());
            curUserData = null;
        }
    }

    public void changeStrength(int strength){
//        stopData();
//        curStrength = strength;
//        curUserData = new UserData();
//        isTimeRun = true;
//        curUserData.setUpdate(false);
//        curUserData.setPattern(curPattern);
//        curUserData.setStrength(curStrength);
//        curUserData.setToken(curToken);
//        curUserData.setSid(curSid);
//        curUserData.setEquipment_id(curEid);
//        curUserData.setStart_time(System.currentTimeMillis());
//        Logger.e("改变模式:" + curUserData.getPattern() +"-" + curUserData.getId() + ",强度：" + curUserData.getStrength());
//        myThread = null;
//        myThread = new MyThread();
//        myThread.start();
        curStrength = strength;
        curUserData.setStrength(curStrength);
        Logger.e("改变当前模式的强度为" + curStrength);
    }

    public void destroyUtil(){
        stopDataWithOutUpDate();
//        try {
//            dbUtils.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    /**
     * 修改使用记录终止时间
     */
    class MyThread extends Thread{
        @Override
        public void run() {
            while (isTimeRun &&curUserData!=null){
                synchronized (MyThread.class){
                    try {
                        if(curUserData!=null){
                            curUserData.setStop_time(System.currentTimeMillis());
                            dbUtils.close();
                            dbUtils = null;
                            if (dbUtils == null){
                                dbUtils = x.getDb(XUtil.getDaoConfig());
                            }
                            dbUtils.saveOrUpdate(curUserData);
//                            Logger.e("当前模式:" + curUserData.getPattern() +"-" + curUserData.getId() + ",强度：" + curUserData.getStrength() + ",终止时间:" + sdf.format(new Date()));
                            sleep(1000);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (DbException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


    public void FindAndUpLoad(){
        if (dbUtils == null){
            dbUtils = x.getDb(XUtil.getDaoConfig());
        }
        try {
            List<UserData> all = dbUtils.findAll(UserData.class);
            List<UserData> unUpDate = new ArrayList<>();
            if(all != null){
                for (UserData ud : all){
                    if (!ud.isUpdate()){
                        unUpDate.add(ud);
                        UpLoadUserData(ud);
                    }
                }
                Logger.e("查询到"+all.size()+"条使用记录,其中未上传记录"+unUpDate.size()+"条");
            }
        } catch (DbException e) {
            Logger.e(e.getMessage());
        }
    }


    /**
     * 上传使用记录
     */
    private void UpLoadUserData(final UserData ud){
        if(ud != null && !ud.isUpdate() && !TextUtils.isEmpty(ud.getToken())){
            if (ud.getStop_time() - ud.getStart_time() <= 10000){
                if (dbUtils == null){
                    dbUtils = x.getDb(XUtil.getDaoConfig());
                }
                try {
                    dbUtils.delete(ud);
                } catch (DbException e) {
                    e.printStackTrace();
                }
                return;
            }
            RequestParams params = new RequestParams(Urls.addEquipmentRecord);
            params.addParameter("token",ud.getToken());
            params.addParameter("sid",ud.getSid());
            params.addParameter("equipment_id",ud.getEquipment_id());
            params.addParameter("pattern",ud.getPattern());
            params.addParameter("strength",ud.getStrength());
            params.addParameter("start_time",ud.getStart_time()/1000);
            params.addParameter("stop_time",ud.getStop_time()/1000);
            x.http().post(params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
//                    Logger.e(result);
                    try {
                        JSONObject object = new JSONObject(result);
                        String returnCode = object.getString("return_code");
                        if (returnCode.equals(SUCCESS)){
                            ud.setUpdate(true);
                            if (dbUtils == null){
                                dbUtils = x.getDb(XUtil.getDaoConfig());
                            }
                            dbUtils.saveOrUpdate(ud);
                            Logger.e("上传数据成功，本地数据库变更数据isUpDate为true--->" + ud.toString());
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    Logger.e(ex.getMessage());
                }
                @Override
                public void onCancelled(CancelledException cex) {
                }
                @Override
                public void onFinished() {
                }
            });
        }
    }


}
