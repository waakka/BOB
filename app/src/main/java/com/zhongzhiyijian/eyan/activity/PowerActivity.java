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

import com.orhanobut.logger.Logger;
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

    private double dianyaBl;
    private double dianyaAm;
    private double dianliangBl;
    private double dianliangAm;


    double BLmaxE = 260;
    double BLminE = 260 * 0.08;
    double BLmaxV = 4.20;
    double BLminV = 3.68;

    double AMmaxE = 260;
    double AMminE = 260 * 0.08;
    double AMmaxV = 4.28;
    double AMminV = 3.40;


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
		filter.addAction(XINTIAO_DISCONNECTED);
		filter.addAction(BATTERY_CHANGED_BL);
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
                Logger.e("电压查询返回值==>" + printHexString(result));
                //按摩板数据
                String stra = Integer.toHexString(result[8] & 0xFF);
                stra = Integer.valueOf(stra,16).toString();
                int inta = Integer.parseInt(stra);
                dianyaBl = getDianya(inta) + 0.23;
                //蓝牙数据
                String strb = Integer.toHexString(result[9] & 0xFF);
                strb = Integer.valueOf(strb,16).toString();
                int intb = Integer.parseInt(strb);
                dianyaAm = getDianya(intb);
                dianliangAm = getDianLiangAm(dianyaAm);

                Logger.e("解析后数据   a=" + getDianya(inta) + " b=" + intb);



                if(dianyaAm > 4.3){
                    //充电状态
                    startCharge(ivBl);
                    startCharge(ivAm);
                    tvDianyaBl.setText(BLmaxV + " V");
                    tvDianliangBl.setText("100%");
                    tvDianliangAm.setText("100%");
                }else{
                    //未充电状态
                    if(dianyaBl - dianyaAm >= 0.3){
                        //蓝牙数据为真实数据
                        dianliangBl = getDianLiangBl(dianyaBl);

                        tvDianyaBl.setText(String.format("%.2f", dianyaBl) + " V");
                        tvDianliangBl.setText(String.format("%.2f", getLevel(dianliangBl)) + "%");
                    }else{
                        //蓝牙数据不准确，以jar返回为准
                        int battery = app.devicePower;
                        dianyaBl = 3.68+battery*((4.20-3.68)/5);
                        dianliangBl =  (BLmaxE - BLminE)/(BLmaxV - BLminV) * (dianyaBl - BLminV);

                        tvDianyaBl.setText(String.format("%.2f", dianyaBl) + " V");
                        tvDianliangBl.setText(String.format("%.2f", getLevelStr(dianliangBl)) + "%");
                    }
                    showLevel(ivAm,getLevel(dianliangAm));
                    showLevel(ivBl,getLevel(dianliangBl));
                    tvDianliangAm.setText(String.format("%.2f", getLevelStr(dianliangAm)) + "%");
                }
                tvDianyaAm.setText(String.format("%.2f", dianyaAm) + " V");
			}else if(XINTIAO_DISCONNECTED.equals(action)){
                showToast("按摩板已断开连接");
                finish();
            }else if(BATTERY_CHANGED_BL.equals(action)){
                quaryBattery();
            }
		}
	}


	private int getLevel(double d){
        double v = (d + AMminE) / AMmaxE;
        int level = (int) (v*10);
        Logger.e("double=" + d + " d=" + v + " level=" + level);
        return level;
    }
	private double getLevelStr(double d){
        double v = (d + AMminE) / AMmaxE;
        return v*100;
    }

	private double getDianLiangAm(double nowV){
        return (AMmaxE - AMminE)/(AMmaxV - AMminV) * (nowV - AMminV) + AMminE ;
    }
	private double getDianLiangBl(double nowV){
        return (BLmaxE - BLminE)/(BLmaxV - BLminV) * (nowV - BLminV) + BLminE ;
    }

	private double getDianya(int j){
        double i = j;
        return (i/255.0)*1.8*2.0*(8.4/5.1);
    }

    public String printHexString(byte[] b) {
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

	/**
	 * 发送查询电池情况广播
	 */
	private void quaryBattery(){
        if(app.workStatus != WORK_STATUS_DIANJI_ON_GAOYA_ON){
            Intent intent = new Intent();
            intent.setAction(SEND_MSG_TO_DEVICE);
            intent.putExtra("byte", MsgUtil.getPower());
            sendBroadcast(intent);
        }else{
            showToast("工作状态中，无法查询电池信息");
        }
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
