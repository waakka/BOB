package com.zhongzhiyijian.eyan.activity.menu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.zhongzhiyijian.eyan.R;
import com.zhongzhiyijian.eyan.activity.LoginActivity;
import com.zhongzhiyijian.eyan.activity.MainActivity;
import com.zhongzhiyijian.eyan.base.BaseActivity;
import com.zhongzhiyijian.eyan.entity.User;
import com.zhongzhiyijian.eyan.net.Urls;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

public class IntegralActivity extends BaseActivity {

    private ImageButton ibBack;
    private RelativeLayout rlRole;
    private RelativeLayout rlRoleData;
    private TextView tvIntegral;
    private ImageButton ibCou;

    private int integral;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jifen);
        integral = getIntent().getIntExtra("integral",0);

        initViews();
        initEvent();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getIntegral();
    }

    private void initViews() {
        ibBack = (ImageButton) findViewById(R.id.ib_back);
        rlRole = (RelativeLayout) findViewById(R.id.rl_role);
        rlRoleData = (RelativeLayout) findViewById(R.id.rl_role_data);
        tvIntegral = (TextView) findViewById(R.id.tv_integral);
        ibCou = (ImageButton) findViewById(R.id.ib_cou);

        tvIntegral.setText(integral+"");
    }

    /**
     * 获取积分
     */
    public void getIntegral() {
        Logger.e(user.toString());
        RequestParams params = new RequestParams(Urls.getIntegral);
        params.addBodyParameter("token",user.getToken());
        params.addBodyParameter("sid",user.getSid()+"");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject object = new JSONObject(result);
                    String returnCode = object.getString("return_code");
                    if (returnCode.equals(SUCCESS)){
                        integral = object.getInt("integral");
                        tvIntegral.setText(integral+"");
                    }else if(returnCode.equals(ALREADYLOGIN)){
                        showToast("您的登录信息已失效或在其他客户端登录，请重新登录");
                        user = new User();
                        userPersist.setUser(mContext,user);
                        onResume();
                        intent2Activity(LoginActivity.class);
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

    private void initEvent() {
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent2Activity(MainActivity.class);
                finish();
            }
        });
        rlRole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent2Activity(RoleActivity.class);
            }
        });
        rlRoleData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent2Activity(IntegralDetailActivity.class);
            }
        });
        ibCou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(mContext,IntegralCouponActivity.class);
                it.putExtra("integral",integral);
                startActivity(it);
            }
        });
    }

    @Override
    public void onBackPressed() {
        intent2Activity(MainActivity.class);
        super.onBackPressed();
    }
}
