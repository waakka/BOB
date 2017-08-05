package com.zhongzhiyijian.eyan.activity.msg;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.orhanobut.logger.Logger;
import com.zhongzhiyijian.eyan.R;
import com.zhongzhiyijian.eyan.activity.LoginActivity;
import com.zhongzhiyijian.eyan.activity.MainActivity;
import com.zhongzhiyijian.eyan.adapter.MsgAdapter;
import com.zhongzhiyijian.eyan.base.BaseActivity;
import com.zhongzhiyijian.eyan.entity.Msg;
import com.zhongzhiyijian.eyan.net.Urls;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MsgActivity extends BaseActivity {

    private ListView lvMsg;
    private List<Msg> data;
    private MsgAdapter mAdapter;
    private ImageButton ibBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg);

        initViews();
        initEvent();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (user.getToken()==null||user.getToken().equals("")){
            showToast(MsgActivity.this.getString(R.string.unlogin));
            intent2Activity(LoginActivity.class);
            finish();
        }
        getMsg();
    }

    private void getMsg() {
        RequestParams params = new RequestParams(Urls.getNewsList);
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
                        org.json.JSONArray array = object.getJSONArray("listn");
                        for (int i = 0 ; i < array.length() ; i++){
                            JSONObject obj = array.getJSONObject(i);
                            Msg msg = new Msg();
                            msg.setValue(obj.getString("value"));
                            msg.setType(obj.getInt("type"));
                            msg.setName(obj.getString("name"));
                            msg.setTime(obj.getLong("time"));
                            msg.setSid(obj.getInt("sid"));
                            if (msg.getType() != 2){
                            }
                            data.add(msg);
                        }
                        Collections.reverse(data);
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

    private void initViews() {
        ibBack = (ImageButton) findViewById(R.id.ib_back);
        lvMsg = (ListView) findViewById(R.id.lv_msg);
        data = new ArrayList<>();
        mAdapter = new MsgAdapter(mContext,data);
        lvMsg.setAdapter(mAdapter);
    }

    private void initEvent() {
        lvMsg.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                lvMsg.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Msg msg = data.get(position);
                        Intent it = new Intent(MsgActivity.this,MsgDetailActivity.class);
                        it.putExtra("msg",msg);
                        msg.setType(1);
                        mAdapter.notifyDataSetChanged();
                        MsgActivity.this.startActivity(it);
                    }
                });
            }
        });
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent2Activity(MainActivity.class);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        intent2Activity(MainActivity.class);
        super.onBackPressed();
    }
}
