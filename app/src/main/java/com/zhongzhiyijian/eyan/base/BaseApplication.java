package com.zhongzhiyijian.eyan.base;

import android.app.Application;
import android.app.Service;
import android.os.Vibrator;

import com.actions.ibluz.factory.BluzDeviceFactory;
import com.actions.ibluz.factory.IBluzDevice;
import com.igexin.sdk.PushManager;
import com.zhongzhiyijian.eyan.entity.Music;
import com.zhongzhiyijian.eyan.service.LocationService;
import com.zhongzhiyijian.eyan.service.PushIntentService;
import com.zhongzhiyijian.eyan.service.PushService;
import com.zhongzhiyijian.eyan.util.XUtil;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangfan on 2016/3/25.
 */
public class BaseApplication extends Application {


    /**
     * 歌曲的List集合
     */
    private ArrayList<Music> musics;
    /**
     * 当前播放的歌曲
     */
    private int currentMusicIndex;
    /**
     * 播放模式
     */
    private int playMode;

    private DbManager dbUtils;

    public LocationService locationService;
    public Vibrator mVibrator;
    //设备是否连接
    public boolean isDeviceConnection;
    public int devicePower;
    public boolean incharge;
    //语言是否改变
    public boolean isLanChanged;
    //文字大小是否改变
    public boolean isSizeChanged;
    public boolean isChanged;

    public boolean isSizeLock = true;

    public IBluzDevice btConnector;


    @Override
    public void onCreate() {
        // 获取歌曲的List集合
//        setMusicList(new MusicDao(this).getMusicList());
        x.Ext.init(this);
        getMusicFromDB();
        isDeviceConnection = false;
        devicePower = 4;
        incharge = false;
        isLanChanged = false;
        isSizeChanged = false;
        super.onCreate();

        /***
         * 初始化定位sdk，建议在Application中创建
         */
        locationService = new LocationService(getApplicationContext());
        mVibrator =(Vibrator)getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);

        PushManager.getInstance().initialize(this.getApplicationContext(), PushService.class);
        PushManager.getInstance().registerPushIntentService(this.getApplicationContext(), PushIntentService.class);

    }



    /**
     * 获取歌曲的List集合
     *
     * @return 歌曲的List集合
     */
    public ArrayList<Music> getMusicList() {
        return this.musics;
    }

    public void setMusicList(ArrayList<Music> ms) {
        if (ms == null) {
            musics = new ArrayList<Music>();
        } else {
            musics = ms;
            saveMusic(ms);
        }
    }

    /**
     * 获取当前正在播放的歌曲的索引
     *
     * @return 当前正在播放的歌曲的索引
     */
    public int getCurrentMusicIndex() {
        return currentMusicIndex;
    }

    /**
     * 设置当前播放的歌曲的索引
     *
     * @param currentMusicIndex
     *            当前播放的歌曲的索引
     */
    public void setCurrentMusicIndex(int currentMusicIndex) {
        this.currentMusicIndex = currentMusicIndex;
    }

    public int getPlayMode() {
        return playMode;
    }

    public void setPlayMode(int playMode) {
        this.playMode = playMode;
    }


    public void saveMusic(ArrayList<Music> ms){
        dbUtils = x.getDb(XUtil.getDaoConfig());
        try {
            dbUtils.delete(Music.class);
            dbUtils.saveBindingId(ms);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    public void getMusicFromDB(){
        dbUtils = x.getDb(XUtil.getDaoConfig());
        try {
            List<Music> ms = dbUtils.findAll(Music.class);
            if (ms == null){
                this.musics = new ArrayList<>();
            }else{
                this.musics = (ArrayList<Music>) ms;
            }
        } catch (DbException e) {
            e.printStackTrace();
            this.musics = new ArrayList<>();
        }
    }

    public IBluzDevice getBluzConnector(){
        if (btConnector == null){
            btConnector = BluzDeviceFactory.getDevice(getApplicationContext());
        }
        return btConnector;
    }



    public boolean isConnect = false;

    public int workStatus = 0;

}
