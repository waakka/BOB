package com.zhongzhiyijian.eyan.base;


import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Window;

import com.igexin.sdk.PushManager;
import com.zhongzhiyijian.eyan.entity.User;
import com.zhongzhiyijian.eyan.entity.UserInfo;
import com.zhongzhiyijian.eyan.service.PushIntentService;
import com.zhongzhiyijian.eyan.service.PushService;
import com.zhongzhiyijian.eyan.user.UserInfoPersist;
import com.zhongzhiyijian.eyan.user.UserPersist;
import com.zhongzhiyijian.eyan.util.LogUtil;
import com.zhongzhiyijian.eyan.util.ToastUtil;

import java.util.Locale;


public class BaseActivity extends AppCompatActivity implements Constants {

	protected Context mContext;
	protected SharedPreferences sp;

	protected BaseApplication app;
	protected BluetoothAdapter btAdapter;

	protected User user;
	protected UserPersist userPersist;
	protected UserInfo info;
	protected UserInfoPersist infoPersist;
	protected ProgressDialog pd;

	protected String clientid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		mContext = this;

		PushManager.getInstance().initialize(this.getApplicationContext(), PushService.class);
		PushManager.getInstance().registerPushIntentService(this.getApplicationContext(), PushIntentService.class);
		clientid = PushManager.getInstance().getClientid(mContext);

		sp = getSharedPreferences(SP_NAME, MODE_PRIVATE);

		pd = new ProgressDialog(mContext);

		btAdapter = BluetoothAdapter.getDefaultAdapter();
		app = (BaseApplication) mContext.getApplicationContext();

		userPersist = UserPersist.getInstance();
		user = userPersist.getUser(mContext);
		infoPersist = UserInfoPersist.getInstance();
		info = infoPersist.getUser(mContext);

		String language = sp.getString("language", "zh");
		changeLanguage(language);
		int size = sp.getInt("txtSize", 0);
		changeSize(size);
	}

	@Override
	protected void onResume() {
		super.onResume();

	}

	protected void intent2Activity(Class<?> cls){
		Intent intent = new Intent(mContext, cls);
		mContext.startActivity(intent);
	}

	protected void showToast(CharSequence text){
		ToastUtil.showToast(mContext, text);
	}

	protected void showLog(String msg){
		LogUtil.showLog(msg);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}


	protected void changeLanguage(String language) {
		Resources resources = getResources();
		Configuration configuration = resources.getConfiguration();
		DisplayMetrics metrics = resources.getDisplayMetrics();
		if (language.equals("en")) {
			configuration.locale = Locale.ENGLISH;
			Log.d("TAG", "切换为英文");
		} else if (language.equals("zh")){
			configuration.locale = Locale.SIMPLIFIED_CHINESE;
			Log.d("TAG", "切换为中文");
		}else if(language.equals("hk")){
			configuration.locale = Locale.TRADITIONAL_CHINESE;
			Log.d("TAG", "切换为繁体");
		}
		resources.updateConfiguration(configuration, metrics);
		SharedPreferences.Editor editor = sp.edit();
		editor.putString("language", language);
		editor.commit();
	}

	public void changeSize(int size) {
		Resources resources = getResources();
		Configuration configuration = resources.getConfiguration();
		DisplayMetrics metrics = resources.getDisplayMetrics();
		switch (size){
			case 1:
				configuration.fontScale = 0.785f;
				break;
			case 2:
				configuration.fontScale = 0.85f;
				break;
			case 3:
				configuration.fontScale = 0.925f;
				break;
			case 4:
				configuration.fontScale = 1.0f;
				break;
			case 5:
				configuration.fontScale = 1.075f;
				break;
			case 6:
				configuration.fontScale = 1.15f;
				break;
			case 7:
				configuration.fontScale = 1.2f;
				break;
		}
		resources.updateConfiguration(configuration, metrics);
		SharedPreferences.Editor editor = sp.edit();
		editor.putInt("txtSize", size);
		editor.commit();
	}

}
