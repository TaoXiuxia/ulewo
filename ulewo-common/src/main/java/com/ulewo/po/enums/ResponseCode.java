package com.ulewo.po.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ResponseCode {

	/**
	 * code:200<br>
	 * 请求成功
	 */
	SUCCESS(200, "请求成功"),
	
	/**
	 * code:403<br>
	 * 没有权限
	 */
	NOPEMISSION(403, "没有权限"),
	
	/**
	 * code:415<br>
	 * 业务异常
	 */
	BUSINESSERROR(415,"业务异常"),

	/**
	 * code:425<br>
	 * 验证码错误
	 */
	CODEERROR(425,"验证码错误"),
	
	/**
	 * code:500<br>
	 * 服务器错误
	 */
	SERVERERROR(500,"服务器错误"),
	
	/**
	 * code:401<br>
	 * 登陆超时
	 */
	LOGINTIMEOUT(401,"登陆超时"),
	
	/**
	 * code:405<br>
	 * 登录失败次数超过三次
	 */
	MOREMAXLOGINCOUNT(405,"登录失败次数超过三次");
	
	private int code;
	private String desc;
	
	private ResponseCode(int code,String desc){
		this.code=code;
		this.desc=desc;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	
}
