package com.zhongzhiyijian.eyan.base;




import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;

import com.zhongzhiyijian.eyan.entity.User;
import com.zhongzhiyijian.eyan.user.UserPersist;
import com.zhongzhiyijian.eyan.util.LogUtil;
import com.zhongzhiyijian.eyan.util.ToastUtil;
import com.zhongzhiyijian.eyan.util.XUtil;

import org.xutils.DbManager;
import org.xutils.x;

import java.util.Locale;

public class BaseFragment extends Fragment implements Constants {
	
	protected Context mContext;
	protected DbManager dbUtils;
	protected BluetoothAdapter btAdapter;
	protected ProgressDialog pd;
	protected BaseApplication app;
	protected SharedPreferences sp;

	protected User user;
	protected UserPersist userPersist;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		mContext =  getActivity();
		btAdapter = BluetoothAdapter.getDefaultAdapter();
		pd = new ProgressDialog(mContext);
		app = (BaseApplication) mContext.getApplicationContext();
		dbUtils = x.getDb(XUtil.getDaoConfig());
		sp = mContext.getSharedPreferences(SP_NAME, mContext.MODE_PRIVATE);

		userPersist = UserPersist.getInstance();
		user = userPersist.getUser(mContext);
	}
	
	protected void intent2Activity(Class<?> cls){
		Intent intent = new Intent(mContext, cls);
		mContext.startActivity(intent);
	}

	@Override
	public void onResume() {
		super.onResume();
		String language = sp.getString("language", "zh");
		changeLanguage(language);
		int size = sp.getInt("txtSize", 0);
		changeSize(size);
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


	protected void showToast(CharSequence text){
		ToastUtil.showToast(mContext, text);
	}
	
	protected void showLog(String msg){
		LogUtil.showLog(msg);
	}
	
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}
}
