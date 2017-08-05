package com.zhongzhiyijian.eyan.activity.Forget;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.zhongzhiyijian.eyan.R;
import com.zhongzhiyijian.eyan.activity.LoginActivity;
import com.zhongzhiyijian.eyan.base.BaseActivity;
import com.zhongzhiyijian.eyan.net.Urls;
import com.zhongzhiyijian.eyan.util.PwdUtil;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

public class ForgetActivity extends BaseActivity {

    public static final int TYPE_PHONE = 1;
    public static final int TYPE_EMAIL = 2;

    private EditText etPhone;
    private EditText etVerificationCode;
    private Button btnGet;
    private Button btnNext;
    private ImageButton ibBack;
    private TextView tvTypeTitle;
    private TextView tvTypeChange;


    private int codeTime = 60;


    private String phone;
    private String email;
    private String code;

    private int type;

    private EditText etPwd1;
    private EditText etPwd2;
    private String pwd1;
    private String pwd2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);
        type = TYPE_PHONE;

        initViews();
        initEvent();
    }

    private void initViews() {
        etPhone = (EditText) findViewById(R.id.et_phone);
        tvTypeTitle = (TextView) findViewById(R.id.tv_type_1);
        tvTypeChange = (TextView) findViewById(R.id.tv_type_2);
        etVerificationCode = (EditText) findViewById(R.id.et_Verification);
        etPwd1 = (EditText) findViewById(R.id.et_pwd);
        etPwd2 = (EditText) findViewById(R.id.et_pwd2);
        btnGet = (Button) findViewById(R.id.btn_code);
        btnNext = (Button) findViewById(R.id.btn_next);
        ibBack = (ImageButton) findViewById(R.id.ib_back);
        setType();
    }

    private void setType() {
        if (type == TYPE_PHONE){
            tvTypeTitle.setText("手机短信验证");
            tvTypeChange.setText("邮箱验证");
            etPhone.setHint(R.string.Please_enter_your_mobile_number);
            etVerificationCode.setHint(R.string.Please_enter_the_verification_code_in_your_message);
        }else if(type == TYPE_EMAIL){
            tvTypeTitle.setText("邮箱验证");
            tvTypeChange.setText("手机短信验证");
            etPhone.setHint("请输入您账号绑定的邮箱");
            etVerificationCode.setHint("请输入您收到的邮箱验证码");
        }
    }

    private void initEvent() {
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type == TYPE_EMAIL){
                    email = etPhone.getText().toString();
                    if (email==null||email.equals("")){
                        showToast("请输入您的电子邮件地址");
                        return;
                    }else if(!email.endsWith(".com")||!email.contains("@")){
                        showToast("请输入正确的电子邮件地址");
                        return;
                    }
                    btnGet.setClickable(false);
                    tvTypeChange.setClickable(false);
                    sendPhoneCode();
                    new Thread(){
                        @Override
                        public void run() {
                            btnGet.setClickable(false);
                            codeTime = 60;
                            while (codeTime >= 0){
                                Message msg = new Message();
                                myHandler.sendMessage(msg);
                                try {
                                    sleep(1000);
                                    codeTime--;
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }.start();
                }else if(type == TYPE_PHONE){
                    phone = etPhone.getText().toString();
                    if (phone==null||phone.equals("")){
                        showToast("请输入您的电话号码");
                        return;
                    }
                    if(phone.length()!=11){
                        showToast("请输入正确的电话号码");
                        return;
                    }
                    btnGet.setClickable(false);
                    tvTypeChange.setClickable(false);
                    sendPhoneCode();
                    new Thread(){
                        @Override
                        public void run() {
                            btnGet.setClickable(false);
                            codeTime = 60;
                            while (codeTime >= 0){
                                Message msg = new Message();
                                myHandler.sendMessage(msg);
                                try {
                                    sleep(1000);
                                    codeTime--;
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }.start();
                }


            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //下一步
                code = etVerificationCode.getText().toString();
                pwd1 = etPwd1.getText().toString();
                pwd2 = etPwd2.getText().toString();
                if(phone==null||phone.equals("")){
                    showToast("请输入手机号码");
                }else if(code==null||code.equals("")){
                    showToast("请输入验证码");
                }else if (pwd1==null||pwd1.equals("")){
                    showToast("请输入密码");
                }else  if(pwd2==null||pwd2.equals("")){
                    showToast("请输入密码");
                }else if(!PwdUtil.isTruePwd(pwd1)){
                    showToast("密码必须是6-16位字母和数组组合的密码");
                }else  if(!pwd2.equals(pwd1)){
                    showToast("密码输入不一致");
                }else if (pwd1!=null&&pwd1.equals(pwd2)){
                    RequestParams params = new RequestParams(Urls.login);
                    params.addBodyParameter("mobile",phone);
                    params.addBodyParameter("password",pwd1);
                    params.addBodyParameter("type","5");
                    params.addBodyParameter("code",code);
                    params.addBodyParameter("invitation_code","-1");
                    x.http().get(params, new Callback.CommonCallback<String>() {
                        @Override
                        public void onSuccess(String result) {
                            Logger.e("result===>" + result);
                            try {
                                JSONObject object = new JSONObject(result);
                                String returnCode = object.getString("return_code");
                                if (returnCode.equals(SUCCESS)){
                                    showToast("密码修改成功！");
                                    Intent intent = new Intent(mContext,LoginActivity.class);
                                    intent.putExtra("phone",phone);
                                    intent.putExtra("pwd",pwd1);
                                    startActivity(intent);
                                    finish();
                                }else if(returnCode.equals(CODEERROR)){
                                    showToast("您的验证码有误，请重新获取！");
                                    etVerificationCode.setText("");
                                    etPwd1.setText("");
                                    etPwd2.setText("");
                                }else if(returnCode.equals(NOACCESS)){
                                    showToast("账号不存在！");
                                    etVerificationCode.setText("");
                                    etPwd1.setText("");
                                    etPwd2.setText("");
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

        tvTypeChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type == TYPE_PHONE){
                    type = TYPE_EMAIL;
                }else{
                    type = TYPE_PHONE;
                }
                setType();
            }
        });

    }

    private void sendPhoneCode(){
        RequestParams params = new RequestParams(Urls.login);
        params.addBodyParameter("mobile",phone);
        params.addBodyParameter("password","-1");
        params.addBodyParameter("type","4");
        params.addBodyParameter("code","-1");
        params.addBodyParameter("invitation_code","-1");
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Logger.e("result===>" + result);
                try {
                    JSONObject object = new JSONObject(result);
                    String returnCode = object.getString("return_code");
                    if (returnCode.equals(SUCCESS)){
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
    private void sendMailCode(){
        RequestParams params = new RequestParams(Urls.emailGetCode);
        params.addBodyParameter("mobile",phone);
        params.addBodyParameter("password","-1");
        params.addBodyParameter("type","4");
        params.addBodyParameter("code","-1");
        params.addBodyParameter("invitation_code","-1");
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Logger.e("result===>" + result);
                try {
                    JSONObject object = new JSONObject(result);
                    String returnCode = object.getString("return_code");
                    if (returnCode.equals(SUCCESS)){
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

    private Handler myHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (codeTime <= 0){
                btnGet.setClickable(true);
                tvTypeChange.setClickable(true);
                btnGet.setText("重新获取");
            }else{
//                codeTime--;
                btnGet.setText(codeTime + "秒后重新获取");
            }
        }
    };


}
