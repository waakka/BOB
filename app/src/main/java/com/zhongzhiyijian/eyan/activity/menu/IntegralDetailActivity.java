package com.zhongzhiyijian.eyan.activity.menu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.zhongzhiyijian.eyan.R;
import com.zhongzhiyijian.eyan.adapter.IntegralAdapter;
import com.zhongzhiyijian.eyan.base.BaseActivity;
import com.zhongzhiyijian.eyan.entity.Integral;
import com.zhongzhiyijian.eyan.net.Urls;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;


public class IntegralDetailActivity extends BaseActivity {

    private ImageButton ibBack;
    private ListView lvDetail;
    private List<Integral> data;
    private List<Integral> dataJia;
    private List<Integral> dataJian;
    private List<Integral> dataCurMonth;
    private IntegralAdapter mAdapter;

    private TextView tvCurMonth;
    private Spinner spinner;
    private List<String> mItems = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
    private String curStr;

    private TextView tvSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jifen_detail);

        curStr = sdf.format(new Date());

        initViews();
        initEvent();
        initSpinner();
        getIntegralDetail();
    }

    private void initSpinner() {
        // 建立数据源
        mItems.add("所有记录");
        mItems.add("获取积分");
        mItems.add("消费积分");
// 建立Adapter并且绑定数据源
        adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, mItems);
        tvSpinner = (TextView) LayoutInflater.from(mContext).inflate(android.R.layout.simple_spinner_item,null);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//绑定 Adapter到控件
        spinner .setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {

                showLog("你点击的是:"+mItems.get(pos));
                tvSpinner = (TextView) view;
                switch (pos){
                    case 0 :
                        mAdapter.setData(data);
                        mAdapter.notifyDataSetChanged();
                        tvCurMonth.setTextColor(getResources().getColor(R.color.gray_dark));
                        tvSpinner.setTextColor(getResources().getColor(R.color.green));
                        break;
                    case 1 :
                        mAdapter.setData(dataJia);
                        mAdapter.notifyDataSetChanged();
                        tvCurMonth.setTextColor(getResources().getColor(R.color.gray_dark));
                        tvSpinner.setTextColor(getResources().getColor(R.color.green));
                        break;
                    case 2 :
                        mAdapter.setData(dataJian);
                        mAdapter.notifyDataSetChanged();
                        tvCurMonth.setTextColor(getResources().getColor(R.color.gray_dark));
                        tvSpinner.setTextColor(getResources().getColor(R.color.green));
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });
    }

    private void initViews() {
        ibBack = (ImageButton) findViewById(R.id.ib_back);
        tvCurMonth = (TextView) findViewById(R.id.tv_cur_month);
        spinner = (Spinner) findViewById(R.id.spinner);
        lvDetail = (ListView) findViewById(R.id.lv_integral_detail);
        data = new ArrayList<>();
        dataJia = new ArrayList<>();
        dataJian = new ArrayList<>();
        dataCurMonth = new ArrayList<>();
        mAdapter = new IntegralAdapter(mContext,data);
        lvDetail.setAdapter(mAdapter);

        tvSpinner = (TextView) spinner.getChildAt(0);
    }

    private void initEvent() {
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvCurMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapter.setData(dataCurMonth);
                mAdapter.notifyDataSetChanged();
                tvCurMonth.setTextColor(getResources().getColor(R.color.green));
                tvSpinner.setTextColor(getResources().getColor(R.color.gray_dark));
            }
        });
    }

    public void getIntegralDetail() {
        Logger.e(user.toString());
        RequestParams params = new RequestParams(Urls.getIntegralRecords);
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
                        data.clear();
                        dataJia.clear();
                        dataJian.clear();
                        dataCurMonth.clear();
                        org.json.JSONArray array = object.getJSONArray("listr");
                        for (int i = 0 ; i < array.length() ; i++){
                            JSONObject obj = array.getJSONObject(i);
                            Integral integral = new Integral();
                            integral.setType(obj.getInt("type"));
                            integral.setIntegral(obj.getInt("integral"));
                            integral.setTime(obj.getLong("time")*1000);
                            integral.setValue(obj.getString("value"));
                            data.add(integral);
                            if (integral.getType() == 1){
                                dataJia.add(integral);
                            }else{
                                dataJian.add(integral);
                            }
                            if(curStr.equals(sdf.format(new Date(integral.getTime())))){
                                dataCurMonth.add(integral);
                            }
                        }
                        Collections.reverse(data);
                        Collections.reverse(dataJia);
                        Collections.reverse(dataJian);
                        Collections.reverse(dataCurMonth);
                        mAdapter.notifyDataSetChanged();
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
}
