package com.ulewo.test.service;

import java.util.Date;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import com.ulewo.exception.BusinessException;
import com.ulewo.po.model.User;
import com.ulewo.service.UserService;

@ContextConfiguration(locations = {"classpath:spring.xml"})
public class ServiceTest extends AbstractTestNGSpringContextTests{
	
	Logger logger=LoggerFactory.getLogger(ServiceTest.class);

	@Resource 
	private UserService UserService;

	/**
	 * 测试注册
	 */
	@Test
	public void testRegister() {
		User user = new User();
		user.setUserName("dehua");
		user.setEmail("dehua@qq.com");
		user.setPassword("123456");
		try {
			this.UserService.register(user);
		} catch (BusinessException e) {
			logger.error(e.getMessage());
		}
	}
	
	@Test
	public void testLogin() {
		try {
			User user = UserService.login("dehua@qq.com", "1213456", true);
			logger.info(user.getUserName());
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		
	}
}
