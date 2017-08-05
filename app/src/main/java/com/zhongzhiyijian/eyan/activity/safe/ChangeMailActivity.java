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
import com.zhongzhiyijian.eyan.net.Urls;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

public class ChangeMailActivity extends BaseActivity {

    private ImageButton ibBack;

    private EditText etMail;
    private EditText etCode;
    private Button btnCode;
    private Button btnSet;

    private TextView tvTitle;
    private LinearLayout ll1;


    private int codeTime = 120;

    private static int TYPE1 = 1;
    private static int TYPE2 = 2;

    private int type;

    private String mail1;
    private String mail2;
    private String code1;
    private String code2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_mail);

        initViews();
        initEvent();
        type = TYPE1;
        setLayout();
        etMail.setText(user.getEmail()+"");
        etCode.requestFocus();
    }

    private void setLayout() {
        if (type == TYPE1){
            ll1.setVisibility(View.VISIBLE);
            tvTitle.setTextColor(getResources().getColor(R.color.black));
            btnSet.setText(getString(R.string.Verification));
        }else{
            ll1.setVisibility(View.GONE);
            tvTitle.setTextColor(getResources().getColor(R.color.green));
            btnSet.setText(getString(R.string.binding));
            btnCode.setText("获取验证码");
        }
    }

    private void initViews() {
        ibBack = (ImageButton) findViewById(R.id.ib_back);
        etMail = (EditText) findViewById(R.id.et_phone);
        etCode = (EditText) findViewById(R.id.et_code);
        btnCode = (Button) findViewById(R.id.btn_code);
        btnSet = (Button) findViewById(R.id.btn_set);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        ll1 = (LinearLayout) findViewById(R.id.ll_type1);
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

                if (type == TYPE1){
                    mail1 = etMail.getText().toString();
                    if (mail1 ==null|| mail1.equals("")){
                        showToast("请输入您的电子邮箱地址");
                        return;
                    }else if(!mail1.contains("@")){
                        showToast("请输入正确的电子邮件地址");
                        return;
                    }
                    sendCode1();
                }else if (type == TYPE2){
                    mail2 = etMail.getText().toString();
                    if (mail2 ==null|| mail2.equals("")){
                        showToast("请输入您的电子邮箱地址");
                        return;
                    }else if(!mail2.contains("@")){
                        showToast("请输入正确的电子邮件地址");
                        return;
                    }
                    sendCode2();
                }
            }
        });
        btnSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type == TYPE1){
                    codeTime = 0;
                    code1 = etCode.getText().toString();
                    if (code1==null||code1.equals("")){
                        showToast("请输入验证码");
                        return;
                    }
                   checkCode1();
                }else{
                    code2 = etCode.getText().toString();
                    if (code2==null||code2.equals("")){
                        showToast("请输入验证码");
                        return;
                    }
                    checkCode2();
                }
            }
        });
    }
    /**
     * 验证旧邮箱验证码
     */
    private void checkCode1() {
        RequestParams params = new RequestParams(Urls.modifymailbox4);
        params.addParameter("mailbox", mail1);
        params.addParameter("token",user.getToken());
        params.addParameter("sid",user.getSid());
        params.addParameter("type",6);
        params.addParameter("code",code1);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Logger.e("旧邮箱验证码成功" + result);
                try {
                    JSONObject object = new JSONObject(result);
                    String returnCode = object.getString("return_code");
                    if (returnCode.equals(SUCCESS)){
                        codeTime = -1;
                        showToast("验证成功");
                        etCode.setText("");
                        etMail.setText("");
                        btnCode.setClickable(true);
                        type = TYPE2;
                        setLayout();
                        etMail.requestFocus();
                    }else if(returnCode.equals(CODEERROR)){
                        showToast("验证码错误，请重新输入");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Logger.e("旧手机验证码失败" + ex.getLocalizedMessage());
                showToast("验证码错误，请重新获取并输入");
            }
            @Override
            public void onCancelled(CancelledException cex) {
            }
            @Override
            public void onFinished() {
            }
        });
    }
    /**
     * 验证新邮箱验证码
     */
    private void checkCode2() {
        RequestParams params = new RequestParams(Urls.modifymailbox6);
        params.addParameter("mailbox", mail2);
        params.addParameter("token",user.getToken());
        params.addParameter("sid",user.getSid());
        params.addParameter("type",5);
        params.addParameter("code",code2);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Logger.e(result);
                try {
                    JSONObject object = new JSONObject(result);
                    String returnCode = object.getString("return_code");
                    if (returnCode.equals(SUCCESS)){
                        showToast("修改成功");
                        user.setEmail(mail2);
                        userPersist.setUser(mContext,user);
                        finish();
                    }else if(returnCode.equals(CODEERROR)){
                        showToast("验证码错误，请重新输入");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Logger.e("新手机验证码失败" + ex.getLocalizedMessage());
                showToast("验证码错误，请重新获取并输入");
            }
            @Override
            public void onCancelled(CancelledException cex) {
            }
            @Override
            public void onFinished() {
            }
        });
    }

    /**
     * 获取旧邮箱验证码
     */
    private void sendCode1(){
        RequestParams params = new RequestParams(Urls.modifymailbox3);
        params.addParameter("mailbox", mail1);
        params.addParameter("token",user.getToken());
        params.addParameter("sid",user.getSid());
        params.addParameter("type",3);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Logger.e(result);
                try {
                    JSONObject object = new JSONObject(result);
                    String returnCode = object.getString("return_code");
                    if (returnCode.equals(SUCCESS)){
                        showToast("验证码已发送，请查收");
                        new Thread(){
                            @Override
                            public void run() {
                                btnCode.setClickable(false);
                                while (codeTime > 0){
                                    try {
                                        if(codeTime >= 0){
                                            Message msg = new Message();
                                            msg.what = 1;
                                            myHandler.sendMessage(msg);
                                        }
                                        sleep(1000);
                                        codeTime--;
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                                if(codeTime == 0){
                                    Message msg = new Message();
                                    msg.what = 2;
                                    myHandler.sendMessage(msg);
                                }
                            }
                        }.start();
                    }else if(returnCode.equals(CODE_SEND_ERROR)){
                        showToast("验证码发送失败请检查邮箱");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }
    /**
     * 获取新邮箱验证码
     */
    private void sendCode2(){
        RequestParams params = new RequestParams(Urls.modifymailbox5);
        params.addParameter("mailbox", mail1);
        params.addParameter("lsmailbox", mail2);
        params.addParameter("token",user.getToken());
        params.addParameter("sid",user.getSid());
        params.addParameter("type",4);
        params.addParameter("code",code1);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Logger.e(result);
                try {
                    JSONObject object = new JSONObject(result);
                    String returnCode = object.getString("return_code");
                    if (returnCode.equals(SUCCESS)){
                        showToast("验证码已发送，请查收");
                        codeTime = 120;
                        new Thread(){
                            @Override
                            public void run() {
                                btnCode.setClickable(false);
                                while (codeTime > 0){
                                    try {
                                        if(codeTime >= 0){
                                            Message msg = new Message();
                                            msg.what = 1;
                                            myHandler.sendMessage(msg);
                                        }
                                        sleep(1000);
                                        codeTime--;
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                                if(codeTime == 0){
                                    Message msg = new Message();
                                    msg.what = 2;
                                    myHandler.sendMessage(msg);
                                }
                            }
                        }.start();
                    }else if(returnCode.equals(CODE_SEND_ERROR)){
                        showToast("验证码发送失败请检查邮箱");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

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
