package com.zhongzhiyijian.eyan.entity;

public class PTStatus {
	private boolean statusZhenJiuIsOpen;
	private int statusZhenJiuIntensity;
	private boolean statusZhenJiuIsClock;
	private int statusZhenJiuClockTime;
	
	private boolean statusAnMoIsOpen;
	private int statusAnMoIntensity;
	private boolean statusAnMoIsClock;
	private int statusAnMoClockTime;
	
	private boolean statusLiLiaoIsOpen;
	private int statusLiLiaoIntensity;
	private boolean statusLiLiaoIsClock;
	private int statusLiLiaoClockTime;
	
	private boolean statusYueLiaoIsOpen;
	private int statusYueLiaoIntensity;
	private int statusYueLiaoFrequency;
	private boolean statusYueLiaoIsClock;
	private int statusYueLiaoClockTime;
	
	
	public boolean isStatusZhenJiuIsOpen() {
		return statusZhenJiuIsOpen;
	}
	public void setStatusZhenJiuIsOpen(boolean statusZhenJiuIsOpen) {
		this.statusZhenJiuIsOpen = statusZhenJiuIsOpen;
	}
	public int getStatusZhenJiuIntensity() {
		return statusZhenJiuIntensity;
	}
	public void setStatusZhenJiuIntensity(int statusZhenJiuIntensity) {
		this.statusZhenJiuIntensity = statusZhenJiuIntensity;
	}
	public boolean isStatusZhenJiuIsClock() {
		return statusZhenJiuIsClock;
	}
	public void setStatusZhenJiuIsClock(boolean statusZhenJiuIsClock) {
		this.statusZhenJiuIsClock = statusZhenJiuIsClock;
	}
	public int getStatusZhenJiuClockTime() {
		return statusZhenJiuClockTime;
	}
	public void setStatusZhenJiuClockTime(int statusZhenJiuClockTime) {
		this.statusZhenJiuClockTime = statusZhenJiuClockTime;
	}
	public boolean isStatusAnMoIsOpen() {
		return statusAnMoIsOpen;
	}
	public void setStatusAnMoIsOpen(boolean statusAnMoIsOpen) {
		this.statusAnMoIsOpen = statusAnMoIsOpen;
	}
	public int getStatusAnMoIntensity() {
		return statusAnMoIntensity;
	}
	public void setStatusAnMoIntensity(int statusAnMoIntensity) {
		this.statusAnMoIntensity = statusAnMoIntensity;
	}
	public boolean isStatusAnMoIsClock() {
		return statusAnMoIsClock;
	}
	public void setStatusAnMoIsClock(boolean statusAnMoIsClock) {
		this.statusAnMoIsClock = statusAnMoIsClock;
	}
	public int getStatusAnMoClockTime() {
		return statusAnMoClockTime;
	}
	public void setStatusAnMoClockTime(int statusAnMoClockTime) {
		this.statusAnMoClockTime = statusAnMoClockTime;
	}
	public boolean isStatusLiLiaoIsOpen() {
		return statusLiLiaoIsOpen;
	}
	public void setStatusLiLiaoIsOpen(boolean statusLiLiaoIsOpen) {
		this.statusLiLiaoIsOpen = statusLiLiaoIsOpen;
	}
	public int getStatusLiLiaoIntensity() {
		return statusLiLiaoIntensity;
	}
	public void setStatusLiLiaoIntensity(int statusLiLiaoIntensity) {
		this.statusLiLiaoIntensity = statusLiLiaoIntensity;
	}
	public boolean isStatusLiLiaoIsClock() {
		return statusLiLiaoIsClock;
	}
	public void setStatusLiLiaoIsClock(boolean statusLiLiaoIsClock) {
		this.statusLiLiaoIsClock = statusLiLiaoIsClock;
	}
	public int getStatusLiLiaoClockTime() {
		return statusLiLiaoClockTime;
	}
	public void setStatusLiLiaoClockTime(int statusLiLiaoClockTime) {
		this.statusLiLiaoClockTime = statusLiLiaoClockTime;
	}
	public boolean isStatusYueLiaoIsOpen() {
		return statusYueLiaoIsOpen;
	}
	public void setStatusYueLiaoIsOpen(boolean statusYueLiaoIsOpen) {
		this.statusYueLiaoIsOpen = statusYueLiaoIsOpen;
	}
	public int getStatusYueLiaoIntensity() {
		return statusYueLiaoIntensity;
	}
	public void setStatusYueLiaoIntensity(int statusYueLiaoIntensity) {
		this.statusYueLiaoIntensity = statusYueLiaoIntensity;
	}
	public int getStatusYueLiaoFrequency() {
		return statusYueLiaoFrequency;
	}
	public void setStatusYueLiaoFrequency(int statusYueLiaoFrequency) {
		this.statusYueLiaoFrequency = statusYueLiaoFrequency;
	}
	public boolean isStatusYueLiaoIsClock() {
		return statusYueLiaoIsClock;
	}
	public void setStatusYueLiaoIsClock(boolean statusYueLiaoIsClock) {
		this.statusYueLiaoIsClock = statusYueLiaoIsClock;
	}
	public int getStatusYueLiaoClockTime() {
		return statusYueLiaoClockTime;
	}
	public void setStatusYueLiaoClockTime(int statusYueLiaoClockTime) {
		this.statusYueLiaoClockTime = statusYueLiaoClockTime;
	}
	@Override
	public String toString() {
		return "PTStatus [针灸是否打开=" + statusZhenJiuIsOpen
				+ ", 针灸强度=" + statusZhenJiuIntensity
				+ ", 针灸定时是否打开=" + statusZhenJiuIsClock
				+ ", 针灸定时为=" + statusZhenJiuClockTime
				+ ", 按摩是否打开=" + statusAnMoIsOpen
				+ ", 按摩强度=" + statusAnMoIntensity
				+ ", 按摩定时是否打开=" + statusAnMoIsClock
				+ ", 按摩定时为=" + statusAnMoClockTime
				+ ", 理疗是否打开=" + statusLiLiaoIsOpen
				+ ", 理疗强度=" + statusLiLiaoIntensity
				+ ", 理疗定时是否打开=" + statusLiLiaoIsClock
				+ ", 理疗定时为=" + statusLiLiaoClockTime
				+ ", 乐疗是否打开=" + statusYueLiaoIsOpen
				+ ", 乐疗强度=" + statusYueLiaoIntensity
				+ ", 乐疗频率=" + statusYueLiaoFrequency
				+ ", 乐疗定时是否打开=" + statusYueLiaoIsClock
				+ ", 乐疗定时为=" + statusYueLiaoClockTime + "]";
	}
	
	
}
