package com.zhongzhiyijian.eyan.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.zhongzhiyijian.eyan.R;
import com.zhongzhiyijian.eyan.activity.safe.ChangePhoneActivity;
import com.zhongzhiyijian.eyan.activity.safe.ChangePwdActivity;
import com.zhongzhiyijian.eyan.activity.safe.SafeEmailActivity;
import com.zhongzhiyijian.eyan.base.BaseActivity;

import org.xutils.view.annotation.Event;
import org.xutils.x;

public class SafeActivity extends BaseActivity {

    private ImageButton ibBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safe);
        x.view().inject(this);

        initViews();
        initEvent();
    }

    private void initViews() {
        ibBack = (ImageButton) findViewById(R.id.ib_safe_back);
    }

    private void initEvent() {
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Event(value = R.id.rl_change_phone)
    private void changePhone(View view){
        intent2Activity(ChangePhoneActivity.class);
    }
    @Event(value = R.id.rl_change_pwd)
    private void changePwd(View view){
        intent2Activity(ChangePwdActivity.class);
    }
    @Event(value = R.id.rl_email)
    private void email(View view){
        intent2Activity(SafeEmailActivity.class);
    }

}
