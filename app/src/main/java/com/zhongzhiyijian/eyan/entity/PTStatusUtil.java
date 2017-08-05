package com.zhongzhiyijian.eyan.entity;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.zhongzhiyijian.eyan.base.Constants;

public class PTStatusUtil {
	public static PTStatusUtil ptStatusUtil;
	
	private PTStatusUtil(){};
	
	public static PTStatusUtil getInstance(){
		if(ptStatusUtil == null){
			ptStatusUtil = new PTStatusUtil();
		}
		return ptStatusUtil;
	}
	
	public static String STATUS_ZHENJIU_IS_OPEN = "statusZhenJiuIsOpen";
	public static String STATUS_ZHENJIU_INTANSITY = "statusZhenJiuIntensity";
	public static String STATUS_ZHENJIU_IS_CLOCK = "statusZhenJiuIsClock";
	public static String STATUS_ZHENJIU_CLOCK_TIME = "statusZhenJiuClockTime";
	
	public static String STATUS_ANMO_IS_OPEN = "statusAnMoIsOpen";
	public static String STATUS_ANMO_INTANSITY = "statusAnMoIntensity";
	public static String STATUS_ANMO_IS_CLOCK = "statusAnMoIsClock";
	public static String STATUS_ANMO_CLOCK_TIME = "statusAnMoClockTime";
	
	public static String STATUS_LILIAO_IS_OPEN = "statusLiLiaoIsOpen";
	public static String STATUS_LILIAO_INTANSITY = "statusLiLiaoIntensity";
	public static String STATUS_LILIAO_IS_CLOCK = "statusLiLiaoIsClock";
	public static String STATUS_LILIAO_CLOCK_TIME = "statusLiLiaoClockTime";
	
	public static String STATUS_YUELIAO_IS_OPEN = "statusYueLiaoIsOpen";
	public static String STATUS_YUELIAO_INTANSITY = "statusYueLiaoIntensity";
	public static String STATUS_YUELIAO_FREQUENCY = "statusYueLiaoFrequecy";
	public static String STATUS_YUELIAO_IS_CLOCK = "statusYueLiaoIsClock";
	public static String STATUS_YUELIAO_CLOCK_TIME = "statusYueLiaoClockTime";
	
	public void setPTStatus(Context context,PTStatus ptStatus){
		SharedPreferences sp = context.getSharedPreferences(Constants.SP_NAME, context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putBoolean(STATUS_ZHENJIU_IS_OPEN, ptStatus.isStatusZhenJiuIsOpen());
		editor.putInt(STATUS_ZHENJIU_INTANSITY, ptStatus.getStatusZhenJiuIntensity());
		editor.putBoolean(STATUS_ZHENJIU_IS_CLOCK, ptStatus.isStatusZhenJiuIsClock());
		editor.putInt(STATUS_ZHENJIU_CLOCK_TIME, ptStatus.getStatusZhenJiuClockTime());
		
		editor.putBoolean(STATUS_ANMO_IS_OPEN, ptStatus.isStatusAnMoIsOpen());
		editor.putInt(STATUS_ANMO_INTANSITY, ptStatus.getStatusAnMoIntensity());
		editor.putBoolean(STATUS_ANMO_IS_CLOCK, ptStatus.isStatusAnMoIsClock());
		editor.putInt(STATUS_ANMO_CLOCK_TIME, ptStatus.getStatusAnMoClockTime());
		
		editor.putBoolean(STATUS_LILIAO_IS_OPEN, ptStatus.isStatusLiLiaoIsOpen());
		editor.putInt(STATUS_LILIAO_INTANSITY, ptStatus.getStatusLiLiaoIntensity());
		editor.putBoolean(STATUS_LILIAO_IS_CLOCK, ptStatus.isStatusLiLiaoIsClock());
		editor.putInt(STATUS_LILIAO_CLOCK_TIME, ptStatus.getStatusLiLiaoClockTime());
		
		editor.putBoolean(STATUS_YUELIAO_IS_OPEN, ptStatus.isStatusYueLiaoIsOpen());
		editor.putInt(STATUS_YUELIAO_INTANSITY, ptStatus.getStatusYueLiaoIntensity());
		editor.putInt(STATUS_YUELIAO_FREQUENCY, ptStatus.getStatusYueLiaoFrequency());
		editor.putBoolean(STATUS_YUELIAO_IS_CLOCK, ptStatus.isStatusYueLiaoIsClock());
		editor.putInt(STATUS_YUELIAO_CLOCK_TIME, ptStatus.getStatusYueLiaoClockTime());
		
		editor.commit();
	}
	
	public PTStatus getPTStatus(Context context){
		SharedPreferences sp = context.getSharedPreferences(Constants.SP_NAME, context.MODE_PRIVATE);
		
		boolean statusZhenJiuIsOpen = sp.getBoolean(STATUS_ZHENJIU_IS_OPEN, false);
		int statusZhenJiuIntensity = sp.getInt(STATUS_ZHENJIU_INTANSITY, 0);
		boolean statusZhenJiuIsClock = sp.getBoolean(STATUS_ZHENJIU_IS_CLOCK, false);
		int statusZhenJiuClockTime = sp.getInt(STATUS_ZHENJIU_CLOCK_TIME, 0);
		
		
		boolean statusAnMoIsOpen = sp.getBoolean(STATUS_ANMO_IS_OPEN, false);
		int statusAnMoIntensity = sp.getInt(STATUS_ANMO_INTANSITY, 0);
		boolean statusAnMoIsClock = sp.getBoolean(STATUS_ANMO_IS_CLOCK, false);
		int statusAnMoClockTime = sp.getInt(STATUS_ANMO_CLOCK_TIME, 0);
		
		boolean statusLiLiaoIsOpen = sp.getBoolean(STATUS_LILIAO_IS_OPEN, false);
		int statusLiLiaoIntensity = sp.getInt(STATUS_LILIAO_INTANSITY, 0);
		boolean statusLiLiaoIsClock = sp.getBoolean(STATUS_LILIAO_IS_CLOCK, false);
		int statusLiLiaoClockTime = sp.getInt(STATUS_LILIAO_CLOCK_TIME, 0);
		
		boolean statusYueLiaoIsOpen = sp.getBoolean(STATUS_YUELIAO_IS_OPEN, false);
		int statusYueLiaoIntensity = sp.getInt(STATUS_YUELIAO_INTANSITY, 0);
		int statusYueLiaoFrequency = sp.getInt(STATUS_YUELIAO_FREQUENCY, 0);
		boolean statusYueLiaoIsClock = sp.getBoolean(STATUS_YUELIAO_IS_CLOCK, false);
		int statusYueLiaoClockTime = sp.getInt(STATUS_YUELIAO_CLOCK_TIME, 0);
		
		PTStatus ptStatus = new PTStatus();
		ptStatus.setStatusZhenJiuIsOpen(statusZhenJiuIsOpen);
		ptStatus.setStatusZhenJiuIntensity(statusZhenJiuIntensity);
		ptStatus.setStatusZhenJiuIsClock(statusZhenJiuIsClock);
		ptStatus.setStatusZhenJiuClockTime(statusZhenJiuClockTime);
		
		ptStatus.setStatusAnMoIsOpen(statusAnMoIsOpen);
		ptStatus.setStatusAnMoIntensity(statusAnMoIntensity);
		ptStatus.setStatusAnMoIsClock(statusAnMoIsClock);
		ptStatus.setStatusAnMoClockTime(statusAnMoClockTime);
		
		ptStatus.setStatusLiLiaoIsOpen(statusLiLiaoIsOpen);
		ptStatus.setStatusLiLiaoIntensity(statusLiLiaoIntensity);
		ptStatus.setStatusLiLiaoIsClock(statusLiLiaoIsClock);
		ptStatus.setStatusLiLiaoClockTime(statusLiLiaoClockTime);
		
		ptStatus.setStatusYueLiaoIsOpen(statusYueLiaoIsOpen);
		ptStatus.setStatusYueLiaoIntensity(statusYueLiaoIntensity);
		ptStatus.setStatusYueLiaoFrequency(statusYueLiaoFrequency);
		ptStatus.setStatusYueLiaoIsClock(statusYueLiaoIsClock);
		ptStatus.setStatusYueLiaoClockTime(statusYueLiaoClockTime);
		
		return ptStatus;
	}
	
	
	
	
	
	
	
	
	
}
