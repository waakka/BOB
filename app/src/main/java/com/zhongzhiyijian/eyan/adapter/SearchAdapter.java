package com.zhongzhiyijian.eyan.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.actions.ibluz.factory.BluzDeviceFactory;
import com.actions.ibluz.factory.BluzDeviceFactory.ConnectionState;
import com.zhongzhiyijian.eyan.R;
import com.zhongzhiyijian.eyan.entity.MyDevice;

import java.util.List;

public class SearchAdapter extends BaseAdapter{

	private List<MyDevice> devices;
	private Context mContext;

	public SearchAdapter(List<MyDevice> devices, Context mContext) {
		this.devices = devices;
		this.mContext = mContext;
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
		ImageView ivIcon;
		TextView tvName;
		ImageView ivConnected;
		TextView tvState;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView == null){
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_device_search, null);
			holder = new ViewHolder();
			holder.ivIcon = (ImageView) convertView.findViewById(R.id.iv_search_icon);
			holder.tvName = (TextView) convertView.findViewById(R.id.tv_search_name);
			holder.ivConnected = (ImageView) convertView.findViewById(R.id.iv_search_nake);
			holder.tvState = (TextView) convertView.findViewById(R.id.tv_search_state);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}

		MyDevice device = devices.get(position);

		holder.ivIcon.setImageResource(R.mipmap.logo_bt_b);
		holder.tvName.setText(device.getDeviceName());
		holder.ivConnected.setVisibility(View.GONE);
		switch (device.getState()) {
			case BluzDeviceFactory.ConnectionState.SPP_CONNECTED:
				holder.tvState.setText(R.string.connected);
				holder.ivConnected.setVisibility(View.VISIBLE);
				break;

			case ConnectionState.SPP_CONNECTING:
				holder.tvState.setText(R.string.connecting);
				break;

			case ConnectionState.A2DP_CONNECTING:
				holder.tvState.setText(R.string.connecting);
				break;

			case ConnectionState.A2DP_CONNECTED:
				holder.tvState.setText(R.string.connecting);
				break;

			case ConnectionState.A2DP_DISCONNECTED:
				holder.tvState.setText(R.string.no_connected);
				break;

			case ConnectionState.A2DP_PAIRING:
				holder.tvState.setText("配对...");
				break;
		}


		return convertView;
	}

}
