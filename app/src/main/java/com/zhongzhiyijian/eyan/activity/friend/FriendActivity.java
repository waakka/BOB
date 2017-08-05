package com.zhongzhiyijian.eyan.activity.friend;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.zhongzhiyijian.eyan.R;
import com.zhongzhiyijian.eyan.base.BaseActivity;
import com.zhongzhiyijian.eyan.base.Constants;
import com.zhongzhiyijian.eyan.net.Urls;

public class FriendActivity extends BaseActivity {

    private ImageButton ibBack;

    private TextView tvRole;

    private LinearLayout msg;
    private LinearLayout wx;
    private LinearLayout wxCircle;
    private TextView tvCode;

    private IWXAPI api;

    private String code;

    private String shareContext ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);

        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
        code = "eyan" + user.getSid();
        shareContext = "快来注册BOB吧，我的邀请码是" +  code;


        inieViews();
        initEvent();
    }

    private void inieViews() {
        ibBack = (ImageButton) findViewById(R.id.ib_back);
        tvRole = (TextView) findViewById(R.id.tv_role);
        msg = (LinearLayout) findViewById(R.id.ll_msg);
        wx = (LinearLayout) findViewById(R.id.ll_weixin);
        wxCircle = (LinearLayout) findViewById(R.id.ll_circle);
        tvCode = (TextView) findViewById(R.id.tv_code);
        tvCode.setText("我的邀请码：" + code);
    }

    private void initEvent() {
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvRole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                AlertDialog dialog;
//                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
//                builder.setTitle("用户注册协议");
//                builder.setMessage(jiangli);
//                dialog = builder.setPositiveButton("知道了", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                }).create();
//                dialog.show();
                intent2Activity(ShareRoleActivity.class);
            }
        });

        msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                sendIntent.putExtra("sms_body", shareContext+Urls.share + code);
                sendIntent.setType("vnd.android-dir/mms-sms");
                startActivity(sendIntent);
            }
        });
        wx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WXWebpageObject webpage = new WXWebpageObject();
                webpage.webpageUrl = Urls.share + code;
                WXMediaMessage msg = new WXMediaMessage(webpage);
                msg.title = "e眼按摩师";
                msg.description = "注册填写邀请码可获得30元优惠券奖励";

                SendMessageToWX.Req req = new SendMessageToWX.Req();
                req.transaction = buildTransaction("webpage");
                req.message = msg;
                req.scene = SendMessageToWX.Req.WXSceneSession;
                api.sendReq(req);

            }
        });
        wxCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WXWebpageObject webpage = new WXWebpageObject();
                webpage.webpageUrl = Urls.share + code;
                WXMediaMessage msg = new WXMediaMessage(webpage);
                msg.title = "e眼按摩师";
                msg.description = "注册填写邀请码可获得30元优惠券奖励";

                SendMessageToWX.Req req = new SendMessageToWX.Req();
                req.transaction = buildTransaction("webpage");
                req.message = msg;
                req.scene = SendMessageToWX.Req.WXSceneTimeline;
                api.sendReq(req);
            }
        });

    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

}
