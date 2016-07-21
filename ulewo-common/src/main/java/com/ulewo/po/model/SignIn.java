package com.ulewo.po.model;

import java.util.Date;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.ulewo.po.serializ.CustomDateSerializer;

public class SignIn {

	private Integer userId;
	private String userName;
	private String userIcon;
	private Date signDate;
	@JsonSerialize(using = CustomDateSerializer.class)
	private Date signTime;
	private boolean isContinueSignIn;

	public boolean isContinueSignIn() {
		return isContinueSignIn;
	}

	public void setContinueSignIn(boolean isContinueSignIn) {
		this.isContinueSignIn = isContinueSignIn;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Date getSignDate() {
		return signDate;
	}

	public void setSignDate(Date signDate) {
		this.signDate = signDate;
	}

	public String getUserIcon() {
		return userIcon;
	}

	public void setUserIcon(String userIcon) {
		this.userIcon = userIcon == null ? null : userIcon.trim();
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName == null ? null : userName.trim();
	}

	public Date getSignTime() {
		return signTime;
	}

	public void setSignTime(Date signTime) {
		this.signTime = signTime;
	}
}