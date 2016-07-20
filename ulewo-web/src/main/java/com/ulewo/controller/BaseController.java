package com.ulewo.controller;

import java.lang.reflect.Method;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ulewo.po.model.SessionUser;
import com.ulewo.utils.Constants;

public class BaseController {

	private Logger logger = LoggerFactory.getLogger(BaseController.class);
	
	public void setUserBaseInfo(Class<?> classz, Object obj, HttpSession session) {
		SessionUser sessionUser = (SessionUser) session.getAttribute(Constants.SESSION_USER_KEY);
		Integer userId = sessionUser.getUserId();
		String userIcon = sessionUser.getUserIcon();
		String userName = sessionUser.getUserName();

		try {
			Method userIdMethod = classz.getDeclaredMethod("setUserId");
			userIdMethod.invoke(obj, userId);
			Method userIconMethod = classz.getDeclaredMethod("setUserIcon");
			userIconMethod.invoke(obj, userIcon);
			Method userNameMethod = classz.getDeclaredMethod("setUserName");
			userNameMethod.invoke(obj, userName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Integer getUserId(HttpSession session) {
		Object sessionObj = session.getAttribute(Constants.SESSION_USER_KEY);
		if (null != sessionObj) {
			SessionUser sessionUser = (SessionUser) sessionObj;
			return sessionUser.getUserId();
		}
		return null;
	}

	public SessionUser getSessionUser(HttpSession session) {
		Object sessionObj = session.getAttribute(Constants.SESSION_USER_KEY);
		if (null != sessionObj) {
			return (SessionUser) sessionObj;
		}
		return null;
	}
}
