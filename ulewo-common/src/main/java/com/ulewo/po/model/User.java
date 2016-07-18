package com.ulewo.po.model;

import java.util.Date;

public class User extends UserKey {
    private String userName;

    private String userIcon;

    private String userBg;

    private String age;

    private String sex;

    private String characters;

    private Integer mark;

    private String address;

    private String work;

    private Date registerTime;

    private Date lastLoginTime;

    private String activationCode;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public String getUserIcon() {
        return userIcon;
    }

    public void setUserIcon(String userIcon) {
        this.userIcon = userIcon == null ? null : userIcon.trim();
    }

    public String getUserBg() {
        return userBg;
    }

    public void setUserBg(String userBg) {
        this.userBg = userBg == null ? null : userBg.trim();
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age == null ? null : age.trim();
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex == null ? null : sex.trim();
    }

    public String getCharacters() {
        return characters;
    }

    public void setCharacters(String characters) {
        this.characters = characters == null ? null : characters.trim();
    }

    public Integer getMark() {
        return mark;
    }

    public void setMark(Integer mark) {
        this.mark = mark;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public String getWork() {
        return work;
    }

    public void setWork(String work) {
        this.work = work == null ? null : work.trim();
    }

    public Date getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(Date registerTime) {
        this.registerTime = registerTime;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getActivationCode() {
        return activationCode;
    }

    public void setActivationCode(String activationCode) {
        this.activationCode = activationCode == null ? null : activationCode.trim();
    }
}