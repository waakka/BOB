package com.zhongzhiyijian.eyan.activity.Forget;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.orhanobut.logger.Logger;
import com.zhongzhiyijian.eyan.R;
import com.zhongzhiyijian.eyan.base.BaseActivity;
import com.zhongzhiyijian.eyan.net.Urls;
import com.zhongzhiyijian.eyan.util.CodeUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;


public class ForgetActivity1 extends BaseActivity {


    private EditText etPhone;
    private EditText etCode;
    private Button btnPhone;
    private Button btnEmail;
    private Button btnChangeCode;
    private ImageButton ibBack;
    private ImageView ivCode;

    private CodeUtils codeUtils;
    private String code;

    private String codeStr;
    private String phoneStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget1);

        initViews();
        initEvent();
        initCode();
    }

    private void initCode() {
        codeUtils = CodeUtils.getInstance();
        Bitmap bitmap = codeUtils.createBitmap();
        ivCode.setImageBitmap(bitmap);
        code = codeUtils.getCode();
    }

    private void initViews() {
        etPhone = (EditText) findViewById(R.id.et_phone);
        etCode = (EditText) findViewById(R.id.et_code);
        ibBack = (ImageButton) findViewById(R.id.ib_back);
        btnPhone = (Button) findViewById(R.id.btn_phone);
        btnEmail = (Button) findViewById(R.id.btn_email);
        btnChangeCode = (Button) findViewById(R.id.btn_change_code);
        ivCode = (ImageView) findViewById(R.id.iv_code);

        btnPhone.setClickable(false);
        btnEmail.setClickable(false);
        btnPhone.setBackgroundResource(R.drawable.bg_btn_login_uncheck);
        btnPhone.setBackgroundResource(R.drawable.bg_btn_login_uncheck);
    }


    private void initEvent() {
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnChangeCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initCode();
                etCode.setText("");
            }
        });

        etCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                codeStr = etCode.getText().toString();
                if (codeStr.equalsIgnoreCase(code)){
                    btnPhone.setClickable(true);
                    btnEmail.setClickable(true);
                    btnPhone.setBackgroundResource(R.drawable.bg_btn_login);
                    btnEmail.setBackgroundResource(R.drawable.bg_btn_login);
                }else{
                    btnPhone.setClickable(false);
                    btnEmail.setClickable(false);
                    btnPhone.setBackgroundResource(R.drawable.bg_btn_login_uncheck);
                    btnEmail.setBackgroundResource(R.drawable.bg_btn_login_uncheck);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        btnPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneStr = etPhone.getText().toString();
                showToast("短信验证");
                Intent it = new Intent(mContext,ForgetActivity2.class);
                it.putExtra("type","phone");
                it.putExtra("phone",phoneStr);
                startActivity(it);
                finish();
            }
        });

        btnEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneStr = etPhone.getText().toString();
                RequestParams params = new RequestParams(Urls.getEmail);
                params.addParameter("mobile",phoneStr);
                params.addParameter("password","-1");
                params.addParameter("type","5");
                params.addParameter("code","-1");
                x.http().get(params, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        Logger.e("result===>" + result);
                        try {
                            JSONObject object = new JSONObject(result);
                            String returnCode = object.getString("return_code");
                            if (returnCode.equals(SUCCESS)){
                                String email = object.getString("mailbox");
                                showToast("邮箱验证");
                                Intent it = new Intent(mContext,ForgetActivity2.class);
                                it.putExtra("type","email");
                                it.putExtra("email",email);
                                it.putExtra("phone",phoneStr);
                                startActivity(it);
                                finish();
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
        });


    }



}
