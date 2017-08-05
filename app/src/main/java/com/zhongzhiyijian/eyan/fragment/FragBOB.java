package com.zhongzhiyijian.eyan.fragment;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.actions.ibluz.factory.BluzDeviceFactory;
import com.actions.ibluz.factory.BluzDeviceFactory.ConnectionState;
import com.actions.ibluz.factory.IBluzDevice;
import com.orhanobut.logger.Logger;
import com.zhongzhiyijian.eyan.R;
import com.zhongzhiyijian.eyan.activity.DeviceDetailActivity;
import com.zhongzhiyijian.eyan.activity.MainActivity;
import com.zhongzhiyijian.eyan.adapter.BOBAdapter;
import com.zhongzhiyijian.eyan.base.BaseFragment;
import com.zhongzhiyijian.eyan.entity.MyDevice;
import com.zhongzhiyijian.eyan.util.XUtil;

import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

public class FragBOB extends BaseFragment implements BOBAdapter.BobCallBack {

	private View view;
	private MainActivity mainActivity;

	private List<MyDevice> devices;
	private ListView mListView;
	private BOBAdapter mAdapter;

	private LinearLayout noDevice;

	private IBluzDevice mBluzDevice;

    private final static int MAX_RETRY_TIMES = 5;
    private int mConnectRetryTimes;


	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.frag_bob, null);
		mainActivity = (MainActivity) getActivity();
		dbUtils = x.getDb(XUtil.getDaoConfig());

		mBluzDevice = app.getBluzConnector();
		

		initViews();
		initList();
		initEvent();

		return view;
	}

	private void initEvent() {
		mBluzDevice.setOnDiscoveryListener(discoveryListener);
		mBluzDevice.setOnConnectionListener(connectionListener);


		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				MyDevice md = devices.get(position);
				final BluetoothDevice btDevice = btAdapter.getRemoteDevice(md.getDeviceAddress());
				BluetoothDevice connectedDevice = mBluzDevice.getConnectedDevice();

				if (connectedDevice == null){
					Logger.e("当前无设备连接，开始连接设备");
					mBluzDevice.connect(btDevice);
				}else{
					if(connectedDevice.getAddress().equals(btDevice.getAddress())){
						Logger.e("已有连接设备，进入详情");
						md.setState(ConnectionState.SPP_CONNECTED);
						mAdapter.notifyDataSetChanged();
						Intent it = new Intent(mContext,DeviceDetailActivity.class);
						mContext.startActivity(it);
					}else{
						Logger.e("点击设备地址-->" + btDevice.getAddress() + ",正在连接的设备地址-->" + btDevice.getAddress());
						mBluzDevice.disconnect(connectedDevice);
						mBluzDevice.connect(btDevice);
					}
				}
			}
		});

	}

	private IBluzDevice.OnConnectionListener connectionListener = new IBluzDevice.OnConnectionListener() {
		@Override
		public void onConnected(BluetoothDevice bluetoothDevice) {
			Logger.e("OnConnectionListener:当前蓝牙设备" + bluetoothDevice.getName() + "的连接状态为：已连接");
		}

		@Override
		public void onDisconnected(BluetoothDevice bluetoothDevice) {
			Logger.e("OnConnectionListener:当前蓝牙设备" + bluetoothDevice.getName() + "的连接状态为：未连接");
		}
	};

	private IBluzDevice.OnDiscoveryListener discoveryListener  = new IBluzDevice.OnDiscoveryListener() {
		@Override
		public void onConnectionStateChanged(BluetoothDevice device, int state) {
			Logger.e("OnDiscoveryListener:当前蓝牙设备" + device.getName() + "的连接状态为：" + state);
			if (device != null){
				MyDevice myDevice = findDevice(device);

				if (state == ConnectionState.A2DP_FAILURE) {
					state = ConnectionState.A2DP_DISCONNECTED;
					if (!retry(device)) {
						showToast("蓝牙连接失败，请重试");
					}
				} else if (state == ConnectionState.SPP_FAILURE) {
					state = ConnectionState.A2DP_CONNECTED;
					if (!retry(device)) {
						showToast("蓝牙数据连接失败，请检查设备");
					}
				}
				if (myDevice != null){
					myDevice.setState(state);
					mAdapter.notifyDataSetChanged();
				}
			}
		}
		@Override
		public void onDiscoveryStarted() {
		}
		@Override
		public void onDiscoveryFinished() {
		}
		@Override
		public void onFound(BluetoothDevice bluetoothDevice) {
		}
	};

	private boolean retry(BluetoothDevice device) {
		if (Build.MODEL.contains("Lenovo S939") || Build.MODEL.contains("Lenovo S898t+")) {
			mBluzDevice.disconnect(device);
			return true;
		}
		if (mConnectRetryTimes < MAX_RETRY_TIMES) {
			Logger.e("retry:" + mConnectRetryTimes);
			mBluzDevice.retry(device);
			mConnectRetryTimes++;
			return true;
		} else {
			mConnectRetryTimes = 0;
			return false;
		}
	}


	@Override
	public void onResume() {
		super.onResume();
        mConnectRetryTimes = 0;
		reSetData();
		mBluzDevice.setOnDiscoveryListener(discoveryListener);
		mBluzDevice.setOnConnectionListener(connectionListener);
	}
	
	@Override
	public void onHiddenChanged(boolean hidden) {
		if(hidden){
			onPause();
		}else{
			onResume();
		}
	}
	
	@Override
	public void onPause() {
		mBluzDevice.setOnDiscoveryListener(null);
		mBluzDevice.setOnConnectionListener(null);
		super.onPause();
	}



	private void initViews() {
		mListView = (ListView) view.findViewById(R.id.lv_device);
		noDevice = (LinearLayout) view.findViewById(R.id.ll_no_device);
	}

	private void initList() {
		devices = new ArrayList<MyDevice>();
		mAdapter = new BOBAdapter(mContext, devices,this);
		mListView.setAdapter(mAdapter);

	}


	private synchronized MyDevice findDevice(BluetoothDevice device){
		MyDevice md = null;
		for (MyDevice d : devices){
//			Logger.e("列表设备蓝牙地址-->" + d.getDeviceAddress() + "   扫描到设备蓝牙地址-->" + device.getAddress());
			if (d.getDeviceAddress().equals(device.getAddress())){
				md = d;
				break;
			}
		}
		return md;
	}

	private void reSetData(){
//		Logger.e("刷新bob界面");
		devices.clear();

        BluetoothDevice connectedDevice = mBluzDevice.getConnectedA2dpDevice();
        BluetoothDevice connectedDevice1 = mBluzDevice.getConnectedDevice();

        try {
			List<MyDevice> all = dbUtils.findAll(MyDevice.class);
			if (all != null){
				for (MyDevice d : all){
                    if (connectedDevice1 != null && !TextUtils.isEmpty(connectedDevice1.getAddress())){
                        if (d.getDeviceAddress().equals(connectedDevice1.getAddress())){
                            d.setState(ConnectionState.SPP_CONNECTED);
                            mBluzDevice.retry(connectedDevice);
                        }
                    }else if (connectedDevice != null && !TextUtils.isEmpty(connectedDevice.getAddress())){
						if (d.getDeviceAddress().equals(connectedDevice.getAddress())){
							d.setState(ConnectionState.A2DP_CONNECTED);
							mBluzDevice.retry(connectedDevice);
						}
					}else {
                        d.setState(ConnectionState.SPP_FAILURE);
                    }
					devices.add(d);
					Logger.e("reSetBOB  " + d.toString());
				}
			}

		} catch (DbException e) {
			e.printStackTrace();
		}
		mAdapter.notifyDataSetChanged();

		if (devices.size() > 0){
//			showLog("有设备devices.size ==> " + devices.size());
			mListView.setVisibility(View.VISIBLE);
			noDevice.setVisibility(View.GONE);
		}else{
//			showLog("无设备devices.size ==> " + devices.size());
			mListView.setVisibility(View.GONE);
			noDevice.setVisibility(View.VISIBLE);
		}
	}


	@Override
	public void onDestroy() {
		super.onDestroy();
		mBluzDevice.setOnDiscoveryListener(null);
	}


	@Override
	public void deleteDevice(int deviceId) {
		//删除设备

		try {
			MyDevice byId = dbUtils.findById(MyDevice.class, deviceId);
			if (byId != null){
				if (byId.getState() == ConnectionState.A2DP_CONNECTED||byId.getState() == ConnectionState.SPP_CONNECTED){
					//如果是连接状态则断开连接
					BluetoothDevice remoteDevice = btAdapter.getRemoteDevice(byId.getDeviceAddress());
					mBluzDevice.disconnect(remoteDevice);
					Intent it = new Intent("finish");
					mContext.sendBroadcast(it);
				}
				dbUtils.delete(byId);
			}
		} catch (DbException e) {
			e.printStackTrace();
		}

		reSetData();
	}
}
