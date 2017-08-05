package com.zhongzhiyijian.eyan.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.zhongzhiyijian.eyan.R;
import com.zhongzhiyijian.eyan.base.BaseActivity;
import com.zhongzhiyijian.eyan.net.Urls;
import com.zhongzhiyijian.eyan.util.PwdUtil;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

public class RegistActivity extends BaseActivity {

    private EditText etPhone;
    private EditText etVerificationCode;
    private EditText etInvitationCode;
    private Button btnGet;
    private Button btnNext;
    private TextView tvTips;
    private EditText etPwd;
    private EditText etPwd2;
    private ImageButton ibBack;

    private int codeTime = 60;


    private String phone;
    private String invitationCode = "-1";
    private String verificationCode ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);

        initViews();
        initEvent();
//        btnNext.setClickable(false);
    }

    private void initViews() {
        etPhone = (EditText) findViewById(R.id.et_phone);
        etVerificationCode = (EditText) findViewById(R.id.et_Verification);
        etInvitationCode = (EditText) findViewById(R.id.et_Invitation);
        btnGet = (Button) findViewById(R.id.btn_code);
        btnNext = (Button) findViewById(R.id.btn_next);
        tvTips = (TextView) findViewById(R.id.tv_tips);
        ibBack = (ImageButton) findViewById(R.id.ib_back);
        etPwd = (EditText) findViewById(R.id.et_pwd);
        etPwd2 = (EditText) findViewById(R.id.et_pwd2);
    }

    private void initEvent() {
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        etPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                phone = etPhone.getText().toString();
            }
        });
        btnGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (phone==null||phone.equals("")){
                    showToast("请输入您的电话号码");
                    return;
                }else if(phone.length()!=11){
                    showToast("请输入正确的电话号码");
                    return;
                }
                btnGet.setClickable(false);
                invitationCode = etInvitationCode.getText().toString();
                sendCode();

            }
        });
        tvTips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog;
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("用户注册协议");
                builder.setMessage(xieyi);
                dialog = builder.setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
                dialog.show();
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //下一步
//                Intent intent = new Intent(mContext,PwdActivity.class);
//                intent.putExtra("phone",phone);
//                intent.putExtra("code",code);
//                intent.putExtra("invitation_code",invitationCode);
//                startActivity(intent);
                final String pwd1 = etPwd.getText().toString();
                String pwd2 = etPwd2.getText().toString();
                verificationCode = etVerificationCode.getText().toString();
                invitationCode = etInvitationCode.getText().toString();
                if(phone==null||phone.equals("")){
                    showToast("请输入手机号码");
                }else if(verificationCode==null||verificationCode.equals("")){
                    showToast("请输入验证码");
                }else if (pwd1==null||pwd1.equals("")){
                    showToast("请输入密码");
                }else  if(pwd2==null||pwd2.equals("")){
                    showToast("请输入密码");
                }else if(!PwdUtil.isTruePwd(pwd1)){
                    showToast("密码必须是6-16位字母和数组组合的密码");
                }else  if(!pwd2.equals(pwd1)){
                    showToast("密码输入不一致");
                }else if (pwd1!=null&&pwd1.equals(pwd2)) {
                    pd.setTitle("注册");
                    pd.setMessage("提交信息...");
                    pd.show();
                    RequestParams params = new RequestParams(Urls.login);
                    params.addParameter("mobile",phone);
                    params.addParameter("password",pwd1);
                    params.addParameter("type","3");
                    params.addParameter("code",verificationCode);
                    params.addParameter("invitation_code",invitationCode);
                    Logger.e("start regist mobile=" + phone + " password=" + pwd1 + " code=" + verificationCode);
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
                                Logger.e(e.getMessage());
                                showToast("注册失败！");
                            }
                        }
                        @Override
                        public void onError(Throwable ex, boolean isOnCallback) {
                            Logger.e("result===>" + ex.getMessage());
                            showToast("注册失败！");
                        }
                        @Override
                        public void onCancelled(CancelledException cex) {
                        }
                        @Override
                        public void onFinished() {
                            if (pd.isShowing()){
                                pd.dismiss();
                            }
                        }
                    });
                }
            }
        });

    }

    /**
     * 发送短信验证码
     */
    private void sendCode(){
        RequestParams params = new RequestParams(Urls.login);
        params.addBodyParameter("mobile",phone);
        params.addBodyParameter("password","-1");
        params.addBodyParameter("type","2");
        params.addBodyParameter("code","-1");
        params.addBodyParameter("invitation_code",invitationCode);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Logger.e("result===>" + result);
                try {
                    JSONObject object = new JSONObject(result);
                    String returnCode = object.getString("return_code");
                    if (returnCode.equals(SUCCESS)){
//                        btnNext.setClickable(true);
                        showToast("短信已发送成功！");
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
                    }else if(returnCode.equals(ALREADYSHOW)){
                        showToast("该账号已注册，请直接登录");
                        Intent intent = new Intent(mContext,LoginActivity.class);
                        intent.putExtra("phone",phone);
                        startActivity(intent);
                        finish();
                    }else{
                        showToast("验证码发送失败，请重新发送");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    showToast("验证码发送失败，请重新发送");
                }
            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Logger.e("result===>" + ex.getMessage());
                showToast("验证码发送失败，请重新发送");
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
                btnGet.setText("重新获取");
            }else{
//                codeTime--;
                btnGet.setText(codeTime + "秒后重新获取");
            }
        }
    };


}
