package com.zhongzhiyijian.eyan.fragment;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.actions.ibluz.factory.BluzDeviceFactory;
import com.actions.ibluz.factory.IBluzDevice;
import com.orhanobut.logger.Logger;
import com.zhongzhiyijian.eyan.R;
import com.zhongzhiyijian.eyan.activity.DeviceDetailActivity;
import com.zhongzhiyijian.eyan.activity.MainActivity;
import com.zhongzhiyijian.eyan.adapter.SearchAdapter;
import com.zhongzhiyijian.eyan.base.BaseFragment;
import com.zhongzhiyijian.eyan.entity.MyDevice;
import com.zhongzhiyijian.eyan.util.XUtil;

import org.xutils.ex.DbException;
import org.xutils.x;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FragSearch extends BaseFragment {

	private View view;
	private MainActivity mainActivity;

	private ListView lvDevice;
	private SearchAdapter mAdapter;
	private List<MyDevice> devices;

	private LinearLayout llSearch;
	private LinearLayout llSearching;
	private View headView;
	private LinearLayout llHead;

	private LinearLayout llEmpty;

	private int curPosition;

	private IBluzDevice mBluzConnector;


	@Override
	public View onCreateView(LayoutInflater inflater,
							 @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.frag_search, null);
		mainActivity = (MainActivity) getActivity();
		if (dbUtils == null){
			dbUtils = x.getDb(XUtil.getDaoConfig());
		}

		mBluzConnector = app.getBluzConnector();

		initViews();
		setListView();
		initEvent();

		return view;
	}



	private void initViews() {
		lvDevice = (ListView) view.findViewById(R.id.lv_search);
		llSearch = (LinearLayout) view.findViewById(R.id.ll_search);
		llSearching = (LinearLayout) view.findViewById(R.id.ll_searching);
		headView = LayoutInflater.from(mContext).inflate(R.layout.head_view_search, null);
		llHead = (LinearLayout) headView.findViewById(R.id.ll_head);
		llEmpty = (LinearLayout) view.findViewById(R.id.ll_empty);

		lvDevice.addHeaderView(headView);
		llHead.setVisibility(View.GONE);
	}

	private void setListView() {
		devices = new ArrayList<MyDevice>();
		mAdapter = new SearchAdapter(devices, mContext);
		lvDevice.setAdapter(mAdapter);
		if (devices.size()==0){
			llEmpty.setVisibility(View.VISIBLE);
			lvDevice.setVisibility(View.GONE);
		}else{
			llEmpty.setVisibility(View.GONE);
			lvDevice.setVisibility(View.VISIBLE);
		}
	}



	private void initEvent() {
		//搜索当前可连接的设备
		llSearch.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!mBluzConnector.isEnabled()){
					showLog("蓝牙未打开，后台打开蓝牙");
					mBluzConnector.enable();
					showToast("打开蓝牙后请点击搜索按钮");
					return;
				}
				devices.clear();
				mAdapter.notifyDataSetChanged();
				mBluzConnector.startDiscovery();
			}
		});



		lvDevice.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				if(position == 0){
					return;
				}
				curPosition = position - 1;


				llSearch.setVisibility(View.VISIBLE);
				llSearching.setVisibility(View.GONE);
				llHead.setVisibility(View.GONE);



				MyDevice md = devices.get(curPosition);
				BluetoothDevice btDevice = btAdapter.getRemoteDevice(md.getDeviceAddress());
				if (md.getState() == BluzDeviceFactory.ConnectionState.SPP_CONNECTED
                        || md.getState() == BluzDeviceFactory.ConnectionState.A2DP_CONNECTED){
					Intent it = new Intent(mContext,DeviceDetailActivity.class);
//					it.putExtra("device",btDevice);
					mContext.startActivity(it);
				}else{
					mBluzConnector.connect(btDevice);
				}
			}
		});

	}

	private IBluzDevice.OnDiscoveryListener discoveryListener = new IBluzDevice.OnDiscoveryListener() {
		@Override
		public void onConnectionStateChanged(BluetoothDevice device, int state) {
			if (device != null){

				MyDevice myDevice = findDevice(device);
				if (myDevice == null){
					myDevice = new MyDevice(device.getName(),device.getAddress(),state);
					devices.add(myDevice);
					try {
						if (dbUtils != null){
							dbUtils.close();
							dbUtils = null;
						}
						dbUtils = x.getDb(XUtil.getDaoConfig());
						dbUtils.saveOrUpdate(myDevice);
						if (dbUtils != null){
							dbUtils.close();
							dbUtils = null;
						}
					} catch (DbException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
					Logger.e("添加新设备到数据库  " + myDevice.toString());
				}else{
					for (MyDevice d : devices){
						if (d.getDeviceAddress().equals(device.getAddress())){
							d.setState(state);
						}
					}
				}
				mAdapter.notifyDataSetChanged();
				if (state == BluzDeviceFactory.ConnectionState.SPP_CONNECTED){
					Intent it = new Intent(mContext,DeviceDetailActivity.class);
					mContext.startActivity(it);
				}
			}
		}

		@Override
		public void onDiscoveryStarted() {
			llEmpty.setVisibility(View.GONE);
			lvDevice.setVisibility(View.VISIBLE);
			llSearch.setVisibility(View.GONE);
			llSearching.setVisibility(View.VISIBLE);
			llHead.setVisibility(View.VISIBLE);
		}

		@Override
		public void onDiscoveryFinished() {
			llSearch.setVisibility(View.VISIBLE);
			llSearching.setVisibility(View.GONE);
			llHead.setVisibility(View.GONE);
            BluetoothDevice a2dpDevice = mBluzConnector.getConnectedA2dpDevice();
            if(null != a2dpDevice){
                if(null == findDevice(a2dpDevice)){
                    MyDevice myDevice = new MyDevice(a2dpDevice.getName(),a2dpDevice.getAddress(), BluzDeviceFactory.ConnectionState.A2DP_CONNECTED);
                    devices.add(myDevice);
                    mAdapter.notifyDataSetChanged();
                }
            }
			if (devices.size()==0){
				llEmpty.setVisibility(View.VISIBLE);
				lvDevice.setVisibility(View.GONE);
			}else{
				llEmpty.setVisibility(View.GONE);
				lvDevice.setVisibility(View.VISIBLE);
			}
		}

		@Override
		public void onFound(BluetoothDevice device) {
			showLog("发现新设备===" + device.getName());
			if(!TextUtils.isEmpty(device.getName())){
				if (findDevice(device) == null){
					String name = device.getName();
					name = name.toLowerCase();
					if(!name.contains("BLE")){
						MyDevice myDevice = new MyDevice(device.getName(),device.getAddress(), BluzDeviceFactory.ConnectionState.A2DP_DISCONNECTED);
						devices.add(myDevice);
						mAdapter.notifyDataSetChanged();
					}
				}
			}
		}
	};

	private synchronized MyDevice findDevice(BluetoothDevice device){
		MyDevice md = null;
		for (MyDevice d : devices){
			if (d.getDeviceAddress().equals(device.getAddress())){
				md = d;
				break;
			}
		}
		return md;
	}


	/**
	 * 删除数据库中已有的设备
	 * @param device
     */
	private synchronized void delDeviceIfHas(BluetoothDevice device){
		try {
			List<MyDevice> all = dbUtils.findAll(MyDevice.class);
			if (all!=null&&all.size()>0){
				for (MyDevice d : all){
					if (d.getDeviceAddress().equals(device.getAddress())){
						dbUtils.delete(d);
						break;
					}
				}
			}
		} catch (DbException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void onResume() {
		super.onResume();
        mBluzConnector.setOnDiscoveryListener(discoveryListener);
        BluetoothDevice a2dpDevice = mBluzConnector.getConnectedA2dpDevice();
        if(null != a2dpDevice){
            if(null == findDevice(a2dpDevice)){
                MyDevice myDevice = new MyDevice(a2dpDevice.getName(),a2dpDevice.getAddress(), BluzDeviceFactory.ConnectionState.A2DP_CONNECTED);
                devices.add(myDevice);
                mAdapter.notifyDataSetChanged();
            }
        }
        if (devices.size()==0){
            llEmpty.setVisibility(View.VISIBLE);
            lvDevice.setVisibility(View.GONE);
        }else{
            llEmpty.setVisibility(View.GONE);
            lvDevice.setVisibility(View.VISIBLE);
        }
    }



	@Override
	public void onHiddenChanged(boolean hidden) {
		if(!hidden){
			onResume();
		}else{
			onPause();
		}
		super.onHiddenChanged(hidden);
	}

	@Override
	public void onPause() {
		mBluzConnector.setOnDiscoveryListener(null);
		super.onPause();
	}
}
