package com.ulewo.po.model;

import java.util.Date;

public class SignInKey {
    private Integer userId;

    private Date signDate;

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
}