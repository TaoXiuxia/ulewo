package com.ulewo.po.model;

import java.util.List;

public class Calendar4SignIn {

	private String month;
	private int firstDay;
	private int monthDays;
	private List<DaySignInfo> dayInfos;
	
	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public int getFirstDay() {
		return firstDay;
	}

	public void setFirstDay(int firstDay) {
		this.firstDay = firstDay;
	}

	public int getMonthDays() {
		return monthDays;
	}

	public void setMonthDays(int monthDays) {
		this.monthDays = monthDays;
	}

	public List getDayInfos() {
		return dayInfos;
	}

	public void setDayInfos(List dayInfos) {
		this.dayInfos = dayInfos;
	}



}
