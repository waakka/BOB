package com.zhongzhiyijian.eyan.fragment;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
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
import com.zhongzhiyijian.eyan.entity.SettingStatus;
import com.zhongzhiyijian.eyan.entity.SettingStatusUtil;
import com.zhongzhiyijian.eyan.service.PlayMusicService;
import com.zhongzhiyijian.eyan.util.DataUtil;
import com.zhongzhiyijian.eyan.util.XUtil;

import org.xutils.ex.DbException;
import org.xutils.x;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FragBOB extends BaseFragment implements BOBAdapter.BobCallBack {

	private List<MyDevice> devices;
	private BOBAdapter mAdapter;
	private IBluzDevice btConnector;
	private ListView mListView;
	private MainActivity mainActivity;
	private LinearLayout noDevice;
	private View view;

    private SettingStatus status;
    private SettingStatusUtil statusUtil;

    private DataUtil dataUtil;

	private InnerReceiver receiver;
	private IntentFilter filter;


	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		this.view = inflater.inflate(R.layout.frag_bob, null);

		mainActivity = (MainActivity)getActivity();
		dbUtils = x.getDb(XUtil.getDaoConfig());
		btConnector = app.getBluzConnector();

        statusUtil = SettingStatusUtil.getInstance();
        status = statusUtil.getSettingStatus(mContext);
        dataUtil = DataUtil.getInstance();

		initViews();
		initList();
		initEvent();
		initReceiver();
		return view;
	}

	private void initReceiver() {
		receiver = new InnerReceiver();
		filter = new IntentFilter();
		filter.addAction(WORK_TYPE_CHANGED);
		getActivity().registerReceiver(receiver,filter);
	}

	private class InnerReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if(WORK_TYPE_CHANGED.equals(action)){
				mAdapter.notifyDataSetChanged();
			}
		}
	}

	private void initEvent() {


		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if(!btConnector.isEnabled()){
//					btConnector.enable();
					showToast("蓝牙未启动，请先打开蓝牙。");
					return;
				}
				MyDevice device = devices.get(position);
				final BluetoothDevice btDevice = btAdapter.getRemoteDevice(device.getDeviceAddress());
				BluetoothDevice connectedDevice = btConnector.getConnectedDevice();
				if (connectedDevice != null){
					if (connectedDevice.getAddress().equals(btDevice.getAddress())){
						device.setState(ConnectionState.SPP_CONNECTED);
						mAdapter.notifyDataSetChanged();
						if(app.isConnect){
							Intent it = new Intent(mContext,DeviceDetailActivity.class);
							mContext.startActivity(it);
						}else{
							showToast("按摩板未连接");
						}
					}else{
						btConnector.connect(btDevice);
					}
				}else{
					btConnector.connect(btDevice);
				}
			}
		});

	}



	private IBluzDevice.OnDiscoveryListener discoveryListener  = new IBluzDevice.OnDiscoveryListener() {
		@Override
		public void onConnectionStateChanged(BluetoothDevice device, int state) {
            showLog(device.getAddress() + " state = " + state);
			if (device != null) {
				MyDevice entry = findDevice(device);
				if (entry == null) {
					entry = new MyDevice(device.getName(),device.getAddress(), state);
					devices.add(entry);
					try {
						if (dbUtils != null){
							dbUtils.close();
							dbUtils = null;
						}
						dbUtils = x.getDb(XUtil.getDaoConfig());
						dbUtils.saveOrUpdate(entry);
						if (dbUtils != null){
							dbUtils.close();
							dbUtils = null;
						}
					} catch (DbException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}else{
					entry.setState(state);
				}
				if(!btConnector.isEnabled()){

				}
				entry.setState(state);
				mAdapter.notifyDataSetChanged();
                if (state == ConnectionState.SPP_CONNECTED){

//					if(app.isConnect){
//						Intent it = new Intent(mContext,DeviceDetailActivity.class);
//						mContext.startActivity(it);
//					}else{
//						showToast("按摩板未连接");
//					}
                }
			}
		}
		@Override
		public void onDiscoveryStarted() {
			Logger.e("bob开始搜索");
		}
		@Override
		public void onDiscoveryFinished() {
			Logger.e("bob结束搜索");
		}
		@Override
		public void onFound(BluetoothDevice bluetoothDevice) {
		}
	};



	@Override
	public void onResume() {
		super.onResume();
		reSetData();
		btConnector.setOnDiscoveryListener(discoveryListener);
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
		btConnector.setOnDiscoveryListener(null);
		super.onPause();
	}

	@Override
	public void onDestroy() {
		getActivity().unregisterReceiver(receiver);
		super.onDestroy();
	}

	private void initViews() {
		mListView = (ListView) view.findViewById(R.id.lv_device);
		noDevice = (LinearLayout) view.findViewById(R.id.ll_no_device);
	}

	private void initList() {
		devices = new ArrayList<>();
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
		devices.clear();

        BluetoothDevice connectedDevice = btConnector.getConnectedA2dpDevice();
        BluetoothDevice connectedDevice1 = btConnector.getConnectedDevice();

        BluetoothDevice btd = btConnector.getConnectedDevice();

        try {
			if (dbUtils != null){
				dbUtils.close();
				dbUtils = null;
			}
			dbUtils = x.getDb(XUtil.getDaoConfig());
			List<MyDevice> all = dbUtils.findAll(MyDevice.class);
			if (all != null){
				for (MyDevice d : all){
                    if (connectedDevice1 != null && !TextUtils.isEmpty(connectedDevice1.getAddress())){
                        if (d.getDeviceAddress().equals(connectedDevice1.getAddress())){
                            d.setState(ConnectionState.SPP_CONNECTED);
                        }
                    }else if (connectedDevice != null && !TextUtils.isEmpty(connectedDevice.getAddress())){
						if (d.getDeviceAddress().equals(connectedDevice.getAddress())){
							d.setState(ConnectionState.A2DP_CONNECTED);
                            if (status.isAutoConnect()){
                                btConnector.retry(connectedDevice);
                            }
						}
					}else {
                        d.setState(ConnectionState.SPP_FAILURE);
                    }
					devices.add(d);
// 					Logger.e("重置BOB  " + d.toString());
				}
			}
			if (dbUtils != null){
				dbUtils.close();
				dbUtils = null;
			}
		} catch (DbException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		//判断是否有已连接设备未添加到列表
		if (btd!= null){
            boolean has = false;
            for(MyDevice d : devices){
                if(d.getDeviceAddress().equals(btd.getAddress())){
                    has = true;
                }
            }
            if (!has){
                devices.add(new MyDevice(btd.getName(),btd.getAddress(),BluzDeviceFactory.ConnectionState.SPP_CONNECTED));
            }
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
	public void deleteDevice(int deviceId) {
		//删除设备

		try {
			if (dbUtils != null){
				dbUtils.close();
				dbUtils = null;
			}
			dbUtils = x.getDb(XUtil.getDaoConfig());
			MyDevice byId = dbUtils.findById(MyDevice.class, deviceId);
			if (byId != null){
                BluetoothDevice connectedDevice = btConnector.getConnectedDevice();
                if (connectedDevice != null && connectedDevice.getAddress().equals(byId.getDeviceAddress())){
					//如果是连接状态则断开连接
                    dataUtil.stopData();
                    mainActivity.stopService(new Intent(mContext, PlayMusicService.class));
					BluetoothDevice remoteDevice = btAdapter.getRemoteDevice(byId.getDeviceAddress());
					btConnector.disconnect(remoteDevice);
					Intent it = new Intent("finish");
					mContext.sendBroadcast(it);
				}
				dbUtils.delete(byId);
			}
			if (dbUtils != null){
				dbUtils.close();
				dbUtils = null;
			}
		} catch (DbException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		reSetData();
	}
}
