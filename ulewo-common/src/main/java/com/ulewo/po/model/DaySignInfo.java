package com.ulewo.po.model;


public class DaySignInfo {

	private boolean curDay;
	private String day;
	private boolean signInType;
	private boolean beforeNowDate;

	public boolean isBeforeNowDate() {
		return beforeNowDate;
	}

	public void setBeforeNowDate(boolean beforeNowDate) {
		this.beforeNowDate = beforeNowDate;
	}

	public boolean isSignInType() {
		return signInType;
	}

	public void setSignInType(boolean signInType) {
		this.signInType = signInType;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public boolean isCurDay() {
		return curDay;
	}

	public void setCurDay(boolean curDay) {
		this.curDay = curDay;
	}
	
	
}
