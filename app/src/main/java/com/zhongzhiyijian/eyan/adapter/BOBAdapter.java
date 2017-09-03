package com.zhongzhiyijian.eyan.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.zhongzhiyijian.eyan.entity.MyDevice;
import com.zhongzhiyijian.eyan.util.LogUtil;

import java.util.List;

public class BOBAdapter extends BaseAdapter{

	private Context mContext;
	private List<MyDevice> devices;
	private BobCallBack callBack;
	
	
	public BOBAdapter(Context mContext, List<MyDevice> devices,BobCallBack callBack) {
		this.mContext = mContext;
		this.devices = devices;
		this.callBack = callBack;
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
				holder.tvStatus.setText(R.string.connected);
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

}
