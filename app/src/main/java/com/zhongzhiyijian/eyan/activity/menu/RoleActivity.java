package com.zhongzhiyijian.eyan.activity.menu;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.zhongzhiyijian.eyan.R;
import com.zhongzhiyijian.eyan.base.BaseActivity;

public class RoleActivity extends BaseActivity{
	
	private LinearLayout back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_role);
		
		initViews();
		initEvent();
	}

	private void initViews() {
		back = (LinearLayout) findViewById(R.id.ll_back);
	}

	private void initEvent() {

		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
}
