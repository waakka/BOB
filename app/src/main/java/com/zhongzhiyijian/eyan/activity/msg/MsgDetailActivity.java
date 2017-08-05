package com.zhongzhiyijian.eyan.activity.msg;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.zhongzhiyijian.eyan.R;
import com.zhongzhiyijian.eyan.base.BaseActivity;
import com.zhongzhiyijian.eyan.entity.Msg;
import com.zhongzhiyijian.eyan.net.Urls;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MsgDetailActivity extends BaseActivity {

    private ImageButton ibBack;
    private TextView tvTitle;
    private TextView tvTime;
    private TextView tvValue;
    private Msg msg;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg_detail);

        msg = (Msg) getIntent().getSerializableExtra("msg");

        initViews();
        initEvent();
        setMsg();
    }

    private void setMsg() {
        RequestParams params = new RequestParams(Urls.modifyNews);
        params.addBodyParameter("token",user.getToken());
        params.addBodyParameter("sid",user.getSid()+"");
        params.addBodyParameter("news_id",msg.getSid()+"");
        params.addBodyParameter("type","1");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Logger.e(result);
                try {
                    JSONObject object = new JSONObject(result);
                    String returnCode = object.getString("return_code");
                    if (returnCode.equals("1000")){
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Logger.e(ex.getMessage());
            }
            @Override
            public void onCancelled(CancelledException cex) {
            }
            @Override
            public void onFinished() {
            }
        });
    }

    private void initViews() {
        ibBack = (ImageButton) findViewById(R.id.ib_back);
        tvTime = (TextView) findViewById(R.id.tv_msg_detail_time);
        tvTitle = (TextView) findViewById(R.id.tv_msg_detail_title);
        tvValue = (TextView) findViewById(R.id.tv_msg_detail_value);
        String date = sdf.format(new Date(msg.getTime()*1000));
        tvTime.setText(date);
        tvValue.setText(msg.getValue());
        tvTitle.setText(msg.getName());
    }

    private void initEvent() {
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
