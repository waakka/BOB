package com.zhongzhiyijian.eyan.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.zhongzhiyijian.eyan.R;
import com.zhongzhiyijian.eyan.base.BaseActivity;

public class SetSizeActivity extends BaseActivity{

	private ImageButton ibBack;

	private int size ;
	private int OldSize;

	private ImageView ivSwitch;
	private SeekBar seekBar;
	private TextView tvShow;


	private int i1 = 0;
	private int i2 = 16;
	private int i3 = 33;
	private int i4 = 50;
	private int i5 = 66;
	private int i6 = 83;
	private int i7 = 100;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_size);
		size = sp.getInt("txtSize", 0);
		OldSize = size;

		initView();
		initEvent();

	}

	private void initView() {
		ibBack = (ImageButton) findViewById(R.id.ib_back);
		ivSwitch = (ImageView) findViewById(R.id.iv_switch);
		seekBar = (SeekBar) findViewById(R.id.seekbar);
		tvShow = (TextView) findViewById(R.id.tv_show);
		setType();
		showLog("size = " + size);
		changeShowSize(size);
		if (size == 1){
			seekBar.setProgress(i1);
		}else if(size == 2){
			seekBar.setProgress(i2);
		}else if(size == 3){
			seekBar.setProgress(i3);
		}
		else if(size == 4){
			seekBar.setProgress(i4);
		}
		else if(size == 5){
			seekBar.setProgress(i5);
		}
		else if(size == 6){
			seekBar.setProgress(i6);
		}
		else if(size == 7){
			seekBar.setProgress(i7);
		}
	}

	private void setType() {
		if (app.isSizeLock){
			seekBar.setEnabled(false);
			ivSwitch.setBackgroundResource(R.mipmap.tb_off);
		}else{
			seekBar.setEnabled(true);
			ivSwitch.setBackgroundResource(R.mipmap.tb_on);
		}
	}

	private void initEvent() {
		ibBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				hasChange();
				intent2Activity(MainActivity.class);
				finish();
			}
		});
		ivSwitch.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (app.isSizeLock){
					app.isSizeLock = false;
					ivSwitch.setBackgroundResource(R.mipmap.tb_on);
					setType();
				}else{
					app.isSizeLock = true;
					ivSwitch.setBackgroundResource(R.mipmap.tb_off);
					setType();
				}
			}
		});
		seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				size = ((progress - 1) * 7 / 100) + 1;
				switch (size){
					case 1:
						seekBar.setProgress(i1);
						break;
					case 2:
						seekBar.setProgress(i2);
						break;
					case 3:
						seekBar.setProgress(i3);
						break;
					case 4:
						seekBar.setProgress(i4);
						break;
					case 5:
						seekBar.setProgress(i5);
						break;
					case 6:
						seekBar.setProgress(i6);
						break;
					case 7:
						seekBar.setProgress(i7);
						break;
				}
			}
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				showLog("size = " + size + "  修改字体大小为该size");
				changeShowSize(size);
			}
		});
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	public void changeShowSize(int size) {
		switch (size){
			case 1:
				tvShow.setTextSize(TypedValue.COMPLEX_UNIT_SP,14f);
				break;
			case 2:
				tvShow.setTextSize(TypedValue.COMPLEX_UNIT_SP,16f);
				break;
			case 3:
				tvShow.setTextSize(TypedValue.COMPLEX_UNIT_SP,18f);
				break;
			case 4:
				tvShow.setTextSize(TypedValue.COMPLEX_UNIT_SP,20f);
				break;
			case 5:
				tvShow.setTextSize(TypedValue.COMPLEX_UNIT_SP,22f);
				break;
			case 6:
				tvShow.setTextSize(TypedValue.COMPLEX_UNIT_SP,24f);
				break;
			case 7:
				tvShow.setTextSize(TypedValue.COMPLEX_UNIT_SP,26f);
				break;
		}
		SharedPreferences.Editor editor = sp.edit();
		editor.putInt("txtSize", size);
		editor.commit();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK){
			hasChange();
			Intent intent = new Intent(mContext,MainActivity.class);
			startActivity(intent);
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void hasChange() {
		if (size != OldSize){
			app.isSizeChanged = true;
		}
	}

}
