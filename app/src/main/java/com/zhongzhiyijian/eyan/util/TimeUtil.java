package com.zhongzhiyijian.eyan.util;

import android.content.Context;

import com.zhongzhiyijian.eyan.R;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by yangfan on 2016/4/5.
 */
public class TimeUtil {

    public static String getStrFromInt(Context context,int time){
        String str = "";
        int min = time/60;
        int sen = time%60;
        if(min == 0){
            str = sen + context.getString(R.string.s);
        }else{
            str = min + context.getString(R.string.min) + sen + context.getString(R.string.s);
        }
        return str;
    }

    public static String getHourStrFromInt(int time){
        String str = "";
        int hour = 0;
        int min = time/60;
        if(min > 60){
            hour = min/60;
            min = min%60;
            str = hour + "小时" + min + "分钟";
        }else{
            if(min < 1){
                str = time + "秒";
            }else{
                str = min + "分钟";
            }
        }
        return str;
    }
    public static String getHourStrFromInt(Context context,float time){
        time = time*60*60;
        String str = "";
        float hour = 0;
        float min = time/60;
        if(min > 60){
            hour = min/60;
            min = min%60;
            str = hour + context.getString(R.string.hour) + min + context.getString(R.string.min);
        }else{
            if(min < 1){
                str = time + context.getString(R.string.s);
            }else{
                str =  new DecimalFormat("##0.00").format(min) + context.getString(R.string.min);
            }
        }
        return str;
    }

    public static String getMsgTime(long time){
        String str = "";
        Calendar calendar = Calendar.getInstance();
        int curYear = calendar.get(Calendar.YEAR);
        int curMonth = calendar.get(Calendar.MONTH) + 1;
        int curDay = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.setTime(new Date(time));
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);
        if (year != curYear){
            str = year + "-" + month + "-" + day;
        }else if(month != curMonth){
            str = month + "-" + day;
        }else if(day != curDay){
            str = month + "-" + day;
        }else{
            if (hour < 10){
                if (min < 10){
                    str = "0" + hour + ":" + "0" +  min;
                }else{
                    str = "0" + hour + ":" + min;
                }
            }else{
                if (min < 10){
                    str = hour + ":" + "0" +  min;
                }else{
                    str = hour + ":" + min;
                }
            }
        }
        return str;
    }

}
