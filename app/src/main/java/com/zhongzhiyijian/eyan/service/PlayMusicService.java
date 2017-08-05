package com.zhongzhiyijian.eyan.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import com.orhanobut.logger.Logger;
import com.zhongzhiyijian.eyan.base.BaseApplication;
import com.zhongzhiyijian.eyan.entity.Music;
import com.zhongzhiyijian.eyan.base.Constants;
import com.zhongzhiyijian.eyan.util.LogUtil;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.IBinder;

/**
 * 播放音乐的Service
 *
 */
public class PlayMusicService extends Service implements Constants {
	/**
	 * 播放器
	 */
	private MediaPlayer player;
	/**
	 * 广播接收者
	 */
	private BroadcastReceiver receiver;
	/**
	 * 广播接收者的意图过滤器
	 */
	private IntentFilter filter;
	/**
	 * Application对象
	 */
	private BaseApplication app;
	/**
	 * 歌曲的List集合
	 */
	private ArrayList<Music> musics;
	/**
	 * 是否正在播放
	 */
	private boolean isPlaying;
	/**
	 * 暂停时的进度
	 */
	private int currentPosition;
	/**
	 * 用于获取歌曲进度，并请求Activity更新的子线程
	 */
	private UpdateProgressThread thread;
	/**
	 * 线程是否工作
	 */
	public boolean isThreadWorking;
	/**
	 * 是否手动点击开始播放过
	 */
	private boolean isStarted;


	@Override
	public void onCreate() {
		LogUtil.showLog("PlayMusicService onCreat");
		// 获取Application对象
		app = (BaseApplication) getApplication();
		// 获取歌曲的List集合
		musics = app.getMusicList();
		// 初始化播放器
		player = new MediaPlayer();
		// 播放结束后下一首
		player.setOnCompletionListener(new OnCompletionListener() {
			@Override
			public void onCompletion(MediaPlayer mp) {
				if (isStarted) {
					next();
				}
			}
		});
		// 注册广播接收者
		initReceiver();

		super.onCreate();
	}

	/**
	 * 初始化并注册广播接收者
	 */
	private void initReceiver() {
		// 初始化广播接收者
		receiver = new InnerReceiver();
		// 初始化意图过滤器
		filter = new IntentFilter();
		// 添加接收的广播类型
		filter.addAction(ACTIVITY_PLAY_BUTTON_CLICK);
		filter.addAction(ACTIVITY_PREVIOUS_BUTTON_CLICK);
		filter.addAction(ACTIVITY_NEXT_BUTTON_CLICK);
		filter.addAction(ACTIVITY_MUSIC_INDEX_CHANGED);
		filter.addAction(ACTIVITY_SEEKBAR_CHANGED);
		filter.addAction(ACTIVITY_CURRENT_MUSIC_CLICK);
		filter.addAction(ACTIVITY_CURRENT_MUSIC_DELETE);
		// 注册广播接收者
		registerReceiver(receiver, filter);

		IntentFilter it = new IntentFilter();
		it.addAction(Constants.MusicPlayControl.SERVICECMD);
		it.addAction(Constants.MusicPlayControl.TOGGLEPAUSE_ACTION);
		it.addAction(Constants.MusicPlayControl.PAUSE_ACTION);
		it.addAction(Constants.MusicPlayControl.NEXT_ACTION);
		it.addAction(Constants.MusicPlayControl.PREVIOUS_ACTION);
		registerReceiver(BlueToothReceiver, it);
	}


    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);

        IntentFilter it = new IntentFilter();
        it.addAction(Constants.MusicPlayControl.SERVICECMD);
        it.addAction(Constants.MusicPlayControl.TOGGLEPAUSE_ACTION);
        it.addAction(Constants.MusicPlayControl.PAUSE_ACTION);
        it.addAction(Constants.MusicPlayControl.NEXT_ACTION);
        it.addAction(Constants.MusicPlayControl.PREVIOUS_ACTION);
        registerReceiver(BlueToothReceiver, it);
    }

    /**
	 * 
	 * 广播接收者
	 * 
	 */
	private class InnerReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();

			musics = app.getMusicList();

			if (ACTIVITY_PLAY_BUTTON_CLICK.equals(action)) {
				LogUtil.showLog("play or pause has be clicked");
				// 当播放按钮被点击时
				if (isPlaying) {
					pause();
				} else {
					play();
				}
			} else if (ACTIVITY_PREVIOUS_BUTTON_CLICK.equals(action)) {
				previous();
			} else if (ACTIVITY_NEXT_BUTTON_CLICK.equals(action)) {
				next();
			} else if (ACTIVITY_MUSIC_INDEX_CHANGED.equals(action)) {
				currentPosition = 0;
				thread = null;
				play();
			} else if (ACTIVITY_SEEKBAR_CHANGED.equals(action)) {
				int progress = intent.getIntExtra("progress", 0);
				currentPosition = player.getDuration() * progress / 100;
				play();
			} else if(ACTIVITY_CURRENT_MUSIC_CLICK.equals(action)){
				if(!isPlaying){
					play();
				}
			} else if(ACTIVITY_CURRENT_MUSIC_DELETE.equals(action)){
				isThreadWorking = false;
				// 暂停
				player.pause();
				// 记录暂停的位置
				currentPosition = 0;
				// 更新播放状态
				isPlaying = false;
				isStarted = false;

//				isPlaying = false;
//				player.pause();
//				// 发送广播，通知Activity歌曲已经暂停
//				currentPosition = 0;
//				thread = null;
				Intent intent1 = new Intent();
				intent1.setAction(SERVICE_PLAYER_PAUSE);
				sendBroadcast(intent1);
			}
		}
	}

	private BroadcastReceiver BlueToothReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String cmd = intent.getStringExtra("command");
            String action = intent.getAction();
            Logger.e("蓝牙按键 " + action);
				if (cmd.equals("next")) {
					next();
					Logger.e("蓝牙按键：下一曲");
				} else if (cmd.equals("pre")) {
					previous();
					Logger.e("蓝牙按键：上一曲");
				} else if (cmd.equals("play")) {
					play();
					Logger.e("蓝牙按键：播放");
				} else if (cmd.equals("pause")) {
					pause();
					Logger.e("蓝牙按键：暂停");
				} else if (cmd.equals("play-pause")) {
					Logger.e("蓝牙按键：播放/暂停");
					if (isPlaying){
						pause();
					}else{
						play();
					}
				} else if (cmd.equals("fastforward")) {
					Logger.e("蓝牙按键：快进");
				} else if (cmd.equals("rewind")) {
					Logger.e("蓝牙按键：快退");
				}
		}
	};


	/**
	 * 
	 * 发送更新播放进度的广播的子线程，每当播放歌曲时启动
	 * 
	 */
	private class UpdateProgressThread extends Thread {
		@Override
		public void run() {
			while (isThreadWorking) {
				Intent intent = new Intent(SERVICE_UPDATE_PROGRESS);
				intent.putExtra("currentPosition", player.getCurrentPosition());
				sendBroadcast(intent);

				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}


	/**
	 * 播放
	 */
	private void play() {
		try {
			// 播放
			player.reset();
			player.setDataSource(musics.get(app.getCurrentMusicIndex()).getData());
			player.prepare();
			player.seekTo(currentPosition);
			player.start();
			LogUtil.showLog("service开始播放音乐");

			// 清空变量中记录的当前播放进度
			currentPosition = 0;

			// 发送广播：正在播放歌曲
			Intent intent = new Intent();
			intent.setAction(SERVICE_PLAYER_PLAY);
			sendBroadcast(intent);

			// 更新播放状态
			isPlaying = true;

			if (!isStarted) {
				// 由用户开始播放过
				LogUtil.showLog("重新启动进度线程");
				isStarted = true;
				// 启动线程
				isThreadWorking = true;
				thread = new UpdateProgressThread();
				thread.start();
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 暂停
	 */
	private void pause() {
		if (isPlaying) {
			// 暂停
			player.pause();
			// 记录暂停的位置
			currentPosition = player.getCurrentPosition();
			// 更新播放状态
			isPlaying = false;
			// 发送广播，通知Activity歌曲已经暂停
			Intent intent = new Intent();
			intent.setAction(SERVICE_PLAYER_PAUSE);
			sendBroadcast(intent);
		}
	}

	/**
	 * 上一首
	 */
	private void previous() {
		int index;
		if (app.getPlayMode() == PLAY_MODE_RANDOM) {
			Random random = new Random();
			do {
				index = random.nextInt(musics.size());
			} while (index == app.getCurrentMusicIndex());
		} else if(app.getPlayMode() == PLAY_MODE_ORDERED){
			index = app.getCurrentMusicIndex();
			index--;
			if (index < 0) {
				index = musics.size() - 1;
			}
		}else{
			index = app.getCurrentMusicIndex();
		}
		app.setCurrentMusicIndex(index);
		play();
	}

	/**
	 * 下一首
	 */
	private void next() {
		int index;
		if (app.getPlayMode() == PLAY_MODE_RANDOM) {
			Random random = new Random();
			if (musics.size() <= 1){
				index = random.nextInt(musics.size());
			}else{
				do {
					index = random.nextInt(musics.size());
				} while (index == app.getCurrentMusicIndex());
			}
		} else  if(app.getPlayMode() == PLAY_MODE_ORDERED){
			index = app.getCurrentMusicIndex();
			index++;
			if (index >= musics.size()) {
				index = 0;
			}
		}else{
			index = app.getCurrentMusicIndex();
		}
		app.setCurrentMusicIndex(index);
		play();
	}

	@Override
	public void onDestroy() {
		// 停止线程
		isThreadWorking = false;
		// 停止接收广播
		unregisterReceiver(receiver);
		unregisterReceiver(BlueToothReceiver);
		// 释放播放器资源
		player.release();
		player = null;
		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

}
