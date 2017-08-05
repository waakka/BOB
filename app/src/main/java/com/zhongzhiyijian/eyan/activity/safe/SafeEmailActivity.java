package com.zhongzhiyijian.eyan.activity.safe;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.zhongzhiyijian.eyan.R;
import com.zhongzhiyijian.eyan.base.BaseActivity;
import com.zhongzhiyijian.eyan.entity.User;
import com.zhongzhiyijian.eyan.net.Urls;
import com.zhongzhiyijian.eyan.user.UserPersist;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

public class SafeEmailActivity extends BaseActivity {

    private ImageButton ibBack;

    private TextView tvTitle;
    private EditText etEmail;
    private LinearLayout llCode;
    private EditText etCode;
    private Button btnCode;
    private Button btnSet;
    private int codeTime = 120;

    private UserPersist userPersist;
    private User user;
    private String email;
    private String code;


    private int type;
    private static final int TYPE_NO_MAIL = 1;
    private static final int TYPE_HAS_MAIL = 2;

    private boolean codeRun = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_safe);
        userPersist = UserPersist.getInstance();
        user = userPersist.getUser(mContext);
        initViews();
        initEvent();
    }

    @Override
    protected void onResume() {
        super.onResume();
        user = userPersist.getUser(mContext);
        email = user.getEmail();
        if (email==null||email.equals("")||email.equals("-1")){
            type = TYPE_NO_MAIL;
        }else{
            type = TYPE_HAS_MAIL;
        }
        setLayout();
    }

    private void setLayout() {
        switch (type){
            case TYPE_NO_MAIL:
                tvTitle.setText(getString(R.string.mailwarmming));
                btnCode.setText(getString(R.string.verification_code));
                llCode.setVisibility(View.VISIBLE);
                btnSet.setVisibility(View.VISIBLE);
                break;
            case TYPE_HAS_MAIL:
                etEmail.setText(user.getEmail());
                etEmail.setFocusable(false);
                tvTitle.setText("温馨提示：设置密保邮箱能便于您多一个找回密码的途径");
                btnCode.setText("更换密保邮箱");
                llCode.setVisibility(View.GONE);
                btnSet.setVisibility(View.GONE);
                break;
        }
    }

    private void initViews() {
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        ibBack = (ImageButton) findViewById(R.id.ib_back);
        etEmail = (EditText) findViewById(R.id.et_email);
        llCode = (LinearLayout) findViewById(R.id.ll_code);
        etCode = (EditText) findViewById(R.id.et_code);
        btnCode = (Button) findViewById(R.id.btn_code);
        btnSet = (Button) findViewById(R.id.btn_set);
    }

    private void initEvent() {
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type == TYPE_NO_MAIL){
                    email = etEmail.getText().toString();
                    if (email==null||email.equals("")){
                        showToast("请输入您的电子邮件地址");
                        return;
                    }else if(!email.contains("@")){
                        showToast("请输入正确的电子邮件地址");
                        return;
                    }
                    btnCode.setClickable(false);
                    sendCode(email);
                    new Thread(){
                        @Override
                        public void run() {
                            while (codeTime > 0 && codeRun){
                                try {
                                    sleep(1000);
                                    codeTime--;
                                    Message msg = new Message();
                                    msg.what = 1;
                                    myHandler.sendMessage(msg);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                            Message msg = new Message();
                            msg.what = 2;
                            myHandler.sendMessage(msg);
                        }
                    }.start();
                }else if(type == TYPE_HAS_MAIL){
                    intent2Activity(ChangeMailActivity.class);
                }
            }
        });
        btnSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                code = etCode.getText().toString();
                if (email==null||email.equals("")){
                    showToast("请输入您的电子邮件地址");
                    return;
                }
                if (code==null||code.equals("")){
                    showToast("请输入您邮箱中的验证码");
                    return;
                }
                checkCode();
            }
        });
    }

    private void checkCode() {
        RequestParams params = new RequestParams(Urls.modifymailbox2);
        params.addParameter("mailbox",email);
        params.addParameter("token",user.getToken());
        params.addParameter("sid",user.getSid());
        params.addParameter("type",2);
        params.addParameter("code",code);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Logger.e("绑定成功！" + result);
                try {
                    JSONObject object = new JSONObject(result);
                    String returnCode = object.getString("return_code");
                    if (returnCode.equals(SUCCESS)){
                        codeRun = false;
                        user.setEmail(email);
                        userPersist.setUser(mContext,user);
                        codeTime = 0;
                        showToast("绑定成功！");
                        type = TYPE_HAS_MAIL;
                        setLayout();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Logger.e("绑定失败，请输入正确的验证码" + ex.getLocalizedMessage());
                showToast("绑定失败，请输入正确的验证码");
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void sendCode(String e){
        RequestParams params = new RequestParams(Urls.modifymailbox1);
        params.addParameter("mailbox",e);
        params.addParameter("token",user.getToken());
        params.addParameter("sid",user.getSid());
        params.addParameter("type",1);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Logger.e(result);
                try {
                    JSONObject object = new JSONObject(result);
                    String returnCode = object.getString("return_code");
                    if (returnCode.equals(SUCCESS)){
                        showToast("已发送验证码到邮箱，请查收");
                    }else if(returnCode.equals(ALREADEMAIL)){
                        showToast("该邮箱已被绑定，请更换");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Logger.e("发送验证码失败，请输入正确的邮箱地址" + ex.getLocalizedMessage());
                showToast("发送验证码失败，请输入正确的邮箱地址");
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }


    private Handler myHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            int what = msg.what;
            if (what == 1){
                btnCode.setText(codeTime + "秒后重新获取");
            }else{
                btnCode.setClickable(true);
                btnCode.setText("重新获取");
                codeTime = 120;
            }
        }
    };

}
