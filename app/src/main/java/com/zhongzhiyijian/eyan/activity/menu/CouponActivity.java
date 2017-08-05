package com.zhongzhiyijian.eyan.activity.menu;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.zhongzhiyijian.eyan.R;
import com.zhongzhiyijian.eyan.adapter.CouponAdapter;
import com.zhongzhiyijian.eyan.base.BaseActivity;
import com.zhongzhiyijian.eyan.entity.Coupon;
import com.zhongzhiyijian.eyan.net.Urls;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

public class CouponActivity extends BaseActivity {

    private ImageButton ibBack;
    private List<Coupon> data = new ArrayList<>();
    private List<Coupon> dataUnUse = new ArrayList<>();
    private List<Coupon> dataUse = new ArrayList<>();
    private List<Coupon> dataOverdue = new ArrayList<>();
    private ListView lvCoupon;
    private TextView tvEmpty;
    private CouponAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youhui);

        inieViews();
        initEvent();
        getData();
    }

    private void inieViews() {
        ibBack = (ImageButton) findViewById(R.id.ib_back);
        lvCoupon = (ListView) findViewById(R.id.lv_coupon);
        tvEmpty = (TextView) findViewById(R.id.tv_empty);
        mAdapter = new CouponAdapter(mContext,data);
        lvCoupon.setAdapter(mAdapter);
        tvEmpty.setVisibility(View.VISIBLE);
        lvCoupon.setVisibility(View.GONE);
    }

    private void initEvent() {
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void getData() {
        final long time = System.currentTimeMillis();
        final RequestParams params = new RequestParams(Urls.getCoupon);
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
                        data.clear();
                        dataUnUse.clear();
                        dataUse.clear();
                        dataOverdue.clear();
                        org.json.JSONArray array = object.getJSONArray("listc");
                        for (int i = 0 ; i < array.length() ; i++){
                            JSONObject obj = array.getJSONObject(i);
                            Coupon coupon = new Coupon();
                            coupon.setTerm_time(obj.getLong("term_time"));
                            coupon.setType(obj.getInt("type"));
                            coupon.setValue1(obj.getInt("value1"));
                            coupon.setValue2(obj.getInt("value2"));
                            coupon.setSid(obj.getInt("sid"));
                            coupon.setState(obj.getInt("state"));
                            if (coupon.getState() == 1 ){
                                //未使用
                                if(coupon.getTerm_time()*1000 > time){
                                    dataUnUse.add(coupon);
                                }else{
                                    dataOverdue.add(coupon);
                                }
                            }else{
                                dataUse.add(coupon);
                            }
                        }
                        data.addAll(dataUnUse);
                        data.addAll(dataUse);
                        data.addAll(dataOverdue);
                        if (data.size() > 0){
                            tvEmpty.setVisibility(View.GONE);
                            lvCoupon.setVisibility(View.VISIBLE);
                        }
                        mAdapter.notifyDataSetChanged();
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
}
