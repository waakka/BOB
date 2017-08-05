package com.zhongzhiyijian.eyan.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Selection;
import android.text.Spannable;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.gson.Gson;
import com.igexin.sdk.PushManager;
import com.orhanobut.logger.Logger;
import com.zhongzhiyijian.eyan.R;
import com.zhongzhiyijian.eyan.activity.Forget.ForgetActivity1;
import com.zhongzhiyijian.eyan.base.BaseActivity;
import com.zhongzhiyijian.eyan.entity.DeviceNum;
import com.zhongzhiyijian.eyan.entity.User;
import com.zhongzhiyijian.eyan.entity.UserInfo;
import com.zhongzhiyijian.eyan.net.Urls;
import com.zhongzhiyijian.eyan.user.UserInfoPersist;
import com.zhongzhiyijian.eyan.user.UserPersist;
import com.zhongzhiyijian.eyan.util.XUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.DbManager;
import org.xutils.common.Callback;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends BaseActivity implements View.OnClickListener {


    private ImageButton ibClose;
    private TextView tvRegist;
    private TextView tvForget;
    private EditText etPhone;
    private EditText etPwd;
    private Button btnLogin;
    private ImageButton ibShow;

    private boolean isShow = false;

    private String phone,pwd;

    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        pd = new ProgressDialog(mContext);

        clientid = PushManager.getInstance().getClientid(mContext);
        Logger.e("clientid=" + clientid);

        phone = getIntent().getStringExtra("phone");
        pwd = getIntent().getStringExtra("pwd");
        Logger.e("phone==>" + phone + "  pwd==>" + pwd );


        initViews();
        initEvent();
        showPwd();


        if (phone!=null&&!phone.equals("")){
            etPhone.setText(phone);
        }
        if (pwd!=null&&!pwd.equals("")){
            etPwd.setText(pwd);
        }
    }

    private void showPwd() {
        if (isShow){
            ibShow.setBackgroundResource(R.mipmap.pwd_show);
            //设置EditText文本为可见的
            etPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        }else{
            ibShow.setBackgroundResource(R.mipmap.pwd_dismiss);
            etPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
        //切换后将EditText光标置于末尾
        CharSequence charSequence = etPwd.getText();
        if (charSequence instanceof Spannable) {
            Spannable spanText = (Spannable) charSequence;
            Selection.setSelection(spanText, charSequence.length());
        }
    }

    private void initViews() {
        ibClose = (ImageButton) findViewById(R.id.ib_close);
        ibShow = (ImageButton) findViewById(R.id.ib_show);
        tvRegist = (TextView) findViewById(R.id.tv_reg);
        tvForget = (TextView) findViewById(R.id.tv_forget);
        btnLogin = (Button) findViewById(R.id.btn_login);
        etPhone = (EditText) findViewById(R.id.et_phone);
        etPwd = (EditText) findViewById(R.id.et_pwd);
    }

    private void initEvent() {
        ibShow.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        ibClose.setOnClickListener(this);
        tvRegist.setOnClickListener(this);
        tvForget.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ib_show:
                isShow = !isShow;
                showPwd();
                break;
            case R.id.btn_login:
                login();
                break;
            case R.id.ib_close:
                intent2Activity(MainActivity.class);
                finish();
                break;
            case R.id.tv_reg:
                intent2Activity(RegistActivity.class);
                finish();
                break;
            case R.id.tv_forget:
                intent2Activity(ForgetActivity1.class);
                finish();
                break;
        }
    }

    private void canclePd(){
        if (pd.isShowing()){
            pd.dismiss();
        }
    }

    private void login() {
        pd.setMessage("登录中...");
        pd.show();
        phone = etPhone.getText().toString();
        pwd = etPwd.getText().toString();
//        phone = "15596827331";
//        pwd = "123456";
        if (phone == null || phone.equals("")){
            showToast("电话号码不能为空");
            canclePd();
            return;
        }
        if (pwd == null || pwd.equals("")){
            showToast("密码不能为空");
            canclePd();
            return;
        }
        RequestParams params = new RequestParams(Urls.login);
        params.addBodyParameter("mobile",phone);
        params.addBodyParameter("password",pwd);
        params.addBodyParameter("type","1");
        params.addBodyParameter("code","-1");
        params.addBodyParameter("invitation_code","-1");
        params.addBodyParameter("clientId",clientid);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Logger.e(result);
                try {
                    JSONObject object = new JSONObject(result);
                    String returnCode = object.getString("return_code");
                    if (returnCode.equals(SUCCESS)){
                        String token = object.getString("token");
                        int sid = object.getInt("sid");
                        User user = new User(token,sid,phone,"",clientid);
                        UserPersist userPersist = UserPersist.getInstance();
                        userPersist.setUser(mContext,user);
                        getUserData();

                    }else if(returnCode.equals(PWDERROR)){
                        showToast("您的密码有误，请重新输入！");
                    }else if(returnCode.equals(NOACCESS)){
                        showToast("此帐号不存在!");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                    Logger.e(ex.getMessage());
                showToast("网络错误！");
            }
            @Override
            public void onCancelled(CancelledException cex) {
            }
            @Override
            public void onFinished() {
                canclePd();
            }
        });
    }


    private void getUserData() {
        user = userPersist.getUser(mContext);
        RequestParams params = new RequestParams(Urls.getData);
        params.addBodyParameter("token",user.getToken());
        params.addBodyParameter("sid",user.getSid()+"");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Logger.e(result);
                try {
                    JSONObject object = new JSONObject(result);
                    String returnCode = object.getString("return_code");
                    if (returnCode.equals("1000")){
                        Gson gson = new Gson();
                        UserInfo info = gson.fromJson(result, UserInfo.class);

                        String name = info.getName();
                        String blood_type = info.getBlood_type();
                        String occupation = info.getOccupation();
                        String address = info.getAddress();
                        String mail = info.getMailbox();
                        String url = info.getUrl();
                        if (address.equals("-1")){
                            info.setAddress("");
                        }
                        if (occupation.equals("-1")){
                            info.setOccupation("");
                        }
                        if (blood_type.equals("-1")){
                            info.setBlood_type("");
                        }
                        if (name.equals("-1")){
                            info.setName("");
                        }
                        if (mail.equals("-1")){
                            info.setMailbox("");
                        }
                        if (url.equals("-1")){
                            info.setUrl("");
                        }

                        UserInfoPersist infoPersist = UserInfoPersist.getInstance();
                        infoPersist.setUserInfo(mContext,info);
                        Logger.e(info.toString());

                        user.setEmail(info.getMailbox());
                        userPersist.setUser(mContext,user);
                        Logger.e(user.toString());

                        JSONArray array = object.getJSONArray("list");
                        List<DeviceNum> nums = new ArrayList<DeviceNum>();
                        for (int i = 0 ; i < array.length() ; i++){
                            JSONObject obj = array.getJSONObject(i);
                            DeviceNum num = new DeviceNum();
                            num.setDid(obj.getInt("did"));
                            num.setName(obj.getString("name"));
                            num.setTime(obj.getLong("time"));
                            nums.add(num);
                        }
                        DbManager db = x.getDb(XUtil.getDaoConfig());
                        db.delete(DeviceNum.class);
                        db.save(nums);
                        intent2Activity(MainActivity.class);
                        finish();
                    }else if(returnCode.equals("0000")){
                        showToast("未知错误");
                    }else if(returnCode.equals("0004")){
                        showToast("密码错误，请重新输入");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (DbException e) {
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


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        intent2Activity(MainActivity.class);
        finish();
    }
}
