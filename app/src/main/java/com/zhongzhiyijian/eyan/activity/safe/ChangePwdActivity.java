package com.zhongzhiyijian.eyan.activity.safe;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.orhanobut.logger.Logger;
import com.zhongzhiyijian.eyan.R;
import com.zhongzhiyijian.eyan.activity.LoginActivity;
import com.zhongzhiyijian.eyan.base.BaseActivity;
import com.zhongzhiyijian.eyan.entity.User;
import com.zhongzhiyijian.eyan.net.Urls;
import com.zhongzhiyijian.eyan.util.PwdUtil;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

public class ChangePwdActivity extends BaseActivity {

    private ImageButton ibBack;

    private EditText etOld;
    private EditText etNew;
    private EditText etNew2;
    private Button btnCommit;

    private String strOld;
    private String strNew;
    private String strNew2;
    private String phone;

    private ImageButton ibClear1;
    private ImageButton ibClear2;
    private ImageButton ibClear3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pwd);
        phone = user.getPhone();

        initViews();
        initEvent();
    }

    private void initViews() {
        ibBack = (ImageButton) findViewById(R.id.ib_back);
        etOld = (EditText) findViewById(R.id.et_pwd_old);
        etNew = (EditText) findViewById(R.id.et_pwd_new);
        etNew2 = (EditText) findViewById(R.id.et_pwd_new2);
        btnCommit = (Button) findViewById(R.id.btn_change_commit);
        ibClear1 = (ImageButton) findViewById(R.id.ib_clear1);
        ibClear2 = (ImageButton) findViewById(R.id.ib_clear2);
        ibClear3 = (ImageButton) findViewById(R.id.ib_clear3);
    }

    private void initEvent() {
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePwd();
            }
        });
        etOld.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    ibClear1.setVisibility(View.VISIBLE);
                }else{
                    ibClear1.setVisibility(View.GONE);
                }
            }
        });
        etNew.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    ibClear2.setVisibility(View.VISIBLE);
                }else{
                    ibClear2.setVisibility(View.GONE);
                }
            }
        });
        etNew2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    ibClear3.setVisibility(View.VISIBLE);
                }else{
                    ibClear3.setVisibility(View.GONE);
                }
            }
        });
        ibClear1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etOld.setText(null);
            }
        });
        ibClear2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etNew.setText(null);
            }
        });
        ibClear3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etNew2.setText(null);
            }
        });
    }

    private void changePwd() {
        strOld = etOld.getText().toString();
        strNew = etNew.getText().toString();
        strNew2 = etNew2.getText().toString();
        if (strOld == null || strOld.length() <= 0){
            showToast("请输入密码");
            return;
        }
        if (strNew == null || strNew.length() <= 0){
            showToast("请输入密码");
            return;
        }
        if (strNew2 == null || strNew2.length() <= 0){
            showToast("请输入密码");
            return;
        }
        if(!PwdUtil.isTruePwd(strNew)){
            showToast("密码必须是6-16位字母和数组组合的密码");
            etNew.setText("");
            etNew2.setText("");
            etNew.requestFocus();
            return;
        }
        if (!strNew.endsWith(strNew2)){
            showToast("两次密码输入不一致");
            return;
        }
        RequestParams params = new RequestParams(Urls.modifyPassword);
        params.addBodyParameter("token",user.getToken());
        params.addBodyParameter("sid",user.getSid()+"");
        params.addBodyParameter("password",strOld);
        params.addBodyParameter("passwords",strNew);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Logger.e(result);
                try {
                    JSONObject object = new JSONObject(result);
                    String returnCode = object.getString("return_code");
                    if (returnCode.equals("1000")){
                        user = new User();
                        userPersist.setUser(mContext,user);
                        showToast("修改成功，请重新登录。");
                        Intent intent = new Intent(mContext,LoginActivity.class);
                        intent.putExtra("phone",phone);
                        intent.putExtra("pwd",strNew);
                        startActivity(intent);
                        finish();
                    }else if(returnCode.equals("0004")){
                        showToast("原密码错误！请重新输入或找回密码");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
//                Logger.e(ex.getMessage());
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
