package com.zhongzhiyijian.eyan.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.zhongzhiyijian.eyan.R;
import com.zhongzhiyijian.eyan.base.BaseActivity;
import com.zhongzhiyijian.eyan.net.Urls;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

public class FeedActivity extends BaseActivity {

    private ImageButton ibClose;

    private EditText etFeed;
    private TextView btnCommit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        initViews();
        initEvent();

    }


    private void initViews() {
        ibClose = (ImageButton) findViewById(R.id.ib_close);
        etFeed = (EditText) findViewById(R.id.et_feed);
        btnCommit = (TextView) findViewById(R.id.tv_send);
    }

    private void initEvent() {
        ibClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String feed = etFeed.getText().toString();
                if (feed != null && !feed.equals("")){
                    RequestParams params = new RequestParams(Urls.addFeedBack);
                    params.addParameter("token",user.getToken());
                    params.addParameter("sid",user.getSid());
                    params.addQueryStringParameter("value",feed);
                    x.http().post(params, new Callback.CommonCallback<String>() {
                        @Override
                        public void onSuccess(String result) {
                            Logger.e(result);
                            try {
                                JSONObject object = new JSONObject(result);
                                String returnCode = object.getString("return_code");
                                if (returnCode.equals(SUCCESS)){
                                    showToast("感谢您的意见或建议，我们会更努力的为您服务！");
                                    etFeed.setText("");
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
                }else{
                    showToast("评论内容不能为空");
                }
            }
        });
    }
}
