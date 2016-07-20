package com.ulewo.po.model;

public class SignInInfo{
	
	/**
	 * 用户签到数量
	 */
	private Integer userSignInCount;
	
	/**
	 * 是否已经签到
	 */
	private boolean haveSignInToday;
	
	/**
	 * 今日签到数量（人数）
	 */
	private Integer todaySignInCount;
	
	/**
	 * 当前星期
	 */
	private String week;
	
	/**
	 * 当前日期
	 */
	private String curDay;

	public Integer getUserSignInCount() {
		return userSignInCount;
	}

	public void setUserSignInCount(Integer userSignInCount) {
		this.userSignInCount = userSignInCount;
	}

	public boolean isHaveSignInToday() {
		return haveSignInToday;
	}

	public void setHaveSignInToday(boolean haveSignInToday) {
		this.haveSignInToday = haveSignInToday;
	}

	public Integer getTodaySignInCount() {
		return todaySignInCount;
	}

	public void setTodaySignInCount(Integer todaySignInCount) {
		this.todaySignInCount = todaySignInCount;
	}


	public String getWeek() {
		return week;
	}

	public void setWeek(String week) {
		this.week = week;
	}

	public String getCurDay() {
		return curDay;
	}

	public void setCurDay(String curDay) {
		this.curDay = curDay;
	}
	
	
  
}