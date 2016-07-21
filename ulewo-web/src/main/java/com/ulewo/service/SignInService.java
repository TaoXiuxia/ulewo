package com.ulewo.service;

import java.util.List;

import com.ulewo.exception.BusinessException;
import com.ulewo.po.model.SessionUser;
import com.ulewo.po.model.SignIn;
import com.ulewo.po.model.SignInInfo;
import com.ulewo.po.query.SignInQuery;
import com.ulewo.utils.PaginationResult;

public interface SignInService {

	/**
	 * 获取用户当天签到信息
	 * @param userId
	 * @return
	 */
	public SignInInfo findSignInInfoByUserId(Integer userId);
	
	/**
	 * 签到
	 * @param sessionUser
	 * @return
	 * @throws BusinessException
	 */
	public SignIn doSignIn(SessionUser sessionUser)throws BusinessException;
	
	/**
	 * 查询当天所有的签到信息
	 * @param query
	 * @return
	 */
	public PaginationResult<SignIn> findCurDaySignIn(SignInQuery query);
	
	/**
	 * 根据年份获取用户的签到信息
	 * @param userId
	 * @param year
	 * @return
	 */
	public List<Calendar4SignIn> findUserSignInsByYear(Integer userId, Integer year);
}
