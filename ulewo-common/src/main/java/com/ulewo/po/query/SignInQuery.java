package com.ulewo.po.query;

import java.util.Date;

public class SignInQuery extends BaseQuery {

	private Integer userId;
	private Date curDate;
	private Date startDate;
	private Date endDate;
	private Integer year;

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Date getCurDate() {
		return curDate;
	}

	public void setCurDate(Date curDate) {
		this.curDate = curDate;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}
}
