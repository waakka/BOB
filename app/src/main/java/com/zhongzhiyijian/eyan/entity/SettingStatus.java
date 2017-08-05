package com.zhongzhiyijian.eyan.entity;

public class SettingStatus {

	private boolean isUSBAutoConnect;
	private String style;
	private String language;
	private int txtSize;
	private boolean isAutoConnect;
	private String versionCode;
	
	
	public boolean isUSBAutoConnect() {
		return isUSBAutoConnect;
	}
	public void setUSBAutoConnect(boolean isUSBAutoConnect) {
		this.isUSBAutoConnect = isUSBAutoConnect;
	}
	public String getStyle() {
		return style;
	}
	public void setStyle(String style) {
		this.style = style;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public int getTxtSize() {
		return txtSize;
	}
	public void setTxtSize(int txtSize) {
		this.txtSize = txtSize;
	}
	public boolean isAutoConnect() {
		return isAutoConnect;
	}
	public void setAutoConnect(boolean isAutoConnect) {
		this.isAutoConnect = isAutoConnect;
	}
	public String getVersionCode() {
		return versionCode;
	}
	public void setVersionCode(String versionCode) {
		this.versionCode = versionCode;
	}
	@Override
	public String toString() {
		return "SettingStatus [isUSBAutoConnect=" + isUSBAutoConnect + ", style=" + style + ", language=" + language
				+ ", txtSize=" + txtSize + ", isAutoConnect=" + isAutoConnect + ", versionCode=" + versionCode + "]";
	}
	
	
	
}
