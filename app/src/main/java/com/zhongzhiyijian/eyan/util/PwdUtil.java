package com.zhongzhiyijian.eyan.util;

/**
 * Created by Administrator on 2016/11/4.
 */
public class PwdUtil {
    public static String reg="^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,16}$";
    public static boolean isTruePwd (String pwd){
        return pwd.matches(reg);
    }
}
