package com.zhongzhiyijian.eyan.entity;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.zhongzhiyijian.eyan.base.Constants;

public class SettingStatusUtil {

	private static SettingStatusUtil instance;

	private SettingStatusUtil (){}

	public static SettingStatusUtil getInstance(){
		if(instance == null){
			instance = new SettingStatusUtil();
		}
		return instance;
	}

	public static String IS_USB_AUTO_CONNECT = "isUSBAutoConnect";
	public static String STYLE = "style";
	public static String LANGUAGE = "language";
	public static String TXT_SIZE = "txtSize";
	public static String IS_AUTO_CONNECT = "isAutoConnect";
	public static String VERSION_CODE = "versionCode";

	public void setSettingStatus(Context context , SettingStatus status){
		SharedPreferences sp = context.getSharedPreferences(Constants.SP_NAME, context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putBoolean(IS_USB_AUTO_CONNECT, status.isUSBAutoConnect());
		editor.putString(STYLE, status.getStyle());
		editor.putString(LANGUAGE, status.getLanguage());
		editor.putInt(TXT_SIZE, status.getTxtSize());
		editor.putBoolean(IS_AUTO_CONNECT, status.isAutoConnect());
		editor.putString(VERSION_CODE, status.getVersionCode());
		editor.commit();
	}


	public SettingStatus getSettingStatus(Context context){
		SharedPreferences sp = context.getSharedPreferences(Constants.SP_NAME, context.MODE_PRIVATE);
		boolean isUSBAutoConnect = sp.getBoolean(IS_USB_AUTO_CONNECT, false);
		String style = sp.getString(STYLE, "普通");
		String language = sp.getString(LANGUAGE, "简体中文");
		int txtSize = sp.getInt(TXT_SIZE, 1);
		boolean isAutoConnect = sp.getBoolean(IS_AUTO_CONNECT, true);
		String versionCode = sp.getString(VERSION_CODE, "1.0");

		SettingStatus status = new SettingStatus();
		status.setUSBAutoConnect(isUSBAutoConnect);
		status.setStyle(style);
		status.setLanguage(language);
		status.setTxtSize(txtSize);
		status.setAutoConnect(isAutoConnect);
		status.setVersionCode(versionCode);

		return status;
	}




}
