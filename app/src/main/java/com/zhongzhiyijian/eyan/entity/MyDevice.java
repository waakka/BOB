package com.zhongzhiyijian.eyan.entity;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

@Table(name = "bt_device")
public class MyDevice extends BaseEntity{
	@Column(name = "id",isId = true,autoGen = true)
	private int id;
	@Column(name = "deviceName")
	private String deviceName;
	@Column(name = "deviceAddress")
	private String deviceAddress;
	@Column(name = "state")
	private int state;
	public String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	public String getDeviceAddress() {
		return deviceAddress;
	}
	public void setDeviceAddress(String deviceAddress) {
		this.deviceAddress = deviceAddress;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public MyDevice(String deviceName, String deviceAddress, int state) {
		this.deviceName = deviceName;
		this.deviceAddress = deviceAddress;
		this.state = state;
	}

	public MyDevice() {
	}

	@Override
	public String toString() {
		return "MyDevice{" +
				"id=" + id +
				", deviceName='" + deviceName + '\'' +
				", deviceAddress='" + deviceAddress + '\'' +
				", state=" + state +
				'}';
	}
}
