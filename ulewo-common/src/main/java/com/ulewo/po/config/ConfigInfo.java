package com.ulewo.po.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("configInfo")
public class ConfigInfo {
	
	/**
	 * 找回密码发送的邮箱地址
	 */
	@Value("#{applicationProperties['ulewo.email.findemail']}")
	private String findemail;
	
	/**
	 * 找回密码发送邮箱的密码
	 */
	@Value("#{applicationProperties['ulewo.email.findpwd']}")
	private String findpwd;

	public String getFindemail() {
		return findemail;
	}

	public void setFindemail(String findemail) {
		this.findemail = findemail;
	}

	public String getFindpwd() {
		return findpwd;
	}

	public void setFindpwd(String findpwd) {
		this.findpwd = findpwd;
	}
	
	
	
}
