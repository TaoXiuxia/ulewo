package com.ulewo.po.enums;

public enum MarkEnum {
	MARK_SIGNIN(2,"签到"), MARK_SIGNIN_CONTINUE(10,"连续签到"), MARK_SPIT_SLOT(2,"吐槽"); 
	
	private int mark;
	private String desc;
	
	private MarkEnum(int mark, String desc){
		this.mark=mark;
	}
	
	public int getMark() {
		return mark;
	}
	public void setMark(int mark) {
		this.mark = mark;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	
	
}
