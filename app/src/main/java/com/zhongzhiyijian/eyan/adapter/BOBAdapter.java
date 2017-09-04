package com.zhongzhiyijian.eyan.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.actions.ibluz.factory.BluzDeviceFactory;
import com.zhongzhiyijian.eyan.R;
import com.zhongzhiyijian.eyan.base.BaseApplication;
import com.zhongzhiyijian.eyan.base.Constants;
import com.zhongzhiyijian.eyan.entity.MyDevice;
import com.zhongzhiyijian.eyan.util.LogUtil;

import java.util.List;

public class BOBAdapter extends BaseAdapter{

	private Context mContext;
	private List<MyDevice> devices;
	private BobCallBack callBack;
	private BaseApplication app;
	
	
	public BOBAdapter(Context mContext, List<MyDevice> devices,BobCallBack callBack) {
		this.mContext = mContext;
		this.devices = devices;
		this.callBack = callBack;
		app = (BaseApplication) mContext.getApplicationContext();
	}

	public interface BobCallBack{
		void deleteDevice(int deviceId);
	}

	@Override
	public int getCount() {
		return devices.size();
	}

	@Override
	public Object getItem(int position) {
		return devices.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	class ViewHolder{
		TextView tvName;
		TextView tvId;
		TextView tvStatus;
		LinearLayout llDelete;
		ImageView ivIcon;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView == null){
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_bob, null);
			holder = new ViewHolder();
			holder.tvName = (TextView) convertView.findViewById(R.id.tv_device_name);
			holder.tvId = (TextView) convertView.findViewById(R.id.tv_device_id);
			holder.tvStatus = (TextView) convertView.findViewById(R.id.tv_device_status);
			holder.llDelete = (LinearLayout) convertView.findViewById(R.id.ll_device_delete);
			holder.ivIcon = (ImageView) convertView.findViewById(R.id.iv_device_icon);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		final MyDevice device = devices.get(position);
		
		holder.tvName.setText(device.getDeviceName());
		holder.tvId.setText(device.getDeviceAddress());

		switch (device.getState()) {
			case BluzDeviceFactory.ConnectionState.SPP_CONNECTED:
//				holder.tvStatus.setText(R.string.connected);
				holder.tvStatus.setText(getText());
				break;

			case BluzDeviceFactory.ConnectionState.SPP_CONNECTING:
				holder.tvStatus.setText(R.string.connecting);
				break;

			case BluzDeviceFactory.ConnectionState.SPP_FAILURE:
				holder.tvStatus.setText(R.string.no_connected);
				break;

			case BluzDeviceFactory.ConnectionState.A2DP_CONNECTING:
				holder.tvStatus.setText(R.string.connecting);
				break;
			case BluzDeviceFactory.ConnectionState.A2DP_FAILURE:
				holder.tvStatus.setText("连接失败");
				break;

			case BluzDeviceFactory.ConnectionState.A2DP_CONNECTED:
				holder.tvStatus.setText(R.string.connecting);
				break;
			case BluzDeviceFactory.ConnectionState.SPP_DISCONNECTED:
				holder.tvStatus.setText(R.string.no_connected);
				break;

			case BluzDeviceFactory.ConnectionState.A2DP_DISCONNECTED:
				holder.tvStatus.setText(R.string.no_connected);
				break;

			case BluzDeviceFactory.ConnectionState.A2DP_PAIRING:
				holder.tvStatus.setText("配对...");
				break;
		}

		holder.ivIcon.setImageResource(R.mipmap.logo_bt_s);
		holder.llDelete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new AlertDialog.Builder(mContext)
					.setTitle(mContext.getResources().getString(R.string.dialog_title_device_delete))
					.setMessage(mContext.getResources().getString(R.string.dialog_msg_device_delete))
					.setPositiveButton(mContext.getResources().getString(R.string.dialog_done_device_delete), new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							//删除设备
							LogUtil.showLog("删除设备 " + device.getDeviceName());
							callBack.deleteDevice(device.getId());
						}
					})
					.setNegativeButton(mContext.getResources().getString(R.string.dialog_cancle_device_delete), null).show();
			}
		});
		
		return convertView;
	}

	private String getText(){
		String str = "";
		if(app.isConnect){
			if(app.workStatus == Constants.WORK_STATUS_DIANJI_ON_GAOYA_ON || app.workStatus == Constants.WORK_STATUS_DIANJI_OFF_GAOYA_ON){
				//正在工作
				if(!TextUtils.isEmpty(app.workType)){
					if ("1".equals(app.workType)){
						//工作界面：搓揉  工作状态：关闭
						str = "搓揉关闭";
					}else if("f1".equals(app.workType)){
						//工作界面：搓揉  工作状态：打开
						str = "搓揉工作";
					}else if ("2".equals(app.workType)){
						//工作界面：推压  工作状态：关闭
						str = "推压关闭";
					}else if("f2".equals(app.workType)){
						//工作界面：推压  工作状态：打开
						str = "推压工作";
					}else if ("3".equals(app.workType)){
						//工作界面：叩击  工作状态：关闭
						str = "叩击关闭";
					}else if("f3".equals(app.workType)){
						//工作界面：叩击  工作状态：打开
						str = "叩击工作";
					}else if ("4".equals(app.workType)){
						//工作界面：自动  工作状态：关闭
						str = "自动关闭";
					}else if("f4".equals(app.workType)){
						//工作界面：自动  工作状态：打开
						str = "自动工作";
					}
				}else{
					str = "已连接";
				}
			}else{
				//待机
				if(!TextUtils.isEmpty(app.workType)){
					if ("1".equals(app.workType)){
						//工作界面：搓揉  工作状态：关闭
						str = "搓揉关闭";
					}else if("f1".equals(app.workType)){
						//工作界面：搓揉  工作状态：打开
						str = "搓揉待机";
					}else if ("2".equals(app.workType)){
						//工作界面：推压  工作状态：关闭
						str = "推压关闭";
					}else if("f2".equals(app.workType)){
						//工作界面：推压  工作状态：打开
						str = "推压待机";
					}else if ("3".equals(app.workType)){
						//工作界面：叩击  工作状态：关闭
						str = "叩击关闭";
					}else if("f3".equals(app.workType)){
						//工作界面：叩击  工作状态：打开
						str = "叩击待机";
					}else if ("4".equals(app.workType)){
						//工作界面：自动  工作状态：关闭
						str = "自动关闭";
					}else if("f4".equals(app.workType)){
						//工作界面：自动  工作状态：打开
						str = "自动待机";
					}
				}else{
					str = "已连接";
				}
			}
		}else{
			str = "按摩板未连接";
		}
		return str;
	}

}
