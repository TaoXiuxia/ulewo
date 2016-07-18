package com.ulewo.test.service;

import javax.annotation.Resource;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import com.ulewo.po.model.SessionUser;
import com.ulewo.po.model.SignInInfo;
import com.ulewo.service.SignInService;

@ContextConfiguration(locations = {"classpath:spring.xml"})
public class SignInTest extends AbstractTestNGSpringContextTests{

	@Resource
	private SignInService signInService;
	
	@Test
	public void doSignIn(){
		SessionUser sessionUser = new SessionUser();
		sessionUser.setUserId(10701);
		sessionUser.setUserName("德华");
		sessionUser.setUserIcon("defgroupicon/02.png");
		try {
			
		} catch (Exception e) {
		}
	}

	@Test
	public void findSignInInfoByUserId(){
		SignInInfo info=signInService.findSignInInfoByUserId(1);
		System.out.println("当前日期："+info.getCurDay());
		System.out.println("星期几:"+info.getWeek());
		System.out.println("今天签到数量："+info.getTodaySignInCount());
		System.out.println("我的签到数量："+info.getUserSignInCount());
		System.out.println("今天是否签到："+info.isHaveSignInToday());
	}
	
	
}


















