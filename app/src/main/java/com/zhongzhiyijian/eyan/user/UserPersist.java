package com.zhongzhiyijian.eyan.user;

import android.content.Context;
import android.content.SharedPreferences;

import com.zhongzhiyijian.eyan.base.Constants;
import com.zhongzhiyijian.eyan.entity.User;

/**
 * Created by Administrator on 2016/9/8.
 */
public class UserPersist {
    public static UserPersist manager = null;
    private UserPersist(){};
    public synchronized static UserPersist getInstance(){
        if(null==manager){
            synchronized(UserPersist.class){
                if(null==manager){
                    manager = new UserPersist();
                }
            }
        }
        return manager;
    }
    public static String SID = "sid";
    public static String TOKEN = "token";
    public static String PHONE = "phone";
    public static String EMAIL = "email";
    public static String CLIENTID = "clientid";

    public void setUser(Context context, User user){
        SharedPreferences sp = context.getSharedPreferences(Constants.SP_NAME,context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(TOKEN,user.getToken());
        editor.putString(PHONE,user.getPhone());
        editor.putString(EMAIL,user.getEmail());
        editor.putInt(SID,user.getSid());
        editor.putString(CLIENTID,user.getClientid());
        editor.commit();
    }

    public User getUser(Context context){
        SharedPreferences sp = context.getSharedPreferences(Constants.SP_NAME,context.MODE_PRIVATE);
        String token = sp.getString(TOKEN,"");
        String phone = sp.getString(PHONE,"");
        String email = sp.getString(EMAIL,"");
        String clientid = sp.getString(CLIENTID,"");
        int sid = sp.getInt(SID,0);
        User user = new User(token,sid,phone,email,clientid);
        return user;
    }

}
