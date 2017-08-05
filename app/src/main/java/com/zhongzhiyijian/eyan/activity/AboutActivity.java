package com.zhongzhiyijian.eyan.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.zhongzhiyijian.eyan.R;
import com.zhongzhiyijian.eyan.base.BaseActivity;

public class AboutActivity extends BaseActivity{
	
	private LinearLayout back;

	private RelativeLayout help;
	private RelativeLayout feed;
	private RelativeLayout pinglun;
	private RelativeLayout conn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		
		initViews();
		initEvent();
	}

	private void initViews() {
		back = (LinearLayout) findViewById(R.id.ll_back);
		help = (RelativeLayout) findViewById(R.id.rl_help);
		feed = (RelativeLayout) findViewById(R.id.rl_feed);
		pinglun = (RelativeLayout) findViewById(R.id.rl_pinglun);
		conn = (RelativeLayout) findViewById(R.id.rl_conn);
	}

	private void initEvent() {
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		feed.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				intent2Activity(FeedActivity.class);
			}
		});
	}
}
