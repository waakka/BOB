package com.zhongzhiyijian.eyan.activity;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhongzhiyijian.eyan.R;
import com.zhongzhiyijian.eyan.base.BaseActivity;
import com.zhongzhiyijian.eyan.entity.SettingStatus;
import com.zhongzhiyijian.eyan.entity.SettingStatusUtil;
import com.zhongzhiyijian.eyan.entity.User;

import java.util.Random;

import cn.qqtheme.framework.picker.OptionPicker;

public class SettingActivity extends BaseActivity implements OnClickListener{

	private ImageButton ibBack;

	private SettingStatus status;
	private SettingStatusUtil statusUtil;

	private SettingReceiver mReceiver;

	private ImageButton ibBTStatus;
	private ProgressBar pbBtStatus;
	private ImageButton ibUSBDeviceStatus;
	private ImageButton ibBTDeviceStatus;
	private TextView tvLanguage;
	private TextView tvVersionCode;
	private TextView tvClear;

	private RelativeLayout rlLanguage;
	private RelativeLayout rlTxtSize;
	private RelativeLayout rlVersionCode;
	private RelativeLayout rlClear;
	private TextView tvLog;
	private TextView tvUnLog;


	private String lan ;
	private int size ;
	private boolean lanHasChanged = false;
	private boolean sizeHasChanged = false;
	private String [] languages = new String[]{"繁体中文","简体中文","English"};

	private boolean isLogin;

	private BluetoothAdapter btAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.frag_setting);
		statusUtil = SettingStatusUtil.getInstance();
		status = statusUtil.getSettingStatus(mContext);

		btAdapter = BluetoothAdapter.getDefaultAdapter();

		if (user.getToken()!=null&&!user.getToken().equals("")){
			isLogin = true;
		}else{
			isLogin = false;
		}

		initView();
		initEvent();
		initReceiver();
	}

	private void initView() {
		ibBack = (ImageButton) findViewById(R.id.ib_back);
		ibBTStatus = (ImageButton) findViewById(R.id.ib_bt_status);
		ibUSBDeviceStatus = (ImageButton) findViewById(R.id.ib_device_status);
		ibBTDeviceStatus = (ImageButton) findViewById(R.id.ib_btdevice_status);
		pbBtStatus = (ProgressBar) findViewById(R.id.pb_changing_bt_status);
		tvLanguage = (TextView) findViewById(R.id.tv_language);
		tvVersionCode = (TextView) findViewById(R.id.tv_version_code);
		tvClear = (TextView) findViewById(R.id.tv_clear);
		tvLog = (TextView) findViewById(R.id.tv_logo_out);
		tvUnLog = (TextView) findViewById(R.id.tv_unlogin);

		rlLanguage = (RelativeLayout) findViewById(R.id.rl_language);
		rlTxtSize = (RelativeLayout) findViewById(R.id.rl_txt_size);
		rlVersionCode = (RelativeLayout) findViewById(R.id.rl_update);
		rlClear = (RelativeLayout) findViewById(R.id.rl_clear);

		lan = sp.getString("language", "zh");
		size = sp.getInt("txtSize", 0);
		showLog("当前语音为：" + lan + "  字体大小为：" + size);
		setLayout();



	}
	private void setLayout() {
		if(btAdapter.isEnabled()){
			ibBTStatus.setImageResource(R.mipmap.tb_on);
		}else{
			ibBTStatus.setImageResource(R.mipmap.tb_off);
		}

		if(status.isUSBAutoConnect()){
			ibUSBDeviceStatus.setImageResource(R.mipmap.tb_on);
		}else{
			ibUSBDeviceStatus.setImageResource(R.mipmap.tb_off);
		}
		if(status.isAutoConnect()){
			ibBTDeviceStatus.setImageResource(R.mipmap.tb_on);
		}else{
			ibBTDeviceStatus.setImageResource(R.mipmap.tb_off);
		}
		String lansss = status.getLanguage();
		if (lansss.equals("zh")){
			tvLanguage.setText(R.string.Simplified_Chinese);
		}
		if (lansss.equals("hk")){
			tvLanguage.setText(R.string.Traditional_Chinese);
		}
		if (lansss.equals("en")){
			tvLanguage.setText(R.string.English);
		}
		tvVersionCode.setText(status.getVersionCode());
		if (isLogin){
			tvLog.setText(R.string.log_out);
			tvUnLog.setVisibility(View.GONE);
		}else{
			tvLog.setText(R.string.log_in);
			tvUnLog.setVisibility(View.VISIBLE);
		}

		tvClear.setText("0");
	}

	private void initEvent() {
		ibBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		ibBTStatus.setOnClickListener(this);
		ibUSBDeviceStatus.setOnClickListener(this);
		ibBTDeviceStatus.setOnClickListener(this);
		rlLanguage.setOnClickListener(this);
		rlTxtSize.setOnClickListener(this);
		rlVersionCode.setOnClickListener(this);
		tvLog.setOnClickListener(this);
		rlClear.setOnClickListener(this);
	}

	private void initReceiver() {
		mReceiver = new SettingReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(STARTED_BT);
		filter.addAction(STOPED_BT);
		mContext.registerReceiver(mReceiver, filter);
	}

	class SettingReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if(STARTED_BT.equals(action)){
				pbBtStatus.setVisibility(View.GONE);
				ibBTStatus.setVisibility(View.VISIBLE);
				ibBTStatus.setImageResource(R.mipmap.tb_on);
			}
			if(STOPED_BT.equals(action)){
				pbBtStatus.setVisibility(View.GONE);
				ibBTStatus.setVisibility(View.VISIBLE);
				ibBTStatus.setImageResource(R.mipmap.tb_off);
			}
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.ib_bt_status:
				if(btAdapter.isEnabled()){
					btAdapter.disable();
					ibBTStatus.setImageResource(R.mipmap.tb_off);
				}else{
					btAdapter.enable();
					pbBtStatus.setVisibility(View.VISIBLE);
					ibBTStatus.setVisibility(View.GONE);
					new Thread(){
						@Override
						public void run() {
							while(!btAdapter.isEnabled()){
								try {
									sleep(1000);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}
							Message msg = new Message();
							btHandler.sendMessage(msg);
						}
					}.start();
				}
				break;

			case R.id.ib_device_status:
				if(status.isUSBAutoConnect()){
					status.setUSBAutoConnect(false);
					ibUSBDeviceStatus.setImageResource(R.mipmap.tb_off);
					showToast("当前USB连接状态为：手动");
				}else{
					status.setUSBAutoConnect(true);
					ibUSBDeviceStatus.setImageResource(R.mipmap.tb_on);
					showToast("当前USB连接状态为：自动");
				}
				statusUtil.setSettingStatus(mContext, status);
				break;
			case R.id.ib_btdevice_status:
				if(status.isAutoConnect()){
					status.setAutoConnect(false);
					ibBTDeviceStatus.setImageResource(R.mipmap.tb_off);
					showToast("当前蓝牙设备连接状态为：手动");
				}else{
					status.setAutoConnect(true);
					ibBTDeviceStatus.setImageResource(R.mipmap.tb_on);
					showToast("当前蓝牙设备连接状态为：自动");
				}
				statusUtil.setSettingStatus(mContext, status);
				break;
			case R.id.rl_language:
				showPicker(languages);
				break;
			case R.id.rl_txt_size:
//				showPicker(textSizes);
				intent2Activity(SetSizeActivity.class);
				finish();
				break;
			case R.id.rl_update:
				pd.setTitle("版本升级");
				pd.setMessage("正在检测中，请稍后");
				pd.show();
				new Thread(){
					@Override
					public void run() {
						try {
							sleep(3000);
							Message msg = new Message();
							updateHandler.sendMessage(msg);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}.start();
				break;
			case R.id.tv_logo_out:
				if (isLogin){
					user = new User();
					user.setClientid("");
					user.setEmail("");
					user.setPhone("");
					user.setSid(0);
					user.setToken("");
					userPersist.setUser(mContext,user);
					finish();
				}else{
					intent2Activity(LoginActivity.class);
					finish();
				}
				break;
			case R.id.rl_clear:
				if(tvClear.getText().equals("0")){
					showToast("当前无缓存");
					return;
				}
				pd.setTitle("清除缓存");
				pd.setMessage("正在清理中，请稍后");
				pd.show();
				new Thread(){
					@Override
					public void run() {
						try {
							sleep(3000);
							Message msg = new Message();
							clearHandler.sendMessage(msg);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}.start();
				break;
			default:
				break;
		}
	}


	private Handler btHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			ibBTStatus.setVisibility(View.VISIBLE);
			pbBtStatus.setVisibility(View.GONE);
			if(btAdapter.isEnabled()){
				ibBTStatus.setImageResource(R.mipmap.tb_on);
			}else{
				ibBTStatus.setImageResource(R.mipmap.tb_off);
			}
		}
	};

	private Handler clearHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			if (pd.isShowing()){
				pd.dismiss();
				tvClear.setText("0");
			}
			showToast("清理完成！");
		}
	};
	private Handler updateHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			if (pd.isShowing()){
				pd.dismiss();
			}
			showToast("当前已经是最新版本！");
		}
	};

	private void showPicker(String[] strs) {
		OptionPicker picker = new OptionPicker(SettingActivity.this, strs);
		picker.setOffset(2);
		picker.setSelectedIndex(1);
		picker.setTextSize(16);
		picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
			@Override
			public void onOptionPicked(String option) {
				showToast(option);
				if ("繁体中文".equals(option)){
					if(!lan.equals("hk")){
						lan = "hk";
						lanHasChanged = true;
					}
				}else if ("简体中文".equals(option)){
					if(!lan.equals("zh")){
						lan = "zh";
						lanHasChanged = true;
					}
				}else if ("English".equals(option)){
					if(!lan.equals("en")){
						lan = "en";
						lanHasChanged = true;
					}
				}else if ("小".equals(option)){
					if(size != -1){
						size = -1;
						sizeHasChanged = true;
					}
				}else if ("中".equals(option)){
					if(size != 0){
						size = 0;
						sizeHasChanged = true;
					}
				}else if ("大".equals(option)){
					if(size != 1){
						size = 1;
						sizeHasChanged = true;
					}
				}

				if(lanHasChanged){
					showLog("语言改变为：" + lan);
//					mainActivity.changeLanguage1(lan);
					SharedPreferences.Editor edit = sp.edit();
					edit.putString("language", lan);
					edit.commit();
					app.isLanChanged = true;
					intent2Activity(MainActivity.class);
					finish();
				}else{
					showLog("语言未改变");
				}

				if(sizeHasChanged){
					//TODO 改变字体
					showLog("字体大小改变为：" + size);
//					mainActivity.changeSize1(size);
					SharedPreferences.Editor edit = sp.edit();
					edit.putInt("txtSize", size);
					edit.commit();
					intent2Activity(MainActivity.class);
					finish();
				}else{
					showLog("字体大小未改变");
				}

			}
		});
		picker.show();
	}


	@Override
	public void onDestroy() {
		super.onDestroy();
		mContext.unregisterReceiver(mReceiver);
	}

}
