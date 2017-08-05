package com.zhongzhiyijian.eyan.user;

import android.content.Context;
import android.content.SharedPreferences;

import com.zhongzhiyijian.eyan.base.Constants;
import com.zhongzhiyijian.eyan.entity.UserInfo;

/**
 * Created by Administrator on 2016/9/8.
 */
public class UserInfoPersist {
    public static UserInfoPersist manager = null;
    private UserInfoPersist(){};
    public synchronized static UserInfoPersist getInstance(){
        if(null==manager){
            synchronized(UserInfoPersist.class){
                if(null==manager){
                    manager = new UserInfoPersist();
                }
            }
        }
        return manager;
    }
    public static String ADDRESS = "address";
    public static String BIRTHDAY = "birthday";
    public static String BLOOD_TYPE = "blood_type";
    public static String NAME = "name";
    public static String OCCUPATION = "occupation";
    public static String SEX = "sex";
    public static String URL = "url";
    public static String MAIL = "MAIL";

    public void setUserInfo(Context context, UserInfo info){
        SharedPreferences sp = context.getSharedPreferences(Constants.SP_NAME,context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(ADDRESS,info.getAddress());
        editor.putLong(BIRTHDAY,info.getBirthday());
        editor.putString(NAME,info.getName());
        editor.putString(BLOOD_TYPE,info.getBlood_type());
        editor.putString(OCCUPATION,info.getOccupation());
        editor.putInt(SEX,info.getSex());
        editor.putString(URL,info.getUrl());
        editor.putString(MAIL,info.getMailbox());

        editor.commit();
    }

    public UserInfo getUser(Context context){
        SharedPreferences sp = context.getSharedPreferences(Constants.SP_NAME,context.MODE_PRIVATE);
        long bitthday = sp.getLong(BIRTHDAY,0);
        int sex = sp.getInt(SEX,0);
        String name = sp.getString(NAME,"");
        String address = sp.getString(ADDRESS,"");
        String occupation = sp.getString(OCCUPATION,"");
        String url = sp.getString(URL,"");
        String blood_type = sp.getString(BLOOD_TYPE,"");
        String mail = sp.getString(MAIL,"");
        UserInfo info = new UserInfo(address,bitthday,blood_type,name,occupation,sex,url,mail);
        return info;
    }

}
