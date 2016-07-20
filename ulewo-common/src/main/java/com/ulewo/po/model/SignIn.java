package com.ulewo.po.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class SignIn {
	
	private Integer userId;
	private String userName;
    private String userIcon;
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern ="yyyy-MM-dd", timezone="GMT-8")
    private Date signDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern ="yyyy-MM-dd HH:mm:ss", timezone="GMT-8")
    private Date signTime;
    private boolean continueSignIn;

    public boolean isContinueSignIn() {
		return continueSignIn;
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

	public void setContinueSignIn(boolean continueSignIn) {
		this.continueSignIn = continueSignIn;
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