package com.zhongzhiyijian.eyan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.orhanobut.logger.Logger;
import com.zhongzhiyijian.eyan.R;
import com.zhongzhiyijian.eyan.base.BaseActivity;
import com.zhongzhiyijian.eyan.net.Urls;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

public class PwdActivity extends BaseActivity {

    private ImageButton ibBack;

    private EditText etPwd;
    private EditText etPwd2;
    private Button btnRegist;

    private String phone;
    private String invitationCode ;
    private String code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pwd);

        Intent it = getIntent();
        phone = it.getStringExtra("phone");
        code = it.getStringExtra("code");
        invitationCode = it.getStringExtra("invitationCode");

        initViews();
        initEvent();
    }

    private void initEvent() {
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnRegist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String pwd1 = etPwd.getText().toString();
                String pwd2 = etPwd2.getText().toString();
                if (pwd1!=null&&pwd1.equals(pwd2)) {
                    RequestParams params = new RequestParams(Urls.login);
                    params.addBodyParameter("mobile",phone);
                    params.addBodyParameter("password",pwd1);
                    params.addBodyParameter("type","3");
                    params.addBodyParameter("code",code);
                    params.addBodyParameter("invitation_code",invitationCode);
                    Logger.e("start regist mobile=" + phone + " password=" + pwd1 + " code=" + code);
                    x.http().get(params, new Callback.CommonCallback<String>() {
                        @Override
                        public void onSuccess(String result) {
                            Logger.e("result===>" + result);
                            try {
                                JSONObject object = new JSONObject(result);
                                String returnCode = object.getString("return_code");
                                if (returnCode.equals(SUCCESS)){
                                    showToast("注册成功！");
                                    Intent intent = new Intent(mContext,LoginActivity.class);
                                    intent.putExtra("phone",phone);
                                    intent.putExtra("pwd",pwd1);
                                    startActivity(intent);
                                    finish();
                                }else if(returnCode.equals(CODEERROR)){
                                    showToast("您的验证码有误，请重新获取！");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        @Override
                        public void onError(Throwable ex, boolean isOnCallback) {
                            Logger.e("result===>" + ex.getMessage());
                        }
                        @Override
                        public void onCancelled(CancelledException cex) {
                        }
                        @Override
                        public void onFinished() {
                        }
                    });
                }
            }
        });
    }

    private void initViews() {
        ibBack = (ImageButton) findViewById(R.id.ib_back);
        etPwd = (EditText) findViewById(R.id.et_pwd);
        etPwd2 = (EditText) findViewById(R.id.et_pwd2);
        btnRegist = (Button) findViewById(R.id.btn_regist);
    }
}
