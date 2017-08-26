package com.zhongzhiyijian.eyan.activity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

import com.actions.ibluz.factory.BluzDeviceFactory;
import com.actions.ibluz.factory.IBluzDevice;
import com.orhanobut.logger.Logger;
import com.zhongzhiyijian.eyan.R;
import com.zhongzhiyijian.eyan.base.BaseActivity;
import com.zhongzhiyijian.eyan.base.Constants;
import com.zhongzhiyijian.eyan.entity.MyDevice;
import com.zhongzhiyijian.eyan.fragment.FragBOB;
import com.zhongzhiyijian.eyan.fragment.FragSearch;
import com.zhongzhiyijian.eyan.fragment.FragUser;
import com.zhongzhiyijian.eyan.util.DataUtil;
import com.zhongzhiyijian.eyan.util.LogUtil;
import com.zhongzhiyijian.eyan.util.ToastUtil;
import com.zhongzhiyijian.eyan.util.XUtil;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class MainActivity extends BaseActivity implements OnCheckedChangeListener,OnClickListener,Constants {


	public static final int OPEN_FRAG_SEARCH = 100;
	public static final int OPEN_FRAG_BOB = 101;
	public static final int OPEN_FRAG_SETTING = 102;
	private static final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 001;
	private RadioGroup mRg;
	private RadioButton rbSearch;
	private RadioButton rbBob;
	private RadioButton rbSetting;
	private FragmentManager fm;
	private FragSearch fragSearch;
	private FragBOB fragBOB;
	private FragUser fragUser;

	private int openType;

	private DbManager dbUtils;

	private static boolean isExit = false;

	private IBluzDevice btConnector;



	private DataUtil dataUtil;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		changeLanAndSize();
		setContentView(R.layout.activity_main);

		btConnector = app.getBluzConnector();

		if(!btConnector.isEnabled()){
			LogUtil.showLog("蓝牙未打开，后台打开蓝牙");
			btConnector.enable();
//			showToast("打开蓝牙后请点击搜索按钮");
		}



		fm = getSupportFragmentManager();




		initViews();
		openType = getIntent().getIntExtra("openType", OPEN_FRAG_BOB);



		openFragmentFrist(openType);

		if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
				!= PackageManager.PERMISSION_GRANTED) {
			//申请WRITE_EXTERNAL_STORAGE权限
			ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
					WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
		}else{
			LogUtil.showLog("读写SD卡已被授权");
			dbUtils = x.getDb(XUtil.getDaoConfig());
		}

		initEvent();


		dataUtil = DataUtil.getInstance();
		dataUtil.FindAndUpLoad();

	}


	@Override
	protected void onResume() {
		super.onResume();
		if (app.isLanChanged){
			Logger.e("语言改变，重绘mainActivity");
			app.isLanChanged = false;
			app.isChanged = true;
			finish();
			Intent it = new Intent(mContext,MainActivity.class);
			startActivity(it);
		}
		if (app.isSizeChanged){
			Logger.e("字体大小改变，重绘mainActivity");
			app.isSizeChanged = false;
			app.isChanged = true;
			finish();
			Intent it = new Intent(mContext,MainActivity.class);
			startActivity(it);
		}
//		changeLanAndSize();
	}

	@Override
	protected void onStart() {
		super.onStart();
		if (app.isChanged){
			app.isChanged = false;
			Intent it = new Intent(mContext,SettingActivity.class);
			startActivity(it);
		}
	}

	private void changeLanAndSize() {
		sp = getSharedPreferences(SP_NAME, MODE_PRIVATE);
		String language = sp.getString("language", "zh");
		changeLanguage(language);
		int size = sp.getInt("txtSize", 0);
		changeSize(size);
	}


	private void initEvent() {
	}



	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		doNext(requestCode,grantResults);
	}

	private void doNext(int requestCode, int[] grantResults) {
		if (requestCode == WRITE_EXTERNAL_STORAGE_REQUEST_CODE) {
			if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				// Permission Granted
				dbUtils = x.getDb(XUtil.getDaoConfig());
			} else {
				// Permission Denied
				ToastUtil.showToast(mContext,"请授权，否则无法正常使用功能");
			}
		}
	}





	public void openFragmentFrist(int type) {
		switch (type) {
			case OPEN_FRAG_SEARCH:
				rbSearch.performClick();
				break;
			case OPEN_FRAG_BOB:
				rbBob.performClick();
				break;
			case OPEN_FRAG_SETTING:
				rbSetting.performClick();
				break;

			default:
				break;
		}
	}

	private void initViews() {
		mRg = (RadioGroup) findViewById(R.id.rg);
		rbSearch = (RadioButton) findViewById(R.id.rb_search);
		rbBob = (RadioButton) findViewById(R.id.rb_bob);
		rbSetting = (RadioButton) findViewById(R.id.rb_setting);
		mRg.setOnCheckedChangeListener(this);
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
			case R.id.rb_search:
				showFragment(0);
				break;
			case R.id.rb_bob:
				showFragment(1);
				break;
			case R.id.rb_setting:
				showFragment(2);
				break;

			default:
				break;
		}
	}

	@Override
	public void onClick(View v) {

	}

	@Override
	protected void onDestroy() {
		if (btConnector != null) {
			btConnector.setOnConnectionListener(null);
			btConnector.setOnDiscoveryListener(null);
//			btConnector.release();
			btConnector = null;
		}
		try {
			List<MyDevice> findAll = dbUtils.findAll(MyDevice.class);
			if (findAll != null && findAll.size() > 0){
				for (MyDevice device : findAll) {
					device.setState(BluzDeviceFactory.ConnectionState.A2DP_DISCONNECTED);
				}
				dbUtils.update(findAll,"state");
			}
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			dbUtils.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		super.onDestroy();
	}

	private void showFragment(int position) {
		FragmentTransaction ft = fm.beginTransaction();
		hideFragment(ft);
		switch (position) {
			case 0:
				if (fragSearch == null) {
					fragSearch = new FragSearch();
					ft.add(R.id.fl_content, fragSearch);
				} else {
					ft.show(fragSearch);
				}
				LogUtil.showLog("search");
				break;
			case 1:
				if (fragBOB == null) {
					fragBOB = new FragBOB();
					ft.add(R.id.fl_content, fragBOB);
				} else {
					ft.show(fragBOB);
				}
				LogUtil.showLog("bob");
				break;
			case 2:
				if (fragUser == null) {
					fragUser = new FragUser();
					ft.add(R.id.fl_content, fragUser);
				} else {
					ft.show(fragUser);
				}
				LogUtil.showLog("user info");
				break;
		}
		ft.commit();
	}

	private void hideFragment(FragmentTransaction ft) {
		if (fragSearch != null) {
			ft.hide(fragSearch);
		}
		if (fragBOB != null) {
			ft.hide(fragBOB);
		}
		if (fragUser != null) {
			ft.hide(fragUser);
		}
	}


	public void changeLanguage(String language) {
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

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			LogUtil.showLog("press back");
			exit();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void exit() {
		if (!isExit) {
			isExit = true;
			Toast.makeText(getApplicationContext(), "再按一次关闭蓝牙并退出程序",
					Toast.LENGTH_SHORT).show();
			// 利用handler延迟发送更改状态信息
			mHandler.sendEmptyMessageDelayed(0, 2000);
		} else {
			btConnector.disable();
			Intent it = new Intent("finish");
			mContext.sendBroadcast(it);
			this.finish();
		}
	}


	private static Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			isExit = false;
		}
	};






}