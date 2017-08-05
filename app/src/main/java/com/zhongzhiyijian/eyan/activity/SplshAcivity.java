package com.zhongzhiyijian.eyan.activity;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;

import com.orhanobut.logger.Logger;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.zhongzhiyijian.eyan.R;
import com.zhongzhiyijian.eyan.base.BaseActivity;
import com.zhongzhiyijian.eyan.entity.MyDevice;
import com.zhongzhiyijian.eyan.util.XUtil;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SplshAcivity extends BaseActivity{

	private static final int WANT_TO_DEVICE_LIST = 1;
	private static final int WANT_TO_SEARCH = 2;
	private static final long SPLSH_DUR_TIME = 2000;
	private Handler splshHandler;

	private IWXAPI api;

	private AlarmManager alarm;

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd,HH:mm");

	@SuppressLint("HandlerLeak")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_splsh);
		creatFiles();
		alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		setTime();
		
		splshHandler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				Intent i = new Intent();
				switch (msg.what) {
				case WANT_TO_SEARCH:
					i.setClass(mContext, MainActivity.class);
					i.putExtra("openType", MainActivity.OPEN_FRAG_SEARCH);
					break;
				case WANT_TO_DEVICE_LIST:
					i.setClass(mContext, MainActivity.class);
					i.putExtra("openType", MainActivity.OPEN_FRAG_BOB);
					break;
				default:
					break;
				}
				startActivity(i);
				finish();
			}
		};
		queryDevice();


		api = WXAPIFactory.createWXAPI(mContext,APP_ID,true);
		api.registerApp(APP_ID);
	}

	private void setTime() {
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		if (month == 11 && day < 31){
			calendar.set(Calendar.MONTH,12);
			calendar.set(Calendar.DAY_OF_MONTH,31);
			calendar.set(Calendar.HOUR_OF_DAY,12);
			calendar.set(Calendar.MONTH,0);

			Intent intentUnIn = new Intent("JIE_ZHI_RI_QI");
			intentUnIn.putExtra("title","您"+year+"年的积分将于"+year+"年"+month+"月"+day+"日过期，快去兑换优惠券吧");
			PendingIntent intent = PendingIntent.getBroadcast(mContext, 998, intentUnIn, PendingIntent.FLAG_UPDATE_CURRENT);
			this.alarm.cancel(intent);
			this.alarm.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),intent);
		}
//		int min = calendar.get(Calendar.MINUTE);
//		Logger.e("目前时间" + sdf.format(new Date(calendar.getTimeInMillis())));
//		min += 1;
//		calendar.set(Calendar.MINUTE,min);
//		Logger.e("推送时间" + sdf.format(new Date(calendar.getTimeInMillis())));
//		Intent intentUnIn = new Intent("JIE_ZHI_RI_QI");
//		intentUnIn.putExtra("title","您"+year+"年的积分将于"+year+"年"+month+"月"+day+"日过期，快去兑换优惠券吧");
//		PendingIntent intent = PendingIntent.getBroadcast(mContext, 0, intentUnIn, PendingIntent.FLAG_UPDATE_CURRENT);
//		this.alarm.cancel(intent);
//		this.alarm.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),intent);
	}

	private void creatFiles() {
		File file = new File(localPath);
		if (!file.exists()){
			file.mkdirs();
		}
	}

	private void queryDevice() {
		DbManager db = x.getDb(XUtil.getDaoConfig());
		try {
			List<MyDevice> all = db.findAll(MyDevice.class);
			if (all != null && all.size() > 0){
				showLog("有历史连接设备");
				splshHandler.sendEmptyMessageDelayed(WANT_TO_DEVICE_LIST,SPLSH_DUR_TIME);
			}else{
				showLog("无历史连接设备");
				splshHandler.sendEmptyMessageDelayed(WANT_TO_SEARCH,SPLSH_DUR_TIME);
			}
		} catch (DbException e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
}
