package com.zhongzhiyijian.eyan.activity.menu;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.zhongzhiyijian.eyan.R;
import com.zhongzhiyijian.eyan.activity.LoginActivity;
import com.zhongzhiyijian.eyan.adapter.CouponIntegralAdapter;
import com.zhongzhiyijian.eyan.base.BaseActivity;
import com.zhongzhiyijian.eyan.entity.CouponIntegral;
import com.zhongzhiyijian.eyan.entity.User;
import com.zhongzhiyijian.eyan.net.Urls;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;


public class IntegralCouponActivity extends BaseActivity implements CouponIntegralAdapter.OnDuiHUanClickLenstener{

    private ImageButton ibBack;
    private ListView lvCoupon;
    private TextView tvIntegral;
    private int integral;

    private List<CouponIntegral> datas;
    private CouponIntegralAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_integral_coupon);
        integral = getIntent().getIntExtra("integral",0);

        datas = new ArrayList<>();

        inieViews();
        initEvent();

        mAdapter = new CouponIntegralAdapter(mContext,datas,integral,IntegralCouponActivity.this);
        lvCoupon.setAdapter(mAdapter);


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (user.getToken()==null||user.getToken().equals("")){
            showToast(IntegralCouponActivity.this.getString(R.string.unlogin));
            intent2Activity(LoginActivity.class);
            finish();
        }
        getIntegral();
        getData();
    }

    private void inieViews() {
        ibBack = (ImageButton) findViewById(R.id.ib_back);
        lvCoupon = (ListView) findViewById(R.id.lv_coupon);
        tvIntegral = (TextView) findViewById(R.id.tv_integral);
        tvIntegral.setText(integral+"");
    }

    private void initEvent() {
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
                        datas.clear();
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




    public void getData() {
        final RequestParams params = new RequestParams(Urls.getcouponintegral);
        params.addBodyParameter("token",user.getToken());
        params.addBodyParameter("sid",user.getSid()+"");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Logger.e(result);
                try {
                    JSONObject object = new JSONObject(result);
                    String return_code = object.getString("return_code");
                    if(return_code.equals("1000")){
                        datas.clear();
                        org.json.JSONArray array = object.getJSONArray("list");
                        for (int i = 0 ; i < array.length() ; i++){
                            JSONObject obj = array.getJSONObject(i);
                            CouponIntegral ci = new CouponIntegral();
                            ci.setCouponconstant_id(obj.getInt("couponconstant_id"));
                            ci.setIntegral(obj.getInt("integral"));
                            ci.setTime(obj.getLong("time"));
                            ci.setValue1(obj.getString("value1"));
                            ci.setValue2(obj.getString("value2"));
                            datas.add(ci);
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Logger.e(ex.getLocalizedMessage());
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
    public void getClicked(int position) {
        CouponIntegral ci = datas.get(position);
        if (integral >= ci.getIntegral()){

            getCoupon(ci.getCouponconstant_id());
        }else{
            showToast("积分不足，当前优惠券需" + ci.getIntegral() + "积分才可兑换！");
        }

    }


    /**
     * 兑换优惠券
     * @param id
     */
    private void getCoupon(int id) {
        final RequestParams params = new RequestParams(Urls.addexchange);
        params.addParameter("token",user.getToken());
        params.addParameter("sid",user.getSid());
        params.addParameter("couponconstant_id",id);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Logger.e(result);
                try {
                    JSONObject object = new JSONObject(result);
                    String return_code = object.getString("return_code");
                    if(return_code.equals("1000")){
                        showToast("兑换成功");
                        finish();
                    }else{
                        showToast("兑换失败");
                    }
                } catch (JSONException e) {
//                    e.printStackTrace();
                    showToast("兑换失败，请重新兑换。");
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Logger.e(ex.getLocalizedMessage());
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
