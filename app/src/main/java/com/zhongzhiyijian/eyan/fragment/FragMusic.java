package com.zhongzhiyijian.eyan.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.actions.ibluz.factory.IBluzDevice;
import com.actions.ibluz.manager.BluzManager;
import com.actions.ibluz.manager.BluzManagerData;
import com.zhongzhiyijian.eyan.R;
import com.zhongzhiyijian.eyan.activity.DeviceDetailActivity;
import com.zhongzhiyijian.eyan.activity.MusicDetailActivity;
import com.zhongzhiyijian.eyan.adapter.MusicAdapter;
import com.zhongzhiyijian.eyan.base.BaseFragment;
import com.zhongzhiyijian.eyan.base.Constants;
import com.zhongzhiyijian.eyan.entity.Music;
import com.zhongzhiyijian.eyan.service.PlayMusicService;
import com.zhongzhiyijian.eyan.util.TextFormatter;

import java.util.ArrayList;
import java.util.List;

public class FragMusic extends BaseFragment implements Constants {

	private View view;

	private List<Music> musics;
	private MusicAdapter mAdapter;

	private LinearLayout layoutMusic;
	private ImageView ivMusicImg;
	private TextView tvMusicName;
	private TextView tvMusiclrc;
	private ImageButton ibPlay;
	private ImageButton ibVolume;
	private Music curMusic;

	private ListView lvMusic;


	/**
	 * 广播接收者
	 */
	private BroadcastReceiver receiver;
	/**
	 * 广播接收者的意图过滤器
	 */
	private IntentFilter filter;


	private AlertDialog seekDialog;
	private AlertDialog.Builder builder;
	private SeekBar seekBar;


	private IBluzDevice iBluzDevice;
	private BluzManager bluzManager;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.frag_music, container,false);

		initViews();
		setList();
		initEvent();

		// 隐式启动Service
		Intent intent = new Intent(mContext,PlayMusicService.class);
		mContext.startService(intent);
		// 初始化广播接收者
		initReceiver();

		setMusicBar();

		DeviceDetailActivity mActivity = (DeviceDetailActivity)getActivity();
		bluzManager = mActivity.getBluzManager();

		initManager();

		return view;
	}

	private void initManager() {

		bluzManager.setOnGlobalUIChangedListener(new BluzManagerData.OnGlobalUIChangedListener() {
			@Override
			public void onEQChanged(int i) {
//						showToast("EQ改变" + i);
			}

			@Override
			public void onBatteryChanged(int battery, boolean incharge) {
				//电量改变
				showLog("电池电量" + battery);
				app.devicePower = battery;
				app.incharge = incharge;
			}

			@Override
			public void onVolumeChanged(int i, boolean b) {
				//音量改变
				showLog("当前音量改变为" + i);
				seekBar.setProgress(i);
			}

			@Override
			public void onModeChanged(int i) {
//						showToast("模式改变" + i);
			}
		});
	}


	private void setList() {
		if(app.getMusicList() == null){
			musics = new ArrayList<>();
			showToast("本地暂无歌曲，请点击右上角按钮添加");
		}else{
			musics = app.getMusicList();
		}
		showLog("fragMusic 获取到"+musics.size()+"首歌曲");
		mAdapter = new MusicAdapter(mContext, musics);
		lvMusic.setAdapter(mAdapter);
	}

	private void initViews() {
		lvMusic = (ListView) view.findViewById(R.id.lv_musics);
		layoutMusic = (LinearLayout) view.findViewById(R.id.ll_music);
		ivMusicImg = (ImageView) view.findViewById(R.id.iv_music_albumImage);
		tvMusicName = (TextView) view.findViewById(R.id.tv_music_name);
		tvMusiclrc = (TextView) view.findViewById(R.id.tv_music_lrc);
		ibPlay = (ImageButton) view.findViewById(R.id.ib_play);
		ibVolume = (ImageButton) view.findViewById(R.id.ib_volume);

		builder = new AlertDialog.Builder(mContext);
		View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_seekbar,null);
		seekBar = (SeekBar) view.findViewById(R.id.seekbar_volume);
		builder.setView(view);
		builder.setTitle("设置音量");
		seekDialog = builder.create();
	}


	private void initEvent() {
		lvMusic.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				//跳转到音乐播放详细列表
				if(musics != null && musics.size() > 0){
					if(app.getCurrentMusicIndex() != arg2){
						app.setCurrentMusicIndex(arg2);
						mContext.sendBroadcast(new Intent(ACTIVITY_MUSIC_INDEX_CHANGED));
					}else{
						mContext.sendBroadcast(new Intent(ACTIVITY_CURRENT_MUSIC_CLICK));
					}
					intent2Activity(MusicDetailActivity.class);
				}
			}
		});

		lvMusic.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
				showLog("item long pressed");
				AlertDialog dialog = new AlertDialog.Builder(mContext)
						.setTitle("删除").setMessage("将歌曲移除列表？")
						.setPositiveButton("确定", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								showLog("delete");
								musics.remove(position);
								mAdapter.notifyDataSetChanged();
								app.setMusicList((ArrayList<Music>) musics);
								if (position == app.getCurrentMusicIndex()){
									mContext.sendBroadcast(new Intent(ACTIVITY_CURRENT_MUSIC_DELETE));
									app.setCurrentMusicIndex(0);
									setMusicBar();
								}
							}
						})
						.setNegativeButton("取消",null).create();
				dialog.show();
				return true;
			}
		});


		layoutMusic.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if(musics != null && musics.size() > 0){
					intent2Activity(MusicDetailActivity.class);
				}
			}
		});
		ibPlay.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(musics != null && musics.size() > 0){
					showLog("musicbar play click");
					showLog("播放/暂停");
					mContext.sendBroadcast(new Intent(ACTIVITY_PLAY_BUTTON_CLICK));
				}else{
					showToast("当前列表无歌曲，请先添加歌曲");
				}
			}
		});
		ibVolume.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				seekDialog.show();
			}
		});
		seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				showToast(progress+"");
				bluzManager.setVolume(progress);
			}
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				seekDialog.dismiss();
			}
		});
	}

	/**
	 * 初始化广播接收者
	 */
	private void initReceiver() {
		receiver = new InnerReceiver();
		filter = new IntentFilter();
		filter.addAction(SERVICE_PLAYER_PLAY);
		filter.addAction(SERVICE_PLAYER_PAUSE);
		filter.addAction(SERVICE_UPDATE_PROGRESS);
		// 注册广播接收者
		mContext.registerReceiver(receiver, filter);
	}

	/**
	 * 广播接收者
	 */
	private class InnerReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (SERVICE_PLAYER_PLAY.equals(action)) {
				// 当播放歌曲时
				ibPlay.setBackgroundResource(android.R.drawable.ic_media_pause); // 切换按钮图片
				setMusicBar();
			} else if (SERVICE_PLAYER_PAUSE.equals(action)) {
				// 当暂停播放时
				ibPlay.setBackgroundResource(android.R.drawable.ic_media_play);
			} else if (SERVICE_UPDATE_PROGRESS.equals(action)) {
				// 实时更新播放进度
				int currentPosition = intent.getIntExtra("currentPosition", 0);
				String timeStr = TextFormatter.getMusicTime(currentPosition) + " / "
						+ TextFormatter.getMusicTime(musics.get(app.getCurrentMusicIndex()).getDuration());
				int progress = (int) (currentPosition * 100 / musics.get(app.getCurrentMusicIndex()).getDuration());
				progress = progress > 100 ? 0 : progress;
				//				tvDuration.setText(timeStr);
				//				sbProgress.setProgress(progress);
			}
		}
	}


	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		if (!hidden){
			onResume();
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		setList();
	}

	@Override
	public void onDestroy() {
		// 停止接收广播
		mContext.unregisterReceiver(receiver);
		super.onDestroy();

	}




	private void setMusicBar() {
		if(musics.size() > 0){
			curMusic = musics.get(app.getCurrentMusicIndex());
			//更新界面
			String path = curMusic.getImage();
			Bitmap bitmap = null;
			if (path != null && !path.equals("")) {
				bitmap = BitmapFactory.decodeFile(path);
			} else {
				bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_launcher);
			}

			ivMusicImg.setImageBitmap(bitmap);
			tvMusicName.setText(curMusic.getTitle());
			tvMusiclrc.setText("");
		}else{
			ivMusicImg.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_launcher));
			tvMusicName.setText("歌曲名称");
			tvMusiclrc.setText("当前歌曲名称");
		}
	}

}
