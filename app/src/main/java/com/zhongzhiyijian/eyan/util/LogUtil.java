package com.zhongzhiyijian.eyan.util;

import android.util.Log;

import com.zhongzhiyijian.eyan.base.Constants;

public class LogUtil implements Constants {

	public static void showLog(String msg){
		if(!isShowLog){
			return;
		}
		Log.d("TAG", msg);
	}
}
