package com.zhongzhiyijian.eyan.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhongzhiyijian.eyan.R;
import com.zhongzhiyijian.eyan.base.BaseActivity;
import com.zhongzhiyijian.eyan.util.MsgUtil;

public class PowerActivity extends BaseActivity{
	
	private LinearLayout back;

	private ImageView ivBl,ivAm;
	private TextView tvDianyaBl,tvDianyaAm,tvDianliangBl,tvDianliangAm;
	private Button btnRefrash;

	private boolean isCharging;

	private byte[] result;

	private InnerReceiver receiver;
	private IntentFilter filter;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_power);

		isCharging = false;
		
		initViews();
		initEvent();

		initReceiver();
	}

	private void initReceiver() {
		receiver = new InnerReceiver();
		filter = new IntentFilter();
		filter.addAction(BATTERY_CHANGED);
		registerReceiver(receiver,filter);
	}

	@Override
	protected void onResume() {
		super.onResume();
		quaryBattery();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(receiver);
	}

	private class InnerReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if(BATTERY_CHANGED.endsWith(action)){
				result = intent.getByteArrayExtra("result");
				//TODO 分析计算处理数据，并展示界面
			}
		}
	}

	/**
	 * 发送查询电池情况广播
	 */
	private void quaryBattery(){
		Intent intent = new Intent();
		intent.setAction(SEND_MSG_TO_DEVICE);
		intent.putExtra("byte", MsgUtil.getPower());
		sendBroadcast(intent);
	}

	private void initViews() {
		back = (LinearLayout) findViewById(R.id.ll_back);

		ivBl = (ImageView) findViewById(R.id.iv_power_bl);
		ivAm = (ImageView) findViewById(R.id.iv_power_am);

		tvDianyaBl = (TextView) findViewById(R.id.tv_dianya_bl);
		tvDianyaAm = (TextView) findViewById(R.id.tv_dianya_am);
		tvDianliangBl = (TextView) findViewById(R.id.tv_dianliang_bl);
		tvDianliangAm = (TextView) findViewById(R.id.tv_dianliang_am);

		btnRefrash = (Button) findViewById(R.id.btn_refrash);
	}

	private void initEvent() {
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		btnRefrash.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//				stopAm(ivBl);
//				stopAm(ivAm);
//				ivBl.setImageResource(R.mipmap.battery_state_08);
//				ivAm.setImageResource(R.mipmap.battery_state_03);
				if(isCharging){
					isCharging = !isCharging;
					startCharge(ivBl);
					startCharge(ivAm);
				}else{
					isCharging = !isCharging;
					showLevel(ivBl,3);
					showLevel(ivAm,8);
				}
				quaryBattery();
			}
		});
	}

	private void startCharge(ImageView iv){
		iv.setImageResource(R.drawable.charge_battery);
		AnimationDrawable am = (AnimationDrawable) iv.getDrawable();
		if(null != am){
			am.start();
		}
	}

	private void showLevel(ImageView iv,int lv){
		switch (lv){
			case 0:
				iv.setImageResource(R.mipmap.battery_state_00);
				break;
			case 1:
				iv.setImageResource(R.mipmap.battery_state_01);
				break;
			case 2:
				iv.setImageResource(R.mipmap.battery_state_02);
				break;
			case 3:
				iv.setImageResource(R.mipmap.battery_state_03);
				break;
			case 4:
				iv.setImageResource(R.mipmap.battery_state_04);
				break;
			case 5:
				iv.setImageResource(R.mipmap.battery_state_05);
				break;
			case 6:
				iv.setImageResource(R.mipmap.battery_state_06);
				break;
			case 7:
				iv.setImageResource(R.mipmap.battery_state_07);
				break;
			case 8:
				iv.setImageResource(R.mipmap.battery_state_08);
				break;
			case 9:
				iv.setImageResource(R.mipmap.battery_state_09);
				break;
			case 10:
				iv.setImageResource(R.mipmap.battery_state_10);
				break;
		}
	}

}
