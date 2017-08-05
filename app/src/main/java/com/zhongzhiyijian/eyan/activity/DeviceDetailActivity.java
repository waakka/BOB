package com.zhongzhiyijian.eyan.activity;


import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.actions.ibluz.factory.IBluzDevice;
import com.actions.ibluz.manager.BluzManager;
import com.actions.ibluz.manager.BluzManagerData;
import com.orhanobut.logger.Logger;
import com.zhongzhiyijian.eyan.R;
import com.zhongzhiyijian.eyan.base.BaseActivity;
import com.zhongzhiyijian.eyan.base.Constants;
import com.zhongzhiyijian.eyan.fragment.FragMusic;
import com.zhongzhiyijian.eyan.fragment.FragPt;
import com.zhongzhiyijian.eyan.service.PlayMusicService;
import com.zhongzhiyijian.eyan.util.BluetoothBoxControl;
import com.zhongzhiyijian.eyan.util.ToastUtil;

public class DeviceDetailActivity extends BaseActivity implements OnClickListener,Constants {


	private static final int SHOW_FRAG_PT = 101;
	private static final int SHOW_FRAG_MUSIC = 102;

	private FragmentManager fm;
	private FragPt fragPt;
	private FragMusic fragMusic;

	private LinearLayout back;
	private TextView tvName;

	private Button btnPt;
	private Button btnMusic;

	private ImageButton ibMusicList;



//	private BluetoothDevice device;

	protected SharedPreferences sp;

	private BluzManager bluzManager;

	private IBluzDevice btConnector;

	private int keySend;
	private int keyAnswer;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.activity_device_detail);

		app.isDeviceConnection = true;
		fm = getSupportFragmentManager();

		startService(new Intent(mContext, PlayMusicService.class));

		initViews();
		initEvent();

		showFragment(SHOW_FRAG_PT);

//		device = getIntent().getParcelableExtra("device");


		btConnector = app.getBluzConnector();
		if (btConnector != null){
            BluetoothDevice connectedDevice = btConnector.getConnectedDevice();
            if(connectedDevice == null){
                ToastUtil.showToast(mContext,"当前设备断开连接！");
                finish();
            }
			btConnector.setOnConnectionListener(new IBluzDevice.OnConnectionListener() {
				@Override
				public void onConnected(BluetoothDevice bluetoothDevice) {

				}

				@Override
				public void onDisconnected(BluetoothDevice bluetoothDevice) {
					app.isDeviceConnection = false;
					ToastUtil.showToast(mContext,"当前设备断开连接！");
					finish();
				}
			});
			initManager();
		}

		IntentFilter filter = new IntentFilter();

		filter.addAction("finish");

		registerReceiver(mFinishReceiver, filter);

		tvName.setText(getString(R.string.device)+btConnector.getConnectedDevice().getName());

	}

	public BluzManager getBluzManager(){
		return bluzManager;
	};

	private void initManager() {
		//用户自定义命令; 以查询类命令为例, id和参数可根据实际情况配置A
		keySend = BluzManager.buildKey(BluzManagerData.CommandType.QUE, 0x81);
		keyAnswer = BluzManager.buildKey(BluzManagerData.CommandType.ANS, 0x81);

		btConnector = app.getBluzConnector();
		bluzManager = new BluzManager(mContext, btConnector, new BluzManagerData.OnManagerReadyListener() {
			@Override
			public void onReady() {

//				bluzManager.setVolume(curBluzVolume);

				bluzManager.setOnCustomCommandListener(new BluzManagerData.OnCustomCommandListener() {
					@Override
					public void onReady(int what, int arg1, int arg2, byte[] arg3) {
						if (what == keyAnswer){
							String result = printHexString(arg3);

							showToast("收到蓝牙反馈>>> what = "+ what +" arg1 = " + arg1 + " arg2 = " + arg2 + " arg3 = " + result);
							Logger.e("收到蓝牙反馈>>> what = "+ what +" arg1 = " + arg1 + " arg2 = " + arg2 + " arg3 = " + result);
							//收到电量后存储到app并通知设置界面更改数据
						}
					}
				});
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
					}

					@Override
					public void onModeChanged(int i) {
//						showToast("模式改变" + i);
					}
				});
			}
		});

	}

	public String printHexString( byte[] b) {
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



	@Override
	protected void onResume() {
		super.onResume();
		btnPt.setText(getString(R.string.unit_pt));
		btnMusic.setText(getString(R.string.unit_music));
		tvName.setText(getString(R.string.device)+btConnector.getConnectedDevice().getName());
	}

	@Override
	protected void onDestroy() {
		unregisterReceiver(mFinishReceiver);
		stopService(new Intent(mContext, PlayMusicService.class));
		if (bluzManager != null){
			bluzManager.setOnCustomCommandListener(null);
			bluzManager.setOnGlobalUIChangedListener(null);
			bluzManager.release();
			bluzManager = null;
		}
		super.onDestroy();
	}

	private BroadcastReceiver mFinishReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if("finish".equals(intent.getAction())) {
				Log.e("#########", "I am " + getLocalClassName()
						+ ",now finishing myself...");
				finish();
			}
		}
	};



	private void initViews() {
		back = (LinearLayout) findViewById(R.id.ll_back);
		tvName = (TextView) findViewById(R.id.tv_name);
		btnPt = (Button) findViewById(R.id.btn_pt);
		btnMusic = (Button) findViewById(R.id.btn_music);
		ibMusicList = (ImageButton) findViewById(R.id.ib_music_type);
	}

	private void showFragment(int position){
		FragmentTransaction ft = fm.beginTransaction();
		resetBgColors();
		hideFragment(ft);
		switch (position) {
			case SHOW_FRAG_PT:
				if(fragPt == null){
					fragPt = new FragPt();
					ft.add(R.id.fl_content, fragPt);
				}else{
					ft.show(fragPt);
				}
				btnPt.setBackgroundResource(R.drawable.topbar_shape);
				btnPt.setTextColor(0xff3ad168);
				break;
			case SHOW_FRAG_MUSIC:
				if(fragMusic == null){
					fragMusic = new FragMusic();
					ft.add(R.id.fl_content, fragMusic);
				}else{
					ft.show(fragMusic);
				}
				btnMusic.setBackgroundResource(R.drawable.topbar_shape);
				btnMusic.setTextColor(0xff3ad168);
				ibMusicList.setVisibility(View.VISIBLE);
				break;

			default:
				break;
		}
		ft.commit();
	}

	private void hideFragment(FragmentTransaction ft) {
		if(fragPt != null){
			ft.hide(fragPt);
		}
		if(fragMusic != null){
			ft.hide(fragMusic);
		}
	}

	private void resetBgColors(){
		btnPt.setBackgroundResource(R.drawable.topbar_shape_green);
		btnPt.setTextColor(0xffffffff);
		btnMusic.setBackgroundResource(R.drawable.topbar_shape_green);
		btnMusic.setTextColor(0xffffffff);
		ibMusicList.setVisibility(View.GONE);
	}

	private void initEvent() {
		back.setOnClickListener(this);
		btnPt.setOnClickListener(this);
		btnMusic.setOnClickListener(this);
		ibMusicList.setOnClickListener(this);

	}



	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK){
			Intent intent = new Intent(mContext,MainActivity.class);
			startActivity(intent);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.ll_back:
				Intent intent = new Intent(mContext,MainActivity.class);
				startActivity(intent);
				break;
			case R.id.btn_pt:
				showFragment(SHOW_FRAG_PT);
				break;
			case R.id.btn_music:
				showFragment(SHOW_FRAG_MUSIC);
				break;
			case R.id.ib_music_type:
				Intent it = new Intent(mContext,AddMusicActivity.class);
				mContext.startActivity(it);
				break;

			default:
				break;
		}

	}


	@Override
	public void onBackPressed() {
		Intent intent = new Intent(mContext,MainActivity.class);
		startActivity(intent);
	}


}
