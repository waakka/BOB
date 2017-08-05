package com.zhongzhiyijian.eyan.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.NotificationCompat;

import com.orhanobut.logger.Logger;
import com.zhongzhiyijian.eyan.R;
import com.zhongzhiyijian.eyan.activity.menu.IntegralCouponActivity;
import com.zhongzhiyijian.eyan.base.Constants;

import java.text.SimpleDateFormat;
import java.util.Date;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by Administrator on 2017/1/11.
 */

public class JieZhiReceiver extends BroadcastReceiver implements Constants{

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd,HH:mm");
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals("JIE_ZHI_RI_QI")){
            String data = intent.getStringExtra("title");
            Logger.e(sdf.format(new Date(System.currentTimeMillis())) + "收到截止日期推送通知，开始推送");
            Bitmap btm = BitmapFactory.decodeResource(context.getResources(),R.mipmap.push);
            Intent intent1 = new Intent(context,IntegralCouponActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent1,
                    PendingIntent.FLAG_CANCEL_CURRENT);
            NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
            Notification notification = builder
                    .setLargeIcon(btm)
                    .setContentTitle("e眼按摩师")
                    .setContentText(data)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.mipmap.push)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .build();
            manager.notify(1, notification);
        }
    }
}
