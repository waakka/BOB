package com.zhongzhiyijian.eyan.activity.friend;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.zhongzhiyijian.eyan.R;
import com.zhongzhiyijian.eyan.base.BaseActivity;

public class ShareRoleActivity extends BaseActivity {

    private ImageButton ibClose;

    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_role);

        initViews();
        initEvent();

    }


    private void initViews() {
        ibClose = (ImageButton) findViewById(R.id.ib_close);
        tv = (TextView) findViewById(R.id.tv_share_role);
        tv.setText(jiangli);
    }

    private void initEvent() {
        ibClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
