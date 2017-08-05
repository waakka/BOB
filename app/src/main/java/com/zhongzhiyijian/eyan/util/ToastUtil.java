package com.zhongzhiyijian.eyan.util;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

public class ToastUtil {

	private static Toast  toast;
	private static int  duration = Toast.LENGTH_SHORT;
	
	public static void showToast(Context context,CharSequence text){
		if(toast == null){
			toast = Toast.makeText(context, text, duration);
		}else{
			toast.setDuration(duration);
			toast.setText(text);
		}
		toast.setGravity(Gravity.TOP, 0, 720);
		toast.show();
	}
}
